package com.gustavo.mspedidos.service;

import com.gustavo.mspedidos.model.Produto;

import java.util.List;

public interface ProdutoService {

    Produto salvar(Produto produto);

    List<Produto> listarTodos();

    Produto buscarPorId(Long id);

    void deletar(Long id);
}
