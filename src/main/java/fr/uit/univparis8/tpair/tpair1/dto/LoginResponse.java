package fr.uit.univparis8.tpair.tpair1.dto;

/**
 * DTO pour la réponse de login
 */
public class LoginResponse {
    public String token;
    public Long userId;
    public String username;
    public String message;

    public LoginResponse() {}

    public LoginResponse(String token, Long userId, String username, String message) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.message = message;
    }
}
