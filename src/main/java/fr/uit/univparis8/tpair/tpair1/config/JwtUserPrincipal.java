package fr.uit.univparis8.tpair.tpair1.config;

import java.security.Principal;

public class JwtUserPrincipal implements Principal {
    private final Long userId;
    private final String username;
    private final String role;

    public JwtUserPrincipal(Long userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }

    public Long getUserId() { return userId; }
    public String getRole() { return role; }

    @Override
    public String getName() {
        return username;
    }
}
