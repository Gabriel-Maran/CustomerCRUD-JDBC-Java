package com.empresa.exception;

public class EmailAlreadyPresent extends RuntimeException {
    public EmailAlreadyPresent(String message) {
        super(message);
    }

    public EmailAlreadyPresent(String message, Throwable cause) {
        super(message, cause);
    }
}
