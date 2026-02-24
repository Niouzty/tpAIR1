package fr.uit.univparis8.tpair.tpair1.security;

import fr.uit.univparis8.tpair.tpair1.dto.ErrorResponse;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Partie III - Exercice 6 : Filtre de sécurité JAX-RS
 * Vérifie la présence et la validité du token sur les endpoints protégés
 */
@Provider
public class SecurityFilter implements ContainerRequestFilter {
    
    // Endpoints qui ne nécessitent pas d'authentification
    private static final String[] PUBLIC_PATHS = {
            "login",           // POST /api/login
            "helloWorld",      // GET /api/helloWorld
            "params"           // GET /api/params
    };

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        String method = requestContext.getMethod();
        
        // Vérifier si le chemin fait partie des endpoints publics
        boolean isPublic = false;
        for (String publicPath : PUBLIC_PATHS) {
            if (path.contains(publicPath)) {
                isPublic = true;
                break;
            }
        }

        // Autoriser les endpoints publics
        if (isPublic) {
            return;
        }
        
        // Pour les autres endpoints, vérifier le token
        String authHeader = requestContext.getHeaderString("Authorization");
        
        if (authHeader == null || authHeader.isEmpty()) {
            abortWithUnauthorized(requestContext, "Token absent");
            return;
        }
        
        // Parser le token du header "Bearer <token>"
        String token = extractToken(authHeader);
        if (token == null) {
            abortWithUnauthorized(requestContext, "Format Authorization invalide. Utilisez 'Bearer <token>'");
            return;
        }
        
        // Valider le token
        TokenManager.TokenInfo tokenInfo = TokenManager.getInstance().validateToken(token);
        if (tokenInfo == null) {
            abortWithUnauthorized(requestContext, "Token invalide ou expiré");
            return;
        }
        
        // Attacher les infos utilisateur au contexte
        requestContext.setProperty("userId", tokenInfo.userId);
        requestContext.setProperty("username", tokenInfo.username);
        requestContext.setProperty("userPrincipal", new UserPrincipal(tokenInfo.userId, tokenInfo.username));
    }

    private String extractToken(String authHeader) {
        if (!authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring("Bearer ".length()).trim();
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext, String message) {
        ErrorResponse response = new ErrorResponse(401, message);
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .entity(response)
                        .build()
        );
    }
}
