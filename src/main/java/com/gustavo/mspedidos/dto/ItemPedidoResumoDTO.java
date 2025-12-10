package com.gustavo.mspedidos.dto;

import java.math.BigDecimal;

public record ItemPedidoResumoDTO(
        Long produtoId,
        String produtoNome,
        Integer quantidade,
        BigDecimal precoUnitario
) {
}
