package com.example.ezback.exception;

public class MissionNotTodayException extends RuntimeException {
    public MissionNotTodayException(String message) {
        super(message);
    }
}
