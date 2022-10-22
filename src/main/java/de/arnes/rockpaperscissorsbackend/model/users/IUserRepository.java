package de.arnes.rockpaperscissorsbackend.model.users;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Arne S.
 *
 */
public interface IUserRepository extends CrudRepository<UserProfile, String> {

	UserProfile findByUsernameIgnoreCase(String username);

	List<UserProfile> findByUsernameContains(String username);
	
	UserProfile findByEmailIgnoreCase(String email);

}
