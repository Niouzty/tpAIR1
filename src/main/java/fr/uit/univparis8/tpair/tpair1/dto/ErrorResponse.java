package fr.uit.univparis8.tpair.tpair1.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Réponse d'erreur normalisée pour l'API REST
 * Partie II - Gestion centralisée des erreurs
 */
public class ErrorResponse {
    
    public int status;
    public String message;
    public String timestamp;
    public List<FieldError> errors;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now().toString();
        this.errors = new ArrayList<>();
    }

    public ErrorResponse(int status, String message, List<FieldError> errors) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now().toString();
        this.errors = errors;
    }

    /**
     * Erreur sur un champ spécifique
     */
    public static class FieldError {
        public String field;
        public String message;

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }
}
