package de.arnes.rockpaperscissorsbackend.model.users;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Arne S.
 *
 */
@Component
public class UserService {

	private final IUserRepository userRepository;

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public UserService(final IUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Creates the {@link UserProfile} with a random generated id.
	 *
	 * @param user
	 * @return created {@link UserProfile}
	 */
	public UserProfile create(final UserProfile user) {
		user.setId(UUID.randomUUID().toString());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	/**
	 * Saves the {@link UserProfile} after changing. If a new password is set, it
	 * will be encoded befor saving it in the db.
	 *
	 * @param user
	 * @return created {@link UserProfile}
	 */
	public UserProfile save(final UserProfile user) {
		final UserProfile oldUser = readById(user.getId()).get();

		final String hashedPW = passwordEncoder.encode(user.getPassword());
		if (!oldUser.getPassword().equals(hashedPW))
			user.setPassword(hashedPW);

		return userRepository.save(user);
	}

	/**
	 * Finds the user in the repository.
	 *
	 * @param id
	 * @return {@link Optional}<{@link UserProfile}>
	 */
	public Optional<UserProfile> readById(final String id) {
		return userRepository.findById(id);
	}

	/**
	 * Finds the user in the repository.
	 *
	 * @param id
	 * @return {@link Optional}<{@link UserProfile}>
	 */
	public Optional<UserProfile> readByUsername(final String username) {
		return Optional.ofNullable(userRepository.findByUsername(username));
	}

	/**
	 * Finds the user in the repository.
	 *
	 * @param username
	 * @return {@link List}<{@link UserProfile}> with all the found users
	 */
	public List<UserProfile> findByUsernameContains(final String username) {
		return userRepository.findByUsernameContains(username);
	}

	/**
	 * Deletes the user in the repository.
	 *
	 * @param id
	 */
	public void deleteById(final String id) {
		userRepository.deleteById(id);
	}

}
