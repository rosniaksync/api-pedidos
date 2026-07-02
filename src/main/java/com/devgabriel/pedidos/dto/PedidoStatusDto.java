package com.devgabriel.pedidos.dto;

import com.devgabriel.pedidos.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record PedidoStatusDto(
        @NotNull(message = "O status é obrigatório")
        StatusPedido status
) {}