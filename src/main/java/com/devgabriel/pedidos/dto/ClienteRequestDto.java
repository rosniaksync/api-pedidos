package com.devgabriel.pedidos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ClienteRequestDto(

        @NotNull(message = "O nome não pode ser nulo")
        String nome,

        @Email(message = "É necessario ter credenciais reais de um email")
        @NotNull(message = "O email não pode ser nulo")
        String email
){}