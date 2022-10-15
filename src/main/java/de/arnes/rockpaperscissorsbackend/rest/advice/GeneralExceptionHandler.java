package de.arnes.rockpaperscissorsbackend.rest.advice;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.log4j.Log4j2;

/**
 * Catches and handles exceptions thrown from the rest endpoints.
 * 
 * @author Arne S.
 *
 */
@Log4j2
@ControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Catches {@link MethodArgumentNotValidException} and customizes the response.
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String errorMessage = "The input is not valid.";
		log.warn(errorMessage);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST) //
				.header("Content-Type", "application/json") //
				.body(new ErrorMessage(errorMessage));
	}

	/**
	 * Catches {@link MissingServletRequestParameterException} and customizes the
	 * response.
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST) //
				.header("Content-Type", "application/json") //
				.body(new ErrorMessage("Parameter '" + ex.getParameterName() + "' is missing."));
	}

	/**
	 * Catches {@link MethodArgumentTypeMismatchException} and customizes the
	 * response.
	 */
	@ExceptionHandler(value = { MethodArgumentTypeMismatchException.class })
	public ResponseEntity<ErrorMessage> handleMethodArgumentTypeMismatchException(
			final MethodArgumentTypeMismatchException ex) {

		final String parameterName = ex.getName();
		final String type = ex.getRequiredType().getSimpleName();
		final Object value = ex.getValue();

		String errorMessage = String.format("The value of '%s' (%s) is not a valid '%s'.", parameterName, value, type);
		log.warn(errorMessage);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST) //
				.header("Content-Type", "application/json") //
				.body(new ErrorMessage(errorMessage));
	}

}
