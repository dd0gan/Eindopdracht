package com.example.les17.exception;

public class UserExistedException extends RuntimeException {

    public UserExistedException(String message) {
        super(message);
    }
}
