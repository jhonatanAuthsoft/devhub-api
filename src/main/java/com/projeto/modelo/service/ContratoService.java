package com.projeto.modelo.service;

import com.projeto.modelo.model.entity.ContratoTemplate;
import com.projeto.modelo.model.entity.Projeto;
import com.projeto.modelo.model.enums.TipoProjeto;
import com.projeto.modelo.repository.ContratoTemplateRepository;
import com.projeto.modelo.repository.ProjetoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContratoService {

    private final ContratoTemplateRepository templateRepository;
    private final ProjetoRepository projetoRepository;

    @Transactional
    public ContratoTemplate salvarTemplate(MultipartFile file, TipoProjeto tipoProjeto) throws IOException {
        ContratoTemplate template = templateRepository.findByTipoProjeto(tipoProjeto)
                .orElse(new ContratoTemplate());
        
        template.setTipoProjeto(tipoProjeto);
        template.setNomeArquivo(file.getOriginalFilename());
        template.setDados(file.getBytes());
        template.setContentType(file.getContentType());
        
        return templateRepository.save(template);
    }

    public List<ContratoTemplate> listarTemplates() {
        return templateRepository.findAll();
    }

    @Transactional(readOnly = true)
    public byte[] gerarContrato(UUID projetoId) throws IOException {
        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Projeto não encontrado"));

        ContratoTemplate template = templateRepository.findByTipoProjeto(projeto.getTipoProjeto())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, 
                        "Nenhum template encontrado para o tipo: " + projeto.getTipoProjeto().getDescricao()));

        Map<String, String> replacements = buildReplacements(projeto);

        try (XWPFDocument doc = new XWPFDocument(new ByteArrayInputStream(template.getDados()))) {
            // Replace in paragraphs
            for (XWPFParagraph p : doc.getParagraphs()) {
                replaceInParagraph(p, replacements);
            }

            // Replace in tables
            for (XWPFTable tbl : doc.getTables()) {
                for (XWPFTableRow row : tbl.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            replaceInParagraph(p, replacements);
                        }
                    }
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doc.write(out);
            return out.toByteArray();
        }
    }

    private Map<String, String> buildReplacements(Projeto p) {
        Map<String, String> map = new HashMap<>();
        NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // DADOS DA EMPRESA (CLIENTE)
        if (p.getCliente() != null) {
            map.put("{{NOME_EMPRESA}}", p.getCliente().getNome());
            map.put("{{CNPJ}}", p.getCliente().getCpfCnpj());
            map.put("{{ENDERECO}}", formatarEndereco(p.getCliente()));
            map.put("{{EMAIL}}", p.getCliente().getEmailPrincipal());
            
            // REPRESENTANTE (Busca primeiro quem recebe contrato, ou o primeiro RESPONSAVEL)
            var representante = p.getCliente().getPessoas().stream()
                .filter(pes -> Boolean.TRUE.equals(pes.getRecebeContrato()))
                .findFirst()
                .orElse(p.getCliente().getPessoas().stream()
                    .filter(pes -> "RESPONSAVEL".equals(pes.getTipoPessoa().name()))
                    .findFirst()
                    .orElse(null));
            
            if (representante != null) {
                map.put("{{REPRESENTANTE}}", representante.getNome());
                map.put("{{CPF}}", representante.getCpf() != null ? representante.getCpf() : "");
            } else {
                map.put("{{REPRESENTANTE}}", "__________________________");
                map.put("{{CPF}}", "");
            }
        } else {
            map.put("{{NOME_EMPRESA}}", "");
            map.put("{{CNPJ}}", "");
            map.put("{{ENDERECO}}", "");
            map.put("{{EMAIL}}", "");
            map.put("{{REPRESENTANTE}}", "");
            map.put("{{CPF}}", "");
        }
        
        // DADOS DO PROJETO
        map.put("{{OBJETIVO}}", p.getDescricao() != null ? p.getDescricao() : "");
        map.put("{{ENTREGAVEIS}}", p.getRequisitos() != null ? p.getRequisitos() : "");
        map.put("{{VALOR_TOTAL}}", p.getValorTotal() != null ? currency.format(p.getValorTotal()) : "R$ 0,00");
        
        // PARCELAS
        if (p.getParcelas() != null && !p.getParcelas().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            p.getParcelas().forEach(par -> {
                sb.append(String.format("%dx de %s (Vencimento: %s)\n", 
                    par.getNumero(), 
                    currency.format(par.getValor()), 
                    par.getDataVencimento() != null ? par.getDataVencimento().format(dateFmt) : "A combinar"));
            });
            map.put("{{PARCELAS}}", sb.toString());
        } else {
            map.put("{{PARCELAS}}", "À vista");
        }

        // TAGS LEGADAS (MANTIDAS POR COMPATIBILIDADE)
        map.put("{{CLIENTE}}", p.getCliente() != null ? p.getCliente().getNome() : "");
        map.put("{{CNPJ_CLIENTE}}", p.getCliente() != null ? p.getCliente().getCpfCnpj() : "");
        map.put("{{TITULO_PROJETO}}", p.getTitulo());
        map.put("{{DATA_INICIO}}", p.getDataInicio() != null ? p.getDataInicio().format(dateFmt) : "");
        map.put("{{DATA_FIM}}", p.getDataFimProjeto() != null ? p.getDataFimProjeto().format(dateFmt) : "");
        map.put("{{DATA_HOJE}}", LocalDate.now().format(dateFmt));

        return map;
    }

    private String formatarEndereco(com.projeto.modelo.model.entity.Cliente c) {
        if (c.getLogradouro() == null) return "";
        return String.format("%s, %s - %s, %s - %s, CEP: %s",
                c.getLogradouro(), c.getNumero() != null ? c.getNumero() : "S/N",
                c.getBairro() != null ? c.getBairro() : "",
                c.getCidade() != null ? c.getCidade() : "",
                c.getEstado() != null ? c.getEstado() : "",
                c.getCep() != null ? c.getCep() : "");
    }

    private void replaceInParagraph(XWPFParagraph p, Map<String, String> replacements) {
        String text = p.getText();
        if (text == null || text.isEmpty()) return;

        // Naive line-break handling for {{PARCELAS}}
        // If text is EXACTLY one tag that contains newlines, we should split runs preferably.
        // For simplicity in MVP, we might treat it as text. 
        // POI handles \n in setText by keeping it in one run, but Word often needs <w:br/>.
        
        List<XWPFRun> runs = p.getRuns();
        if (runs != null) {
            for (XWPFRun r : runs) {
                String runText = r.getText(0);
                if (runText != null) {
                    for (Map.Entry<String, String> entry : replacements.entrySet()) {
                        String key = entry.getKey();
                        if (runText.contains(key)) {
                            String val = entry.getValue();
                            if (val.contains("\n")) {
                                // Special handling for newlines
                                runText = runText.replace(key, "");
                                r.setText(runText, 0);
                                String[] lines = val.split("\n");
                                for(int i=0; i<lines.length; i++) {
                                    r.setText(lines[i]);
                                    if(i < lines.length - 1) r.addBreak();
                                }
                            } else {
                                runText = runText.replace(key, val);
                                r.setText(runText, 0);
                            }
                        }
                    }
                }
            }
        }
    }
}
