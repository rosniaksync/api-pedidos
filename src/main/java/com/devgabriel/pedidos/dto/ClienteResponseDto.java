package com.devgabriel.pedidos.dto;

import com.devgabriel.pedidos.domain.ClienteEntity;

import java.time.LocalDate;

public record ClienteResponseDto(
        Long id,
        String nome,
        String email,
        LocalDate dataCadastro) {

    public ClienteResponseDto(ClienteEntity cliente) {
        this(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getDataCadastro());
    }
}