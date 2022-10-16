package de.arnes.rockpaperscissorsbackend.model.users;

import java.util.UUID;

import org.springframework.stereotype.Component;

/**
 * 
 * @author Arne S.
 *
 */
@Component
public class UserService {

	private IUserRepository userRepository;

	public UserService(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Creates the {@link UserProfile} with a random generated id.
	 * 
	 * @param user
	 * @return created {@link UserProfile}
	 */
	public UserProfile create(UserProfile user) {
		user.setId(UUID.randomUUID().toString());
		return userRepository.save(user);
	}

	/**
	 * Saves the {@link UserProfile} after changing.
	 * 
	 * @param user
	 * @return created {@link UserProfile}
	 */
	public UserProfile save(UserProfile user) {
		return userRepository.save(user);
	}

}
