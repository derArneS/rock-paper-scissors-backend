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

import de.arnes.rockpaperscissorsbackend.model.rest.authorization.exception.BadUsernameException;
import de.arnes.rockpaperscissorsbackend.model.rest.authorization.exception.ExpiredTokenException;
import de.arnes.rockpaperscissorsbackend.model.users.profile.UserProfile;
import de.arnes.rockpaperscissorsbackend.model.users.profile.exception.UserAlreadyExistsException;
import de.arnes.rockpaperscissorsbackend.model.users.profile.exception.UserNotFoundException;
import de.arnes.rockpaperscissorsbackend.model.users.statistics.exception.UserStatisticsAlreadyCreatedException;
import de.arnes.rockpaperscissorsbackend.model.users.statistics.exception.UserStatisticsNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * Catches and handles exceptions thrown from the rest endpoints.
 *
 * @author Arne S.
 *
 */
@Slf4j
@ControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Catches {@link MethodArgumentNotValidException} and customizes the response.
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		final String errorMessage = "The input is not valid.";
		log.debug(errorMessage);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST) //
				.header("Content-Type", "application/json") //
				.body(new ErrorMessage(errorMessage));
	}

	/**
	 * Catches {@link MissingServletRequestParameterException} and customizes the
	 * response.
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
			final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status,
			final WebRequest request) {
		final String errorMessage = String.format("Parameter '%s' is missing.", ex.getParameterName());
		log.debug(errorMessage);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST) //
				.header("Content-Type", "application/json") //
				.body(new ErrorMessage(errorMessage));
	}

	/**
	 * Catches {@link MethodArgumentTypeMismatchException} and customizes the
	 * response.
	 */
	@ExceptionHandler(value = { MethodArgumentTypeMismatchException.class })
	public ResponseEntity<ErrorMessage> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex) {

		final String parameterName = ex.getName();
		final String type = ex.getRequiredType().getSimpleName();
		final Object value = ex.getValue();

		final String errorMessage = String.format("The value of '%s' (%s) is not a valid '%s'.", parameterName, value,
				type);
		log.debug(errorMessage);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST) //
				.header("Content-Type", "application/json") //
				.body(new ErrorMessage(errorMessage));
	}

	/**
	 * Catches {@link UserNotFoundException} and customizes the response.
	 */
	@ExceptionHandler(value = { UserNotFoundException.class })
	public ResponseEntity<UserProfile> handleUserNotFound(final UserNotFoundException ex) {
		final String errorMessage = ex.getMessage();
		log.debug(errorMessage);

		return ResponseEntity.status(HttpStatus.OK) //
				.header("Content-Type", "application/json") //
				.build();
	}

	/**
	 * Catches {@link ExpiredTokenException} and customizes the response.
	 */
	@ExceptionHandler(value = { ExpiredTokenException.class })
	public ResponseEntity<ErrorMessage> handleExpiredToken(final ExpiredTokenException ex) {
		final String errorMessage = ex.getMessage();
		log.debug(errorMessage);

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED) //
				.header("Content-Type", "application/json") //
				.body(new ErrorMessage(errorMessage));
	}

	/**
	 * Catches {@link BadUsernameException} and customizes the response.
	 */
	@ExceptionHandler(value = { BadUsernameException.class })
	public ResponseEntity<ErrorMessage> handleBadUsername(final BadUsernameException ex) {
		final String errorMessage = ex.getMessage();
		log.debug(errorMessage);

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED) //
				.header("Content-Type", "application/json") //
				.body(new ErrorMessage(errorMessage));
	}

	/**
	 * Catches {@link UserAlreadyExistsException} and customizes the response.
	 */
	@ExceptionHandler(value = { UserAlreadyExistsException.class })
	public ResponseEntity<ErrorMessage> handleUserAlreadyExists(final UserAlreadyExistsException ex) {
		final String errorMessage = ex.getMessage();
		log.debug(errorMessage);

		return ResponseEntity.status(HttpStatus.CONFLICT) //
				.header("Content-Type", "application/json") //
				.body(new ErrorMessage(errorMessage));
	}

	/**
	 * Catches {@link UserStatisticsAlreadyCreatedException} and customizes the
	 * response.
	 */
	@ExceptionHandler(value = { UserStatisticsAlreadyCreatedException.class })
	public ResponseEntity<ErrorMessage> handleUserStatisticsAlreadyCreated(
			final UserStatisticsAlreadyCreatedException ex) {
		final String errorMessage = ex.getMessage();
		log.debug(errorMessage);

		// Not in the HTTP Status Code Registry, but on wikipedia as: "The request
		// should
		// be retried after doing the appropriate action"
		return ResponseEntity.status(449) //
				.header("Content-Type", "application/json") //
				.body(new ErrorMessage("Please try again."));
	}

	/**
	 * Catches {@link UserStatisticsNotFoundException} and customizes the response.
	 */
	@ExceptionHandler(value = { UserStatisticsNotFoundException.class })
	public ResponseEntity<ErrorMessage> handleUserStatisticsNotFound(final UserStatisticsNotFoundException ex) {
		final String errorMessage = ex.getMessage();
		log.debug(errorMessage);

		// Not in the HTTP Status Code Registry, but on wikipedia as: "The request
		// should
		// be retried after doing the appropriate action"
		return ResponseEntity.status(423) //
				.header("Content-Type", "application/json") //
				.body(new ErrorMessage("This user is having a deeper issue. Please consult an admin."));
	}
}
