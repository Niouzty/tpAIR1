package fr.uit.univparis8.tpair.tpair1.api.auth.dto;

public class LoginResponse {
    private String token;
    public LoginResponse() {}
    public LoginResponse(String token) { this.token = token; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
