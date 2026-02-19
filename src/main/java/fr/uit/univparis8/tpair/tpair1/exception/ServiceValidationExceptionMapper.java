package fr.uit.univparis8.tpair.tpair1.exception;

import fr.uit.univparis8.tpair.tpair1.dto.ErrorResponse;
import fr.uit.univparis8.tpair.tpair1.service.ValidationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Provider
public class ServiceValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException exception) {
        Map<String, String> errors = exception.getErrors();
        if (errors != null && !errors.isEmpty()) {
            List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();
            for (Map.Entry<String, String> entry : errors.entrySet()) {
                fieldErrors.add(new ErrorResponse.FieldError(entry.getKey(), entry.getValue()));
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(400, "Validation échouée", fieldErrors))
                    .build();
        }

        return Response.status(Response.Status.CONFLICT)
                .entity(new ErrorResponse(409, exception.getMessage()))
                .build();
    }
}
