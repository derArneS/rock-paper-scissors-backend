package de.arnes.rockpaperscissorsbackend.model.rest.authorization.exception;

/**
 *
 * @author Arne S.
 *
 */
public class ExpiredTokenException extends RuntimeException {

	private static final long serialVersionUID = 1576218714730235960L;

	public ExpiredTokenException(final String message) {
		super(message);
	}
}
