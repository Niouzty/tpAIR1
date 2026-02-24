package fr.uit.univparis8.tpair.tpair1.exception;

import fr.uit.univparis8.tpair.tpair1.dto.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Partie II - Exercice 4 : Gestion des erreurs REST
 * Mapper pour les exceptions non gérées (erreurs 500)
 */
@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        // Log l'exception pour le diagnostic
        exception.printStackTrace();

        ErrorResponse response = new ErrorResponse(
                500,
                "Erreur interne du serveur : " + exception.getMessage()
        );

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response)
                .build();
    }
}
