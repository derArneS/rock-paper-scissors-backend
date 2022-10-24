package de.arnes.rockpaperscissorsbackend.model.users.statistics.exception;

/**
 * 
 * @author Arne S.
 *
 */
public class UserStatisticsAlreadyCreatedException extends RuntimeException {

	private static final long serialVersionUID = 3508562230435210927L;

	public UserStatisticsAlreadyCreatedException(final String msg) {
		super(msg);
	}
}
