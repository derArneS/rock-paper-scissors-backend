package de.arnes.rockpaperscissorsbackend.rest.security.authentication;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import de.arnes.rockpaperscissorsbackend.model.rest.authorization.UserPrincipal;
import de.arnes.rockpaperscissorsbackend.model.rest.authorization.exception.BadUsernameException;
import de.arnes.rockpaperscissorsbackend.model.users.profile.UserProfile;
import de.arnes.rockpaperscissorsbackend.model.users.profile.UserProfileService;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author Arne S.
 *
 */
@Service
@RequiredArgsConstructor
public class AuthenticationUserDetailsService implements UserDetailsService {

	public static final String ROLE_USER = "ROLE_USER";

	private final UserProfileService userService;

	@Override
	public UserDetails loadUserByUsername(final String username) {
		final UserProfile user = userService.readByUsername(username)
				.orElseThrow(() -> new BadUsernameException("user " + username + " not found"));
		return new UserPrincipal(username, user.getPassword(), Collections.emptyList());
	}

}
