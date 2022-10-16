package de.arnes.rockpaperscissorsbackend.model.rest.authorization.exception;

/**
 *
 * @author Arne S.
 *
 */
public class BadUsernameException extends RuntimeException {

	private static final long serialVersionUID = -3285654810698177197L;

	public BadUsernameException(final String message) {
		super(message);
	}

}
