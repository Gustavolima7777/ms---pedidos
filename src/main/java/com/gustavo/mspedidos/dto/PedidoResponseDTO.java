package com.gustavo.mspedidos.dto;

import com.gustavo.mspedidos.model.ItemPedido;
import com.gustavo.mspedidos.model.Pedido;
import com.gustavo.mspedidos.model.PedidoStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(
        Long id,
        String clienteNome,
        BigDecimal valorTotal,
        PedidoStatus status,
        LocalDateTime dataCriacao,
        List<ItemPedidoResumoDTO> itens
) {

    public static PedidoResponseDTO fromEntity(Pedido pedido) {
        List<ItemPedidoResumoDTO> itensResumo = pedido.getItens().stream()
                .map(PedidoResponseDTO::mapItem)
                .toList();

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getCliente().getNome(),
                pedido.getValorTotal(),
                pedido.getStatus(),
                pedido.getDataCriacao(),
                itensResumo
        );
    }

    private static ItemPedidoResumoDTO mapItem(ItemPedido item) {
        return new ItemPedidoResumoDTO(
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoUnitario()
        );
    }
}
