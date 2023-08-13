package com.example.poetvine.server.exception;

public class PoemAlreadyExistsException extends RuntimeException {

    public PoemAlreadyExistsException() {
        super();
    }

    public PoemAlreadyExistsException(String message) {
        super(message);
    }

    public PoemAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
