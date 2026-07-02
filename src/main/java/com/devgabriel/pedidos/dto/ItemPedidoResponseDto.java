package com.devgabriel.pedidos.dto;

import java.math.BigDecimal;

public record ItemPedidoResponseDto(
        Long id,
        Long produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal valorUnitario,
        BigDecimal subtotal
) {}