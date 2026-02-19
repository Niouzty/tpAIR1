package fr.uit.univparis8.tpair.tpair1.security.jaas;

import fr.uit.univparis8.tpair.tpair1.security.TokenManager;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.util.*;


public class TokenLoginModule implements LoginModule {

    private Subject subject;
    private CallbackHandler callbackHandler;
    private Map<String, ?> sharedState;
    private Map<String, ?> options;
    
    private boolean debug = false;
    private boolean success = false;
    private Long userId;
    private String username;
    private Set<String> roles;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler,
                          Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.options = options;
        
        String debugOption = (String) options.get("debug");
        this.debug = "true".equalsIgnoreCase(debugOption);
        
        if (debug) {
            System.out.println("[TokenLoginModule] Initialisé");
        }
    }

    @Override
    public boolean login() throws LoginException {
        Callback[] callbacks = new Callback[1];
        callbacks[0] = new NameCallback("Token: ");
        
        try {
            callbackHandler.handle(callbacks);
        } catch (IOException | UnsupportedCallbackException e) {
            throw new LoginException("Erreur lors de la récupération du token: " + e.getMessage());
        }
        
        String token = ((NameCallback) callbacks[0]).getName();
        
        if (debug) {
            System.out.println("[TokenLoginModule] Tentative validation token: " + 
                             (token != null ? token.substring(0, Math.min(8, token.length())) + "..." : "null"));
        }
        TokenManager.TokenInfo tokenInfo = TokenManager.getInstance().validateToken(token);
        
        if (tokenInfo == null) {
            throw new LoginException("Token invalide ou expiré");
        }
        
        this.userId = tokenInfo.userId;
        this.username = tokenInfo.username;
        this.roles = new HashSet<>();
        if ("admin".equals(username)) {
            this.roles.add("ROLE_ADMIN");
            this.roles.add("ROLE_USER");
        } else {
            this.roles.add("ROLE_USER");
        }
        
        this.success = true;
        
        if (debug) {
            System.out.println("[TokenLoginModule] Token valide pour: " + username);
        }
        
        return true;
    }

    @Override
    public boolean commit() throws LoginException {
        if (!success) {
            return false;
        }
        subject.getPrincipals().add(new JaasUserPrincipal(userId, username));
        for (String role : roles) {
            subject.getPrincipals().add(new JaasRolePrincipal(role));
        }
        
        if (debug) {
            System.out.println("[TokenLoginModule] Commit: Principals ajoutés au Subject");
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
            System.out.println("[TokenLoginModule] Logout effectué");
        }
        
        return true;
    }
}
