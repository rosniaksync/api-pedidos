package com.devgabriel.pedidos.controller;

import com.devgabriel.pedidos.dto.ClienteRequestDto;
import com.devgabriel.pedidos.dto.ClienteResponseDto;
import com.devgabriel.pedidos.dto.ClienteUpdateDto;
import com.devgabriel.pedidos.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService service;

    @PostMapping
    public ResponseEntity<ClienteResponseDto> cadastrarCliente(@RequestBody @Valid ClienteRequestDto dto, UriComponentsBuilder builder) {
        ClienteResponseDto cliente = service.cadastrar(dto);
        URI uri = builder.path("v1/clientes/{id}")
                .buildAndExpand(cliente.id())
                .toUri();
        return ResponseEntity.created(uri).body(cliente);
    }

    @GetMapping
    public ResponseEntity<Page<ClienteResponseDto>> listarClientes(Pageable pageable) {
        return ResponseEntity.ok(service.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> listarClientePorId(@PathVariable Long id) {
        ClienteResponseDto cliente = service.listarUm(id);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> atualizarCliente(@PathVariable Long id, @RequestBody @Valid ClienteUpdateDto dto) {
        ClienteResponseDto cliente = service.atualizar(id, dto);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}