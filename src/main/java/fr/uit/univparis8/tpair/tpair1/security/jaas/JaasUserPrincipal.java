package fr.uit.univparis8.tpair.tpair1.security.jaas;

import java.security.Principal;


public class JaasUserPrincipal implements Principal {
    
    private final Long userId;
    private final String username;

    public JaasUserPrincipal(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    @Override
    public String getName() {
        return username;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "JaasUserPrincipal{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                '}';
    }
}
