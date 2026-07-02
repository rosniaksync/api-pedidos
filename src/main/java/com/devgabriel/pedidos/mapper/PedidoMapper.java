package com.devgabriel.pedidos.mapper;

import com.devgabriel.pedidos.domain.ItemPedidoEntity;
import com.devgabriel.pedidos.domain.PedidoEntity;
import com.devgabriel.pedidos.dto.ClienteResumoDto;
import com.devgabriel.pedidos.dto.ItemPedidoResponseDto;
import com.devgabriel.pedidos.dto.PedidoResponseDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PedidoMapper {

    public PedidoResponseDto toResponseDto(PedidoEntity pedido) {

        List<ItemPedidoResponseDto> itensResponse = pedido.getItens().stream()
                .map(this::itemToResponseDto)
                .toList();

        ClienteResumoDto clienteResumo = new ClienteResumoDto(
                pedido.getCliente().getId(),
                pedido.getCliente().getNome()
        );

        return new PedidoResponseDto(
                pedido.getId(),
                clienteResumo,
                itensResponse,
                pedido.getDataPedido(),
                pedido.getStatus(),
                pedido.getValorTotal()
        );
    }

    public ItemPedidoResponseDto itemToResponseDto(ItemPedidoEntity itemPedido) {
        BigDecimal subtotal = itemPedido.getValorUnitario()
                .multiply(BigDecimal.valueOf(itemPedido.getQuantidade()));

        return new ItemPedidoResponseDto(
                itemPedido.getId(),
                itemPedido.getProduto().getId(),
                itemPedido.getProduto().getNome(),
                itemPedido.getQuantidade(),
                itemPedido.getValorUnitario(),
                subtotal
        );
    }
}