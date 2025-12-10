package com.gustavo.mspedidos.dto;

import java.math.BigDecimal;

public record PagamentoRequestDTO(
        Long pedidoId,
        BigDecimal valor,
        String tipoPagamento,   // "PIX", "CARTAO_CREDITO", "CARTAO_DEBITO"
        String detalhes
) {
}
