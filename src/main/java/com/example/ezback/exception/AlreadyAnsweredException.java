package com.example.ezback.exception;

public class AlreadyAnsweredException extends RuntimeException {
    public AlreadyAnsweredException(String message) {
        super(message);
    }
}
