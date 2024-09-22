package com.upload.domain.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class EntidadeNaoRemoverException extends DataIntegrityViolationException {
    public EntidadeNaoRemoverException(String mensagem) {
        super(mensagem);
    }
}
