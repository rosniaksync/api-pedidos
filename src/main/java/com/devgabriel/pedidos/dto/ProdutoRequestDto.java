package com.devgabriel.pedidos.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProdutoRequestDto(

        @NotBlank
        String nome,

        String descricao,

        Integer quantidadeEstoque,

        @NotNull
        @DecimalMin("0.01")
        BigDecimal valorUnitario) {}