package fr.uit.univparis8.tpair.tpair1.security;

import fr.uit.univparis8.tpair.tpair1.dto.ErrorResponse;
import fr.uit.univparis8.tpair.tpair1.security.jaas.JaasConfigSupport;
import fr.uit.univparis8.tpair.tpair1.security.jaas.JaasUserPrincipal;
import fr.uit.univparis8.tpair.tpair1.security.jaas.TokenCallbackHandler;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;


@Provider
public class SecurityFilter implements ContainerRequestFilter {
    private static final String[] PUBLIC_PATHS = {
            "login",
            "auth/login",
            "params"
    };

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String path = requestContext.getUriInfo().getPath();
        boolean isPublic = isPublicPath(path);
        if (isPublic) {
            return;
        }
        String authHeader = requestContext.getHeaderString("Authorization");
        
        if (authHeader == null || authHeader.isEmpty()) {
            abortWithUnauthorized(requestContext, "Token absent");
            return;
        }
        String token = extractToken(authHeader);
        if (token == null) {
            abortWithUnauthorized(requestContext, "Format Authorization invalide. Utilisez 'Bearer <token>'");
            return;
        }
        
        JaasConfigSupport.ensureConfigured();

        Subject subject;
        try {
            LoginContext loginContext = new LoginContext(
                    "MasterAnnonceToken",
                    new TokenCallbackHandler(token)
            );
            loginContext.login();
            subject = loginContext.getSubject();
        } catch (LoginException e) {
            abortWithUnauthorized(requestContext, "Token invalide ou expiré");
            return;
        }

        JaasUserPrincipal jaasUser = subject.getPrincipals(JaasUserPrincipal.class)
                .stream()
                .findFirst()
                .orElse(null);
        if (jaasUser == null) {
            abortWithUnauthorized(requestContext, "Token invalide ou expiré");
            return;
        }

        requestContext.setProperty("userId", jaasUser.getUserId());
        requestContext.setProperty("username", jaasUser.getName());
        requestContext.setProperty("userPrincipal", new UserPrincipal(jaasUser.getUserId(), jaasUser.getName()));
    }

    private boolean isPublicPath(String path) {
        if (path == null) {
            return false;
        }
        for (String publicPath : PUBLIC_PATHS) {
            if (path.equals(publicPath) || path.startsWith(publicPath + "/")) {
                return true;
            }
        }
        return false;
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
