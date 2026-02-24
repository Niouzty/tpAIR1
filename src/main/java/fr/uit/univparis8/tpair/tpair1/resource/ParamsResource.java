package fr.uit.univparis8.tpair.tpair1.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Exercice 1 : Ressource de test pour les paramètres JAX-RS
 * Endpoints : /api/params avec QueryParams et PathParams
 */
@Path("/params")
@Produces(MediaType.APPLICATION_JSON)
public class ParamsResource {

    /**
     * Endpoint avec QueryParams : GET /api/params?nom=Dupont&prenom=Jean
     */
    @GET
    public Response paramsQuery(@QueryParam("nom") String nom, 
                                @QueryParam("prenom") String prenom) {
        if (nom == null || prenom == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorMessage("Les paramètres 'nom' et 'prenom' sont obligatoires"))
                    .build();
        }
        return Response.ok(new Message("Bonjour " + prenom + " " + nom)).build();
    }

    /**
     * Endpoint avec PathParams : GET /api/params/{nom}/{prenom}
     */
    @GET
    @Path("/{nom}/{prenom}")
    public Response paramsPath(@PathParam("nom") String nom, 
                               @PathParam("prenom") String prenom) {
        return Response.ok(new Message("Bonjour " + prenom + " " + nom)).build();
    }

    /**
     * DTO simple pour les réponses
     */
    public static class Message {
        public String message;

        public Message(String message) {
            this.message = message;
        }
    }

    /**
     * DTO pour les erreurs
     */
    public static class ErrorMessage {
        public String error;

        public ErrorMessage(String error) {
            this.error = error;
        }
    }
}
