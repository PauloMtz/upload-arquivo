package com.upload.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import com.upload.domain.exception.ArquivoNaoEncontradoException;
import com.upload.domain.exception.EntidadeNaoEncontradaException;
import com.upload.domain.exception.EntidadeNaoRemoverException;
import com.upload.domain.exception.RegistroJaCadastradoException;
import com.upload.domain.model.Produto;
import com.upload.domain.repository.ProdutoRepository;

@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public Produto salvar(Produto produto) {
        if (produto.getId() == null) {
            validaCodigoUnico(produto);
        }
        return produtoRepository.save(produto);
    }

    public Produto buscar(Long produtoId) {
        return produtoRepository.findById(produtoId)
            .orElseThrow(() -> new EntidadeNaoEncontradaException(
                String.format("Não existe produto com ID %d", produtoId)));
    }

    @Transactional
    public void excluir(Long produtoId) {
        try {
            Produto produto = buscar(produtoId);
            produtoRepository.delete(produto);
        } catch (Exception e) {
            throw new EntidadeNaoRemoverException(e.getMessage());
        }
    }

    public Produto buscarProduto(String codigo) throws ArquivoNaoEncontradoException {
        Produto produto = produtoRepository.findByCodigo(codigo);
        if (produto != null) {
            return produto;
        } else {
            throw new ArquivoNaoEncontradoException(String.format(
                "Não existe produto com código %s", codigo));
        }
    }

    // método sugerido pelo chatGPT
    @Transactional
    public void baixarSaldo(String codigo, Integer valor) throws ArquivoNaoEncontradoException {
        Produto produto = buscarProduto(codigo);

        if (produto.getSaldo() < valor) {
            throw new IllegalArgumentException(String.format(
                "Estoque insuficiente para o produto %s. Saldo atual: %d, solicitado: %d",
                codigo, produto.getDescricao(), valor));
        }
        produto.baixaSaldo(valor);
    }

    public Produto buscarPorNome(String nome) {
        return produtoRepository.findByNome(nome);
    }

    private void validaCodigoUnico(Produto produto) {
        if (produtoRepository.findByCodigo(produto.getCodigo()) != null) {
            var mensagem = String.format("O código %s já está cadastrado", produto.getCodigo());
            var fieldError = new FieldError(produto.getClass().getName(),
                "codigo", produto.getCodigo(), false, null, null, mensagem);
            
            throw new RegistroJaCadastradoException(mensagem, fieldError);
        }
    }
}
