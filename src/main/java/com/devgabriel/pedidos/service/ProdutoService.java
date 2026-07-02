package com.devgabriel.pedidos.service;

import com.devgabriel.pedidos.domain.ProdutoEntity;
import com.devgabriel.pedidos.dto.ProdutoRequestDto;
import com.devgabriel.pedidos.dto.ProdutoResponseDto;
import com.devgabriel.pedidos.dto.ProdutoUpdateDto;
import com.devgabriel.pedidos.exception.BusinessException;
import com.devgabriel.pedidos.exception.ResourceNotFoundException;
import com.devgabriel.pedidos.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoResponseDto cadastrar(ProdutoRequestDto dto) {
        if(repository.existsByNome(dto.nome())) {
            throw new BusinessException("Nome do produto já cadastrado");
        }
        ProdutoEntity produto = ProdutoEntity
                .builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .quantidadeEstoque(dto.quantidadeEstoque())
                .valorUnitario(dto.valorUnitario())
                .build();

        ProdutoEntity produtoSalvo = repository.save(produto);

        return new ProdutoResponseDto(produtoSalvo);
    }

    public Page<ProdutoResponseDto> listarTodos(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ProdutoResponseDto::new);
    }

    public ProdutoResponseDto listarUm(Long id) {
        ProdutoEntity produto = buscarProdutoPorId(id);
        return new ProdutoResponseDto(produto);
    }

    @Transactional
    public ProdutoResponseDto atualizar(Long id, ProdutoUpdateDto dto) {
        ProdutoEntity produto = buscarProdutoPorId(id);
        if(dto.nome() != null) {
            produto.setNome(dto.nome());
        }
        if(dto.descricao() != null) {
            produto.setDescricao(dto.descricao());
        }
        if(dto.quantidadeEstoque() != null) {
            produto.setQuantidadeEstoque(dto.quantidadeEstoque());
        }
        if(dto.valorUnitario() != null) {
            produto.setValorUnitario(dto.valorUnitario());
        }
        return new ProdutoResponseDto(produto);
    }

    public void deletar(Long id) {
        buscarProdutoPorId(id);
        repository.deleteById(id);
    }

    private ProdutoEntity buscarProdutoPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
    }
}