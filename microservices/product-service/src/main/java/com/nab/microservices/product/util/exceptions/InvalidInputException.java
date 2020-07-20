package com.nab.microservices.product.util.exceptions;

public class InvalidInputException extends RuntimeException {
    public InvalidInputException() {
    }

    public InvalidInputException(String message) {
        super(message);
    }

}
