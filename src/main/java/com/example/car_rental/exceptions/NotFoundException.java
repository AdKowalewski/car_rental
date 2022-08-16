package com.example.car_rental.exceptions;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) { super(message); }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
