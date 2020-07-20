package com.nab.microservices.core.phone.exceptions;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException() {
    }

    public InvalidInputException(String message) {
        super(message);
    }

}
