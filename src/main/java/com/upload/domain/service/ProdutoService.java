package com.upload.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.upload.domain.model.Produto;
import com.upload.domain.repository.ProdutoRepository;
import com.upload.exception.ProdutoNaoEncontradoException;

@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UploadService uploadService;

    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto buscarPorId(Long id) {
        var produtoEncontrado = produtoRepository.findById(id);

        if (produtoEncontrado.isPresent()) {
            return produtoEncontrado.get();
        }

        throw new ProdutoNaoEncontradoException("Produto não encontrado.");
    }

    public void excluir(Long id) {
        var produtoEncontrado = buscarPorId(id);
        produtoRepository.delete(produtoEncontrado);
        // atualiza JPA
        produtoRepository.flush();
        // remove foto na pasta
        uploadService.remover(produtoEncontrado.getFoto());
    }
}
