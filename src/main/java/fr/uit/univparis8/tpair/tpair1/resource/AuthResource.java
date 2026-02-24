package fr.uit.univparis8.tpair.tpair1.resource;

import fr.uit.univparis8.tpair.tpair1.dto.LoginRequest;
import fr.uit.univparis8.tpair.tpair1.dto.LoginResponse;
import fr.uit.univparis8.tpair.tpair1.dto.ErrorResponse;
import fr.uit.univparis8.tpair.tpair1.security.TokenManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Partie III - Exercice 5 : Authentification stateless
 * Endpoint pour le login et la génération de tokens
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    /**
     * POST /api/auth/login
     * Authentifie un utilisateur et retourne un token
     */
    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        try {
            // Validation des paramètres
            if (request == null || request.username == null || request.password == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse(400, "Username et password sont obligatoires"))
                        .build();
            }

            if (request.username.trim().isEmpty() || request.password.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse(400, "Username et password ne peuvent pas être vides"))
                        .build();
            }

            // TODO: Vérifier les credentials en base de données
            // Pour l'instant, accepter tous les logins (à remplacer par une vraie authentification)
            Long userId = generateUserId(request.username);
            
            // Générer le token
            String token = TokenManager.getInstance().generateToken(userId, request.username);

            // Retourner la réponse
            LoginResponse response = new LoginResponse(
                    token,
                    userId,
                    request.username,
                    "Authentification réussie"
            );

            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(500, "Erreur lors de l'authentification: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * POST /api/auth/logout
     * Révoque le token de l'utilisateur courant
     */
    @POST
    @Path("/logout")
    public Response logout(@HeaderParam("Authorization") String authHeader) {
        try {
            if (authHeader == null || authHeader.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse(400, "Token absent"))
                        .build();
            }

            String token = extractToken(authHeader);
            if (token != null) {
                TokenManager.getInstance().revokeToken(token);
            }

            return Response.ok(new LogoutResponse("Déconnexion réussie")).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(500, "Erreur lors de la déconnexion"))
                    .build();
        }
    }

    private String extractToken(String authHeader) {
        if (!authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring("Bearer ".length()).trim();
    }

    /**
     * Génère un ID utilisateur basé sur le username (pour la démo)
     * En production, cet ID viendrait de la base de données
     */
    private Long generateUserId(String username) {
        return (long) username.hashCode();
    }

    /**
     * DTO pour la réponse de logout
     */
    public static class LogoutResponse {
        public String message;

        public LogoutResponse(String message) {
            this.message = message;
        }
    }
}
