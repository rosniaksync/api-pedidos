package com.devgabriel.pedidos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PedidoRequestDto(

        @NotNull(message = "O cliente é obrigatório")
        Long clienteId,

        @NotEmpty(message = "O pedido deve conter ao menos um item")
        @Valid
        List<ItemPedidoRequestDto> itens
) {}