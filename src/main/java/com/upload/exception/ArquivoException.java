package com.upload.exception;

public class ArquivoException extends RuntimeException {
    
    public ArquivoException(String message) {
        super(message);
    }

    public ArquivoException(String message, Throwable cause) {
        super(message, cause);
    }
}
