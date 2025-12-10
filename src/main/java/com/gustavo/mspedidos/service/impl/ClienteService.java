package com.gustavo.mspedidos.service;



import com.gustavo.mspedidos.model.Cliente;

import java.util.List;

public interface ClienteService {

    Cliente salvar(Cliente cliente);

    List<Cliente> listarTodos();

    Cliente buscarPorId(Long id);
}
