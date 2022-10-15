package de.arnes.rockpaperscissorsbackend.model.users;

import java.util.Optional;

import lombok.NonNull;

/**
 * 
 * @author Arne S.
 *
 */
public interface IUserRepository {

	/**
	 * Creates the {@link User} and saves him in the repository.
	 * 
	 * @param userToCreate
	 * @return created {@link User}
	 */
	public User createUser(@NonNull User userToCreate);

	/**
	 * Read the {@link User} with the specified id from the repository.
	 * 
	 * @param id
	 * @return read {@link User}
	 */
	public Optional<User> readUserById(@NonNull String id);

	/**
	 * Updates the {@link User} with the specified id.
	 * 
	 * @param id
	 * @return updated {@link User}
	 */
	public User updateUserById(@NonNull String id);

	/**
	 * Deletes the {@link User} with the specified id from the repository.
	 * 
	 * @param id
	 * @return deleted {@link User}.
	 */
	public User deleteUserbyId(@NonNull String id);

}
