package fr.uit.univparis8.tpair.tpair1.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ErrorResponse {
    public int code;
    public int status;
    public String message;
    public String timestamp;
    public List<FieldError> errors;

    public ErrorResponse(int status, String message) {
        this.code = status;
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now().toString();
        this.errors = new ArrayList<>();
    }

    public ErrorResponse(int status, String message, List<FieldError> errors) {
        this.code = status;
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now().toString();
        this.errors = errors;
    }

    
    public static class FieldError {
        public String field;
        public String message;

        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }
    }
}
