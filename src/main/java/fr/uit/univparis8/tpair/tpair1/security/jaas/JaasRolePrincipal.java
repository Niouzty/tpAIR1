package fr.uit.univparis8.tpair.tpair1.security.jaas;

import java.security.Principal;

/**
 * Bonus Exercice 5 : JAAS RolePrincipal
 * Représente un rôle d'utilisateur via JAAS
 */
public class JaasRolePrincipal implements Principal {
    
    private final String role;

    public JaasRolePrincipal(String role) {
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
        return "JaasRolePrincipal{" +
                "role='" + role + '\'' +
                '}';
    }
}
