package fr.uit.univparis8.tpair.tpair1.security;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Partie III - Exercice 5 : Authentification stateless
 * Gestionnaire de tokens en mémoire
 * Note : En production, utiliser un vrai système de tokens (JWT, Redis, etc.)
 */
public class TokenManager {
    
    private static final TokenManager INSTANCE = new TokenManager();
    private static final long TOKEN_EXPIRATION_TIME = 3600000; // 1 heure en millisecondes
    
    private final Map<String, TokenInfo> tokens = new HashMap<>();

    private TokenManager() {}

    public static TokenManager getInstance() {
        return INSTANCE;
    }

    /**
     * Génère un nouveau token pour un utilisateur
     */
    public String generateToken(Long userId, String username) {
        String token = UUID.randomUUID().toString();
        TokenInfo info = new TokenInfo(userId, username, System.currentTimeMillis() + TOKEN_EXPIRATION_TIME);
        tokens.put(token, info);
        return token;
    }

    /**
     * Valide un token et retourne les infos utilisateur
     */
    public TokenInfo validateToken(String token) {
        TokenInfo info = tokens.get(token);
        
        if (info == null) {
            return null; // Token inexistant
        }
        
        if (info.isExpired()) {
            tokens.remove(token);
            return null; // Token expiré
        }
        
        return info;
    }

    /**
     * Révoque un token (logout)
     */
    public void revokeToken(String token) {
        tokens.remove(token);
    }

    /**
     * Infos stockées dans un token
     */
    public static class TokenInfo {
        public final Long userId;
        public final String username;
        public final long expirationTime;

        public TokenInfo(Long userId, String username, long expirationTime) {
            this.userId = userId;
            this.username = username;
            this.expirationTime = expirationTime;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }
}
