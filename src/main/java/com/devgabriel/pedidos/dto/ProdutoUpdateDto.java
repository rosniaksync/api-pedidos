package com.devgabriel.pedidos.dto;

import java.math.BigDecimal;

public record ProdutoUpdateDto(
        String nome,
        String descricao,
        Integer quantidadeEstoque,
        BigDecimal valorUnitario
) {}