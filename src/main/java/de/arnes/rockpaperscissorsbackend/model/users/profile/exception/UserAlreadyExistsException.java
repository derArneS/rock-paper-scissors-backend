package de.arnes.rockpaperscissorsbackend.model.users.profile.exception;

/**
 * 
 * @author Arne S.
 *
 */
public class UserAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -1514171418941443451L;
	
	public UserAlreadyExistsException(final String msg) {
		super(msg);
	}

}
