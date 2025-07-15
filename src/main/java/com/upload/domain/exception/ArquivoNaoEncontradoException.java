package com.upload.domain.exception;

import java.nio.file.NoSuchFileException;

public class ArquivoNaoEncontradoException extends NoSuchFileException {
    public ArquivoNaoEncontradoException(String message) {
        super(message);
    }
}
