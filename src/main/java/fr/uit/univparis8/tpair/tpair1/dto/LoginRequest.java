package fr.uit.univparis8.tpair.tpair1.dto;

/**
 * DTO pour la requête de login
 */
public class LoginRequest {
    public String username;
    public String password;

    public LoginRequest() {}

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
