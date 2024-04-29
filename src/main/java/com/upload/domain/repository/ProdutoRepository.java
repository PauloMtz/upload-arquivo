package com.upload.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upload.domain.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
