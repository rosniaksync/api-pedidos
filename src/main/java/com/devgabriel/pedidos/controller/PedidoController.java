package com.devgabriel.pedidos.controller;

import com.devgabriel.pedidos.dto.PedidoStatusDto;
import com.devgabriel.pedidos.dto.PedidoRequestDto;
import com.devgabriel.pedidos.dto.PedidoResponseDto;
import com.devgabriel.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;

    @PostMapping
    public ResponseEntity<PedidoResponseDto> cadastrarPedido(@RequestBody @Valid PedidoRequestDto dto, UriComponentsBuilder uriBuilder) {
        PedidoResponseDto pedido = service.cadastrar(dto);
        URI uri = uriBuilder.path("/v1/pedidos/{id}")
                .buildAndExpand(pedido.id()).toUri();
        return ResponseEntity.created(uri).body(pedido);
    }

    @GetMapping
    public ResponseEntity<Page<PedidoResponseDto>> listarPedidos(Pageable pageable) {
        Page<PedidoResponseDto> pedidos = service.listar(pageable);
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDto> listarPorId(@PathVariable Long id) {
        PedidoResponseDto pedido = service.listarPorId(id);
        return ResponseEntity.ok(pedido);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDto> atualizarStatus(@PathVariable Long id, @RequestBody @Valid PedidoStatusDto dto) {
        PedidoResponseDto pedido = service.atualizarStatus(id, dto);
        return ResponseEntity.ok(pedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long id) {
        service.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}