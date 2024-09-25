package com.bbc.app.exception;

public class SomethingWrongException extends RuntimeException {
    public SomethingWrongException(String message) {
        super(message);
    }
}
