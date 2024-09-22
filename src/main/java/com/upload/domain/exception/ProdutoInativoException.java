package com.upload.domain.exception;

import org.springframework.validation.FieldError;

public class ProdutoInativoException extends ValidacaoException {
    public ProdutoInativoException(String message, FieldError fieldError) {
        super(message, fieldError);
    }
}
