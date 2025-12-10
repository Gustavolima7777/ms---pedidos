package com.gustavo.mspedidos.service;

import com.gustavo.mspedidos.dto.PedidoRequestDTO;
import com.gustavo.mspedidos.dto.PedidoResponseDTO;

import java.util.List;

public interface PedidoService {

    PedidoResponseDTO criarPedido(PedidoRequestDTO dto);

    PedidoResponseDTO buscarPorId(Long id);

    List<PedidoResponseDTO> listarTodos();
}
