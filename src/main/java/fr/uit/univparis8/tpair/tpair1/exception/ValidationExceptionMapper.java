package fr.uit.univparis8.tpair.tpair1.exception;

import fr.uit.univparis8.tpair.tpair1.dto.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.ArrayList;
import java.util.List;


@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<ErrorResponse.FieldError> fieldErrors = new ArrayList<>();
        
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            fieldErrors.add(new ErrorResponse.FieldError(
                    violation.getPropertyPath().toString(),
                    violation.getMessage()
            ));
        }
        
        ErrorResponse response = new ErrorResponse(400, "Validation échouée", fieldErrors);
        
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(response)
                .build();
    }
}
