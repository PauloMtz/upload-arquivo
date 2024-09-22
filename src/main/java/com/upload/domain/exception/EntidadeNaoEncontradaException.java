package com.upload.domain.exception;

import jakarta.persistence.EntityNotFoundException;

public class EntidadeNaoEncontradaException extends EntityNotFoundException {
    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
