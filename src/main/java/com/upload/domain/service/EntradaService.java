package com.upload.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import com.upload.domain.exception.ProdutoInativoException;
import com.upload.domain.model.Entrada;
import com.upload.domain.model.Produto;
import com.upload.domain.repository.EntradaRepository;
import com.upload.domain.repository.ProdutoRepository;

@Service
public class EntradaService {
    
    @Autowired
    private EntradaRepository entradaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public Entrada salvar(Entrada entrada) {
        Produto produto = produtoRepository.findByCodigo(entrada.getProduto().getCodigo());
        if (produto != null) {
            validaProdutoAtivo(entrada.getProduto());
        }
        
        entrada = entradaRepository.save(entrada);
        entradaRepository.flush();
        return entrada;
    }

    private void validaProdutoAtivo(Produto produto) {
        if (!produto.isAtivo()) {
            var mensagem = "Este produto não está ativo. Ative antes para poder efetuar sua entrada no sistema.";
            var fieldError = new FieldError(produto.getClass().getName(),
                "ativo", produto.isAtivo(), false, null, null, mensagem);
            
            throw new ProdutoInativoException(mensagem, fieldError);
        }
    }
}
