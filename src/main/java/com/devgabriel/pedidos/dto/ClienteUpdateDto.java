package com.devgabriel.pedidos.dto;

import java.time.LocalDate;

public record ClienteUpdateDto(
        String nome,
        String email
) {}
