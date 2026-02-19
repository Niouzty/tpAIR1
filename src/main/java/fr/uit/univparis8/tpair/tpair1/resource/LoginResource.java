package fr.uit.univparis8.tpair.tpair1.resource;

import fr.uit.univparis8.tpair.tpair1.dto.LoginRequest;
import fr.uit.univparis8.tpair.tpair1.dto.LoginResponse;
import fr.uit.univparis8.tpair.tpair1.dto.ErrorResponse;
import fr.uit.univparis8.tpair.tpair1.security.TokenManager;
import fr.uit.univparis8.tpair.tpair1.security.jaas.JaasConfigSupport;
import fr.uit.univparis8.tpair.tpair1.security.jaas.JaasUserPrincipal;
import fr.uit.univparis8.tpair.tpair1.security.jaas.UsernamePasswordCallbackHandler;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;


@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginResource {

    
    @POST
    public Response login(LoginRequest request) {
        try {
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

            JaasConfigSupport.ensureConfigured();
            LoginContext loginContext = new LoginContext(
                    "MasterAnnonceLogin",
                    new UsernamePasswordCallbackHandler(request.username, request.password)
            );

            try {
                loginContext.login();
            } catch (LoginException e) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new ErrorResponse(401, "Identifiants invalides"))
                        .build();
            }

            Subject subject = loginContext.getSubject();
            JaasUserPrincipal principal = subject.getPrincipals(JaasUserPrincipal.class)
                    .stream()
                    .findFirst()
                    .orElse(null);
            if (principal == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new ErrorResponse(401, "Authentification JAAS invalide"))
                        .build();
            }
            String token = TokenManager.getInstance().generateToken(principal.getUserId(), principal.getName());
            LoginResponse response = new LoginResponse(
                    token,
                    principal.getUserId(),
                    principal.getName(),
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
