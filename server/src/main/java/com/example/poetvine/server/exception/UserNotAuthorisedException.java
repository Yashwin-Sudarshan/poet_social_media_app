package com.example.poetvine.server.exception;

public class UserNotAuthorisedException extends RuntimeException {

    public UserNotAuthorisedException() {
        super();
    }

    public UserNotAuthorisedException(String message) {
        super(message);
    }

    public UserNotAuthorisedException(String message, Throwable cause) {
        super(message, cause);
    }
}
