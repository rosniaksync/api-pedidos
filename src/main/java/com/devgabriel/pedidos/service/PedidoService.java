package com.devgabriel.pedidos.service;

import com.devgabriel.pedidos.domain.ClienteEntity;
import com.devgabriel.pedidos.domain.ItemPedidoEntity;
import com.devgabriel.pedidos.domain.PedidoEntity;
import com.devgabriel.pedidos.domain.ProdutoEntity;
import com.devgabriel.pedidos.dto.*;
import com.devgabriel.pedidos.enums.StatusPedido;
import com.devgabriel.pedidos.exception.ResourceNotFoundException;
import com.devgabriel.pedidos.exception.TransicaoInvalidaException;
import com.devgabriel.pedidos.mapper.PedidoMapper;
import com.devgabriel.pedidos.repository.ClienteRepository;
import com.devgabriel.pedidos.repository.PedidoRepository;
import com.devgabriel.pedidos.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository repository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoMapper mapper;

    @Transactional
    public PedidoResponseDto cadastrar(PedidoRequestDto dto) {
        ClienteEntity cliente = buscarClientePorId(dto.clienteId());

        PedidoEntity pedido = PedidoEntity
                .builder()
                .cliente(cliente)
                .status(StatusPedido.PENDENTE)
                .build();

        List<ItemPedidoEntity> itens = dto.itens().stream()
                .map(itemdto -> {
                    ProdutoEntity produto = buscarProdutoPorId(itemdto.produtoId());

                    return ItemPedidoEntity.builder()
                            .produto(produto)
                            .quantidade(itemdto.quantidade())
                            .valorUnitario(produto.getValorUnitario())
                            .pedido(pedido)
                            .build();
                })
                .toList();

        BigDecimal valorTotal = itens.stream()
                .map(item -> item.getValorUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setItens(itens);
        pedido.setValorTotal(valorTotal);

        PedidoEntity pedidoSalvo = repository.save(pedido);
        return mapper.toResponseDto(pedidoSalvo);
    }

    public Page<PedidoResponseDto> listar(Pageable pageable) {
        Page<PedidoEntity> pedidos = repository.findAll(pageable);
        return pedidos.map(mapper::toResponseDto);
    }

    public PedidoResponseDto listarPorId(Long id) {
        PedidoEntity pedido = buscarPedidoPorId(id);
        return mapper.toResponseDto(pedido);
    }

    @Transactional
    public PedidoResponseDto atualizarStatus(Long id, PedidoStatusDto statusDto) {
        PedidoEntity pedido = buscarPedidoPorId(id);
        pedido.setStatus(statusDto.status());
        return mapper.toResponseDto(pedido);
    }

    @Transactional
    public void cancelar(Long id) {
        PedidoEntity pedido = buscarPedidoPorId(id);

        if(pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new TransicaoInvalidaException("Não é possivel cancelar um pedido já entregue");
        }
        pedido.setStatus(StatusPedido.CANCELADO);
    }

    private PedidoEntity buscarPedidoPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com id: " + id));
    }

    private ClienteEntity buscarClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com id: " + id));
    }

    private ProdutoEntity buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));
    }
}