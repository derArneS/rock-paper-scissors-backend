package de.arnes.rockpaperscissorsbackend.model.users.statistics.exception;

/**
 * 
 * @author Arne S.
 *
 */
public class UserStatisticsNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -7066829639797906467L;

	public UserStatisticsNotFoundException(final String msg) {
		super(msg);
	}
}
