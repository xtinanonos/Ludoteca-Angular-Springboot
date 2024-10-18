package com.ccsw.tutorial.loan;
// Clase para enviar excepciones personalizadas

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
