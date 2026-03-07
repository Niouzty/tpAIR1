package fr.uit.univparis8.tpair.tpair1.api.common.dto;

import fr.uit.univparis8.tpair.tpair1.enums.Role;

public class UserDto {
    private Long id;
    private String username;
    private String email;
    private Role role;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
