package com.gustavo.mspedidos.dto;

import java.util.List;

public record PedidoRequestDTO(
        Long clienteId,
        String formaPagamento,     // vamos usar depois com o ms-pagamentos
        List<ItemPedidoDTO> itens
) {
}
