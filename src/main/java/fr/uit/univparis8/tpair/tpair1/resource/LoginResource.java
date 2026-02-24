package fr.uit.univparis8.tpair.tpair1.resource;

import fr.uit.univparis8.tpair.tpair1.dto.LoginRequest;
import fr.uit.univparis8.tpair.tpair1.dto.LoginResponse;
import fr.uit.univparis8.tpair.tpair1.dto.ErrorResponse;
import fr.uit.univparis8.tpair.tpair1.model.User;
import fr.uit.univparis8.tpair.tpair1.security.TokenManager;
import fr.uit.univparis8.tpair.tpair1.service.AuthService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Partie III - Exercice 5 : Authentification stateless
 * Endpoint POST /api/login pour authentifier un utilisateur et générer un token
 */
@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginResource {

    private final AuthService authService = new AuthService();

    /**
     * POST /api/login
     * Authentifie un utilisateur avec username/password et retourne un token
     * 
     * Requête :
     * {
     *   "username": "john",
     *   "password": "secret"
     * }
     * 
     * Réponse (200 OK) :
     * {
     *   "token": "uuid-token",
     *   "userId": 1,
     *   "username": "john",
     *   "message": "Authentification réussie"
     * }
     */
    @POST
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

            // Vérifier les credentials en base de données (vraie authentification)
            User user = authService.authenticate(request.username, request.password);
            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new ErrorResponse(401, "Identifiants invalides"))
                        .build();
            }

            // Générer le token stateless
            String token = TokenManager.getInstance().generateToken(user.getId(), user.getUsername());

            // Retourner la réponse
            LoginResponse response = new LoginResponse(
                    token,
                    user.getId(),
                    user.getUsername(),
                    "Authentification réussie"
            );

            return Response.ok(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(500, "Erreur lors de l'authentification"))
                    .build();
        }
    }
}
