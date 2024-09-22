package com.upload.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upload.domain.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Produto findByNome(String nome);
    Produto findByCodigo(String codigo);

    default Boolean produtoAtivo(Produto produto) {
        if (!produto.isAtivo()) {
            return false;
        }

        return true;
    }
}
