package com.devgabriel.pedidos.controller;

import com.devgabriel.pedidos.dto.ProdutoRequestDto;
import com.devgabriel.pedidos.dto.ProdutoResponseDto;
import com.devgabriel.pedidos.dto.ProdutoUpdateDto;
import com.devgabriel.pedidos.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/v1/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService service;

    @PostMapping
    public ResponseEntity<ProdutoResponseDto> cadastrar(@RequestBody @Valid ProdutoRequestDto dto, UriComponentsBuilder uriBuilder) {
        ProdutoResponseDto produto = service.cadastrar(dto);
        URI uri = uriBuilder.path("/v1/produtos/{id}")
                .buildAndExpand(produto.id()).toUri();
        return ResponseEntity.created(uri).body(produto);
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoResponseDto>> listarProdutos(Pageable pageable) {
        return ResponseEntity.ok(service.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> listarProdutoPorId(@PathVariable Long id) {
        ProdutoResponseDto produto = service.listarUm(id);
        return ResponseEntity.ok(produto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDto> atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoUpdateDto dto) {
        ProdutoResponseDto produto = service.atualizar(id, dto);
        return ResponseEntity.ok(produto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}