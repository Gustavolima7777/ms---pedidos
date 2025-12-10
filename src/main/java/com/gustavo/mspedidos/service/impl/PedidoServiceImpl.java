package com.gustavo.mspedidos.service.impl;

import com.gustavo.mspedidos.dto.ItemPedidoDTO;
import com.gustavo.mspedidos.dto.PagamentoRequestDTO;
import com.gustavo.mspedidos.dto.PagamentoResponseDTO;
import com.gustavo.mspedidos.dto.PedidoRequestDTO;
import com.gustavo.mspedidos.dto.PedidoResponseDTO;
import com.gustavo.mspedidos.model.Cliente;
import com.gustavo.mspedidos.model.ItemPedido;
import com.gustavo.mspedidos.model.Pedido;
import com.gustavo.mspedidos.model.Produto;
import com.gustavo.mspedidos.repository.ClienteRepository;
import com.gustavo.mspedidos.repository.PedidoRepository;
import com.gustavo.mspedidos.repository.ProdutoRepository;
import com.gustavo.mspedidos.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteRepository clienteRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ms.pagamentos.url}")
    private String pagamentosUrl;

    public PedidoServiceImpl(PedidoRepository pedidoRepository,
                             ProdutoRepository produtoRepository,
                             ClienteRepository clienteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional
    public PedidoResponseDTO criarPedido(PedidoRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);

        List<ItemPedido> itens = dto.itens().stream()
                .map(itemDto -> criarItemPedido(itemDto, pedido))
                .collect(Collectors.toList());

        pedido.setItens(itens);

        BigDecimal total = itens.stream()
                .map(i -> i.getPrecoUnitario().multiply(BigDecimal.valueOf(i.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setValorTotal(total);

        Pedido salvo = pedidoRepository.save(pedido);

        // Chama o ms-pagamentos após salvar o pedido
        registrarPagamento(salvo, dto.formaPagamento());

        return PedidoResponseDTO.fromEntity(salvo);
    }

    private ItemPedido criarItemPedido(ItemPedidoDTO itemDto, Pedido pedido) {
        Produto produto = produtoRepository.findById(itemDto.produtoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setProduto(produto);
        item.setQuantidade(itemDto.quantidade());
        item.setPrecoUnitario(produto.getPreco());
        return item;
    }

    @Override
    public PedidoResponseDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        return PedidoResponseDTO.fromEntity(pedido);
    }

    @Override
    public List<PedidoResponseDTO> listarTodos() {
        return pedidoRepository.findAll()
                .stream()
                .map(PedidoResponseDTO::fromEntity)
                .toList();
    }

    private void registrarPagamento(Pedido pedido, String formaPagamento) {

        PagamentoRequestDTO request = new PagamentoRequestDTO(
                pedido.getId(),
                pedido.getValorTotal(),
                formaPagamento,
                "Pagamento do pedido " + pedido.getId()
        );

        PagamentoResponseDTO response = restTemplate.postForObject(
                pagamentosUrl,
                request,
                PagamentoResponseDTO.class
        );

        if (response == null) {
            throw new RuntimeException("Erro ao processar pagamento para o pedido " + pedido.getId());
        }

        // Se quisesse armazenar algo do pagamento no pedido,
        // precisaríamos criar um campo na entidade Pedido.
        // Como o professor não pediu, só garantimos que o pagamento foi feito.
    }
}
