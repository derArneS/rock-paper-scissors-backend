package de.arnes.rockpaperscissorsbackend.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import de.arnes.rockpaperscissorsbackend.model.users.IUserRepository;
import de.arnes.rockpaperscissorsbackend.testimplementation.TestUserRepository;

/**
 * Configuration for spring boot tests, so the autowired fields can be filled
 * with special test-implementations
 * 
 * @author Arne S.
 *
 */
@TestConfiguration
public class UserTestConfiguration {

	@Bean
	IUserRepository getUserRepository() {
		return new TestUserRepository();
	}

}
