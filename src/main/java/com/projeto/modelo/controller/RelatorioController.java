package com.projeto.modelo.controller;

import com.projeto.modelo.dto.relatorio.RelatorioHorasDTO;
import com.projeto.modelo.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
@CrossOrigin(origins = "*") 
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/horas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RelatorioHorasDTO>> getRelatorioHoras() {
        return ResponseEntity.ok(relatorioService.gerarRelatorioHoras());
    }

    @Autowired
    private com.projeto.modelo.repository.UsuarioRepository usuarioRepository;

    private com.projeto.modelo.model.entity.Usuario getUsuarioLogado() {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() != null) {
            String tempUsername = "";
            if (auth.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
                tempUsername = ((org.springframework.security.core.userdetails.UserDetails) auth.getPrincipal()).getUsername();
            } else {
                tempUsername = auth.getPrincipal().toString();
            }
            if (auth.getPrincipal() instanceof com.projeto.modelo.model.entity.Usuario) {
                 return (com.projeto.modelo.model.entity.Usuario) auth.getPrincipal();
            }
            final String username = tempUsername;
            return usuarioRepository.findByEmail(username).orElseGet(() -> 
                usuarioRepository.findByEmailAuthsoft(username).orElse(null)
            );
        }
        return null;
    }

    @GetMapping("/avancado")
    public ResponseEntity<List<com.projeto.modelo.dto.relatorio.RelatorioItemDTO>> getRelatorioAvancado(
            @org.springframework.web.bind.annotation.ModelAttribute com.projeto.modelo.dto.relatorio.RelatorioFiltroDTO filtro) {
        
        com.projeto.modelo.model.entity.Usuario logado = getUsuarioLogado();
        if (logado != null && logado.getPermissao() != com.projeto.modelo.model.enums.PermissaoStatus.ADMIN) {
            filtro.setColaboradorId(logado.getId());
        }
        
        return ResponseEntity.ok(relatorioService.gerarRelatorioAvancado(filtro));
    }

    @Autowired
    private com.projeto.modelo.service.BoardAnaliticoService boardAnaliticoService;

    @GetMapping("/board-analitico/tipo-servico")
    public ResponseEntity<com.projeto.modelo.dto.relatorio.BoardTipoServicoDTO> getBoardTipoServico(
            @org.springframework.web.bind.annotation.RequestParam(required = false) Integer ano,
            @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "false") Boolean somenteRecebidos) {
        
        int anoFinal = ano != null ? ano : java.time.LocalDate.now().getYear();
        return ResponseEntity.ok(boardAnaliticoService.gerarBoardPorTipoServico(anoFinal, somenteRecebidos));
    }

    @GetMapping("/board-analitico/centro-custo")
    public ResponseEntity<com.projeto.modelo.dto.relatorio.BoardCentroCustoDTO> getBoardCentroCusto(
            @org.springframework.web.bind.annotation.RequestParam(required = false) Integer ano,
            @org.springframework.web.bind.annotation.RequestParam(required = false, defaultValue = "false") Boolean somentePagos) {
        
        int anoFinal = ano != null ? ano : java.time.LocalDate.now().getYear();
        return ResponseEntity.ok(boardAnaliticoService.gerarBoardPorCentroCusto(anoFinal, somentePagos));
    }
}
