package fr.uit.univparis8.tpair.tpair1.exception;

import fr.uit.univparis8.tpair.tpair1.dto.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger log = LoggerFactory.getLogger(GeneralExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {
        log.error("Unhandled API exception", exception);

        ErrorResponse response = new ErrorResponse(
                500,
                "Erreur interne du serveur"
        );

        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(response)
                .build();
    }
}
