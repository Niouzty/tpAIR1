package fr.uit.univparis8.tpair.tpair1.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenManagerTest {

    @Test
    void shouldGenerateAndValidateToken() {
        TokenManager manager = TokenManager.getInstance();
        String token = manager.generateToken(42L, "alice");

        assertNotNull(token);

        TokenManager.TokenInfo info = manager.validateToken(token);
        assertNotNull(info);
        assertEquals(42L, info.userId);
        assertEquals("alice", info.username);

        manager.revokeToken(token);
    }

    @Test
    void shouldRevokeToken() {
        TokenManager manager = TokenManager.getInstance();
        String token = manager.generateToken(7L, "bob");

        manager.revokeToken(token);

        assertNull(manager.validateToken(token));
    }
}
