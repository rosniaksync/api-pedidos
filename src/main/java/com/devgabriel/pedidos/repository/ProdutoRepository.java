package com.devgabriel.pedidos.repository;

import com.devgabriel.pedidos.domain.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {

    boolean existsByNome(String nome);
}
