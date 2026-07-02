package com.devgabriel.pedidos.dto;

import com.devgabriel.pedidos.domain.ProdutoEntity;

import java.math.BigDecimal;

public record ProdutoResponseDto(
        Long id,
        String nome,
        String descricao,
        Integer quantidadeEstoque,
        BigDecimal valorUnitario) {

    public ProdutoResponseDto(ProdutoEntity produto) {
        this(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getQuantidadeEstoque(),
                produto.getValorUnitario());
    }
}