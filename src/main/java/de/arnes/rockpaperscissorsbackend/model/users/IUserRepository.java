package de.arnes.rockpaperscissorsbackend.model.users;

import org.springframework.data.repository.CrudRepository;

/**
 * 
 * @author Arne S.
 *
 */
public interface IUserRepository extends CrudRepository<UserProfile, String> {

}
