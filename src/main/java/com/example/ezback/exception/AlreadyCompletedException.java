package com.example.ezback.exception;

public class AlreadyCompletedException extends RuntimeException {
    public AlreadyCompletedException(String message) {
        super(message);
    }
}
