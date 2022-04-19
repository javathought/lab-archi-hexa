package devoxx.lab.archihexa.courtage.application.quarkus.exceptionhandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Provider
public class ConstraintViolationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {
	@Override
	public Response toResponse(ConstraintViolationException e) {
		return Response
			.status(Response.Status.BAD_REQUEST)
			.entity(
				e.getConstraintViolations().stream()
					.map(constraintViolation -> "\t" + getPropertyName(constraintViolation) + " " + constraintViolation.getMessage())
					.collect(Collectors.joining("\n", "Donnée(s) erronée(s):\n", ""))
			)
			.build();
	}

	private static String getPropertyName(ConstraintViolation<?> constraintViolation) {
		return StreamSupport
			.stream(constraintViolation.getPropertyPath().spliterator(), false)
			// Récupération du dernier "noeud" du chemin
			.reduce((first, second) -> second)
			.map(Path.Node::getName)
			.orElse(null);
	}
}