package fr.uit.univparis8.tpair.tpair1.service;

import java.util.Map;

public class ValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        super("Erreur de validation");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}