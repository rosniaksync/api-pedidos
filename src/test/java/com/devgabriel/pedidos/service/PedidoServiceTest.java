package com.devgabriel.pedidos.service;

import com.devgabriel.pedidos.domain.ClienteEntity;
import com.devgabriel.pedidos.domain.PedidoEntity;
import com.devgabriel.pedidos.domain.ProdutoEntity;
import com.devgabriel.pedidos.dto.ItemPedidoRequestDto;
import com.devgabriel.pedidos.dto.PedidoRequestDto;
import com.devgabriel.pedidos.enums.StatusPedido;
import com.devgabriel.pedidos.exception.ResourceNotFoundException;
import com.devgabriel.pedidos.exception.TransicaoInvalidaException;
import com.devgabriel.pedidos.mapper.PedidoMapper;
import com.devgabriel.pedidos.repository.ClienteRepository;
import com.devgabriel.pedidos.repository.PedidoRepository;
import com.devgabriel.pedidos.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private PedidoMapper mapper;

    @Captor
    private ArgumentCaptor<PedidoEntity> captor;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void deveCalcularValorTotalCorretamenteAoCadastrarProduto() {

        ClienteEntity cliente = ClienteEntity.builder()
                .id(1L)
                .nome("Gabriel da Juju")
                .build();

        ProdutoEntity produto = ProdutoEntity.builder()
                .id(1L)
                .nome("Cola bastão")
                .valorUnitario(new BigDecimal("12.00"))
                .build();

        PedidoRequestDto pedidoDto = new PedidoRequestDto(
                1L,
                List.of(new ItemPedidoRequestDto(1L, 2))
        );

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(pedidoRepository.save(any(PedidoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        pedidoService.cadastrar(pedidoDto);

        verify(mapper).toResponseDto(captor.capture());

        PedidoEntity pedidoCapturado = captor.getValue();

        assertEquals(0, new BigDecimal("24.00").compareTo(pedidoCapturado.getValorTotal()));
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoEncontrado() {

        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        PedidoRequestDto dto = new PedidoRequestDto(
                1L,
                List.of(new ItemPedidoRequestDto(1L, 2))
        );

        assertThrows(ResourceNotFoundException.class, () -> {
            pedidoService.cadastrar(dto);
        });
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoForEncontrado() {

        ClienteEntity cliente = ClienteEntity.builder()
                .id(1L)
                .nome("Gabriel")
                .build();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        PedidoRequestDto dto = new PedidoRequestDto(
                1L,
                List.of(new ItemPedidoRequestDto(1L, 2))
        );

        assertThrows(ResourceNotFoundException.class, () -> {
            pedidoService.cadastrar(dto);
        });
    }

    @Test
    void deveLancarExcessaoAoCancelarPedidoJaEntregue() {

        PedidoEntity pedido = PedidoEntity.builder()
                .id(1L)
                .status(StatusPedido.ENTREGUE)
                .build();

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        assertThrows(TransicaoInvalidaException.class, () -> {
            pedidoService.cancelar(pedido.getId());
        });
    }

    @Test
    void deveCancelarOPedidoSeEstiverPendente() {

        PedidoEntity pedido = PedidoEntity.builder()
                .id(1L)
                .status(StatusPedido.PENDENTE)
                .build();

        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));

        pedidoService.cancelar(1L);

        assertEquals(StatusPedido.CANCELADO, pedido.getStatus());
    }
}