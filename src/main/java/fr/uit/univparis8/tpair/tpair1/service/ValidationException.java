package fr.uit.univparis8.tpair.tpair1.service;

import java.util.Map;

public class ValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public ValidationException(Map<String, String> errors) {
        super("Erreur de validation");
        this.errors = errors;
    }

<<<<<<< HEAD
=======
    public ValidationException(String message) {
        super(message);
        this.errors = null;
    }

>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
    public Map<String, String> getErrors() {
        return errors;
    }
}