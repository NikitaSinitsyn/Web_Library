package com.example.https.CustomExceptionHandler;

public class PositionNotFoundException extends RuntimeException{
    public PositionNotFoundException(String message) {
        super(message);
    }
}