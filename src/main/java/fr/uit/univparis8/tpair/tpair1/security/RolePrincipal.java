package fr.uit.univparis8.tpair.tpair1.security;

import java.security.Principal;

/**
 * Partie III - Exercice 5 : Authentification stateless
 * Principal représentant un rôle d'utilisateur
 */
public class RolePrincipal implements Principal {
    
    private final String role;

    public RolePrincipal(String role) {
        this.role = role;
    }

    @Override
    public String getName() {
        return role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "RolePrincipal{" +
                "role='" + role + '\'' +
                '}';
    }
}
