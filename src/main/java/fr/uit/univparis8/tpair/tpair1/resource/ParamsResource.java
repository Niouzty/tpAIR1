package fr.uit.univparis8.tpair.tpair1.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/params")
@Produces(MediaType.APPLICATION_JSON)
public class ParamsResource {

    
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

    
    @GET
    @Path("/{nom}/{prenom}")
    public Response paramsPath(@PathParam("nom") String nom, 
                               @PathParam("prenom") String prenom) {
        return Response.ok(new Message("Bonjour " + prenom + " " + nom)).build();
    }

    
    public static class Message {
        public String message;

        public Message(String message) {
            this.message = message;
        }
    }

    
    public static class ErrorMessage {
        public String error;

        public ErrorMessage(String error) {
            this.error = error;
        }
    }
}
