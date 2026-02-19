package fr.uit.univparis8.tpair.tpair1.security;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class TokenManager {
    
    private static final TokenManager INSTANCE = new TokenManager();
    private static final long TOKEN_EXPIRATION_TIME = 3600000;
    
    private final Map<String, TokenInfo> tokens = new ConcurrentHashMap<>();

    private TokenManager() {}

    public static TokenManager getInstance() {
        return INSTANCE;
    }

    
    public String generateToken(Long userId, String username) {
        String token = UUID.randomUUID().toString();
        TokenInfo info = new TokenInfo(userId, username, System.currentTimeMillis() + TOKEN_EXPIRATION_TIME);
        tokens.put(token, info);
        return token;
    }

    
    public TokenInfo validateToken(String token) {
        TokenInfo info = tokens.get(token);
        
        if (info == null) {
            return null;
        }
        
        if (info.isExpired()) {
            tokens.remove(token);
            return null;
        }
        
        return info;
    }

    
    public void revokeToken(String token) {
        tokens.remove(token);
    }

    
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
