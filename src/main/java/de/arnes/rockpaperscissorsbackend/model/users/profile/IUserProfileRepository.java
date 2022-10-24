package de.arnes.rockpaperscissorsbackend.model.users.profile;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Arne S.
 *
 */
public interface IUserProfileRepository extends CrudRepository<UserProfile, String> {

	UserProfile findByUsernameIgnoreCase(String username);

	List<UserProfile> findByUsernameContains(String username);
	
	UserProfile findByEmailIgnoreCase(String email);

}
