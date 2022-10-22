package de.arnes.rockpaperscissorsbackend.model.users;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Arne S.
 *
 */
@Slf4j
@Component
public class UserService {

	private final IUserRepository userRepository;

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public UserService(final IUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Creates the {@link UserProfile} with a random generated id.
	 * Warning: This method overrides the id!
	 *
	 * @param user
	 * @return created {@link UserProfile}
	 */
	public UserProfile create(final UserProfile user) {
		user.setId(UUID.randomUUID().toString());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		log.debug("create user with id '{}' and username '{}'", user.getId(), user.getUsername());
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

		log.debug("create user with id '{}' and username '{}'", user.getId(), user.getUsername());
		return userRepository.save(user);
	}

	/**
	 * Finds the user in the repository.
	 *
	 * @param id
	 * @return {@link Optional}<{@link UserProfile}>
	 */
	public Optional<UserProfile> readById(final String id) {
		log.debug("find user by id '{}'", id);
		return userRepository.findById(id);
	}

	/**
	 * Finds the user in the repository.
	 *
	 * @param id
	 * @return {@link Optional}<{@link UserProfile}>
	 */
	public Optional<UserProfile> readByUsername(final String username) {
		log.debug("find user by username '{}'", username);
		return Optional.ofNullable(userRepository.findByUsernameIgnoreCase(username));
	}
	
	/**
	 * Finds the user in the repository.
	 *
	 * @param id
	 * @return {@link Optional}<{@link UserProfile}>
	 */
	public Optional<UserProfile> readByEmail(final String email) {
		log.debug("find user by email '{}'", email);
		return Optional.ofNullable(userRepository.findByEmailIgnoreCase(email));
	}

	/**
	 * Finds the user in the repository.
	 *
	 * @param username
	 * @return {@link List}<{@link UserProfile}> with all the found users
	 */
	public List<UserProfile> findByUsernameContains(final String username) {
		log.debug("find user if username contains {}", username);
		return userRepository.findByUsernameContains(username);
	}

	/**
	 * Deletes the user in the repository.
	 *
	 * @param id
	 */
	public void deleteById(final String id) {
		log.debug("delete user by id '{}'", id);
		userRepository.deleteById(id);
	}

}
