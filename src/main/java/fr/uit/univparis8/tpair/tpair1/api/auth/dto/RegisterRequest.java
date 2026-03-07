package fr.uit.univparis8.tpair.tpair1.api.auth.dto;

public class RegisterRequest {
    @jakarta.validation.constraints.NotBlank
    private String username;
    @jakarta.validation.constraints.Email
    @jakarta.validation.constraints.NotBlank
    private String email;
    @jakarta.validation.constraints.NotBlank
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
