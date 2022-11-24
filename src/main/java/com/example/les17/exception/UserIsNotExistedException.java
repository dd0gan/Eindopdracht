package com.example.les17.exception;

public class UserIsNotExistedException extends RuntimeException {

    public UserIsNotExistedException(String message) {
        super(message);
    }
}
