package fr.uit.univparis8.tpair.tpair1.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Exercice 1 : Ressource de test pour JAX-RS
 * Endpoint : /api/helloWorld
 */
@Path("/helloWorld")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {

    /**
     * Endpoint simple : GET /api/helloWorld
     */
    @GET
    public Response hello() {
        return Response.ok(new Message("Hello World!")).build();
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
}
