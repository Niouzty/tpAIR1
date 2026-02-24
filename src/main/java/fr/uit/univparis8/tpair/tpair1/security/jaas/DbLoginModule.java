package fr.uit.univparis8.tpair.tpair1.security.jaas;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.util.*;

/**
 * Bonus Exercice 5 : JAAS DbLoginModule
 * Authentifie les utilisateurs en vérifiant username/password en base de données
 */
public class DbLoginModule implements LoginModule {

    private Subject subject;
    private CallbackHandler callbackHandler;
    private Map<String, ?> sharedState;
    private Map<String, ?> options;
    
    private boolean debug = false;
    private boolean success = false;
    private String username;
    private Long userId;
    private Set<String> roles;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler,
                          Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.options = options;
        
        // Récupérer l'option debug si présente
        String debugOption = (String) options.get("debug");
        this.debug = "true".equalsIgnoreCase(debugOption);
        
        if (debug) {
            System.out.println("[DbLoginModule] Initialisé");
        }
    }

    @Override
    public boolean login() throws LoginException {
        // Récupérer les callbacks (username et password)
        Callback[] callbacks = new Callback[2];
        callbacks[0] = new NameCallback("Username: ");
        callbacks[1] = new PasswordCallback("Password: ", false);
        
        try {
            callbackHandler.handle(callbacks);
        } catch (IOException | UnsupportedCallbackException e) {
            throw new LoginException("Erreur lors de la récupération des credentials: " + e.getMessage());
        }
        
        username = ((NameCallback) callbacks[0]).getName();
        char[] passwordChars = ((PasswordCallback) callbacks[1]).getPassword();
        String password = new String(passwordChars != null ? passwordChars : new char[0]);
        
        if (debug) {
            System.out.println("[DbLoginModule] Tentative login pour user: " + username);
        }
        
        // TODO: Vérifier en base de données
        // Pour la démo, accepter admin/admin123
        if ("admin".equals(username) && "admin123".equals(password)) {
            this.userId = 1L;
            this.roles = new HashSet<>();
            this.roles.add("ROLE_ADMIN");
            this.roles.add("ROLE_USER");
            this.success = true;
            
            if (debug) {
                System.out.println("[DbLoginModule] Authentication réussie pour: " + username);
            }
            return true;
        } else if ("user".equals(username) && "user123".equals(password)) {
            this.userId = 2L;
            this.roles = new HashSet<>();
            this.roles.add("ROLE_USER");
            this.success = true;
            
            if (debug) {
                System.out.println("[DbLoginModule] Authentication réussie pour: " + username);
            }
            return true;
        }
        
        throw new LoginException("Authentication échouée pour: " + username);
    }

    @Override
    public boolean commit() throws LoginException {
        if (!success) {
            return false;
        }
        
        // Ajouter les Principals au Subject
        subject.getPrincipals().add(new JaasUserPrincipal(userId, username));
        
        // Ajouter les rôles
        for (String role : roles) {
            subject.getPrincipals().add(new JaasRolePrincipal(role));
        }
        
        if (debug) {
            System.out.println("[DbLoginModule] Commit: Principals ajoutés au Subject");
        }
        
        return true;
    }

    @Override
    public boolean abort() throws LoginException {
        if (!success) {
            return false;
        }
        
        logout();
        return true;
    }

    @Override
    public boolean logout() throws LoginException {
        subject.getPrincipals().clear();
        this.success = false;
        
        if (debug) {
            System.out.println("[DbLoginModule] Logout effectué");
        }
        
        return true;
    }
}
