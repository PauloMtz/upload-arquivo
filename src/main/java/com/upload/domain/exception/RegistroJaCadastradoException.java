package com.upload.domain.exception;

import org.springframework.validation.FieldError;

public class RegistroJaCadastradoException extends ValidacaoException {
    public RegistroJaCadastradoException(String message, FieldError fieldError) {
        super(message, fieldError);
    }
}
