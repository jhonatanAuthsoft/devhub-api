package com.projeto.modelo.service.imp;

import com.projeto.modelo.controller.dto.request.CartaoCreditoRequestDTO;
import com.projeto.modelo.controller.dto.response.CartaoCreditoResponseDTO;
import com.projeto.modelo.controller.dto.response.ContaBancariaResponseDTO;
import com.projeto.modelo.model.entity.CartaoCredito;
import com.projeto.modelo.model.entity.ContaBancaria;
import com.projeto.modelo.repository.CartaoCreditoRepository;
import com.projeto.modelo.repository.ContaBancariaRepository;
import com.projeto.modelo.service.CartaoCreditoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartaoCreditoServiceImp implements CartaoCreditoService {

    private final CartaoCreditoRepository cartaoCreditoRepository;
    private final ContaBancariaRepository contaBancariaRepository;

    @Override
    public CartaoCreditoResponseDTO salvar(CartaoCreditoRequestDTO dto) {
        ContaBancaria conta = contaBancariaRepository.findById(dto.getContaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));

        CartaoCredito cartao = CartaoCredito.builder()
                .descricao(dto.getDescricao())
                .limite(dto.getLimite())
                .diaFechamento(dto.getDiaFechamento())
                .diaVencimento(dto.getDiaVencimento())
                .conta(conta)
                .build();

        cartao = cartaoCreditoRepository.save(cartao);
        return toDTO(cartao);
    }

    @Override
    public CartaoCreditoResponseDTO atualizar(UUID id, CartaoCreditoRequestDTO dto) {
        CartaoCredito cartao = getCartaoCredito(id);
        
        ContaBancaria conta = contaBancariaRepository.findById(dto.getContaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));

        cartao.setDescricao(dto.getDescricao());
        cartao.setLimite(dto.getLimite());
        cartao.setDiaFechamento(dto.getDiaFechamento());
        cartao.setDiaVencimento(dto.getDiaVencimento());
        cartao.setConta(conta);

        cartao = cartaoCreditoRepository.save(cartao);
        return toDTO(cartao);
    }

    @Override
    public CartaoCreditoResponseDTO buscarPorId(UUID id) {
        return toDTO(getCartaoCredito(id));
    }

    @Override
    public List<CartaoCreditoResponseDTO> listarTodos() {
        return cartaoCreditoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deletar(UUID id) {
        CartaoCredito cartao = getCartaoCredito(id);
        cartaoCreditoRepository.delete(cartao);
    }

    private CartaoCredito getCartaoCredito(UUID id) {
        return cartaoCreditoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cartão de Crédito não encontrado"));
    }

    private CartaoCreditoResponseDTO toDTO(CartaoCredito cartao) {
        ContaBancariaResponseDTO contaDTO = ContaBancariaResponseDTO.builder()
                .id(cartao.getConta().getId())
                .nome(cartao.getConta().getNome())
                .saldoAtual(cartao.getConta().getSaldoAtual())
                .ativo(cartao.getConta().getAtivo())
                .emiteBoleto(cartao.getConta().getEmiteBoleto())
                .build();

        return CartaoCreditoResponseDTO.builder()
                .id(cartao.getId())
                .descricao(cartao.getDescricao())
                .limite(cartao.getLimite())
                .diaFechamento(cartao.getDiaFechamento())
                .diaVencimento(cartao.getDiaVencimento())
                .conta(contaDTO)
                .build();
    }
}
