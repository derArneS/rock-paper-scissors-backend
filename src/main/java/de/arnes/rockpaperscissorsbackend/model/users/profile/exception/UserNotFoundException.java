package de.arnes.rockpaperscissorsbackend.model.users.profile.exception;

/**
 *
 * @author Arne S.
 *
 */
public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7267737118175627169L;

	public UserNotFoundException(final String message) {
		super(message);
	}

}
