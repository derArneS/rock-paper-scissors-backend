package de.arnes.rockpaperscissorsbackend.rest.controller;

import javax.validation.Valid;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import de.arnes.rockpaperscissorsbackend.model.rest.authorization.AuthenticationRequest;
import de.arnes.rockpaperscissorsbackend.model.rest.authorization.AuthenticationResponse;
import de.arnes.rockpaperscissorsbackend.rest.security.SecurityTokenService;
import de.arnes.rockpaperscissorsbackend.rest.security.authentication.AuthenticationUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Arne S.
 *
 */
@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationManager authenticationManager;

	private final SecurityTokenService tokenService;

	private final AuthenticationUserDetailsService authenticationUserDetailsService;

	/**
	 * Authenticates the user and creates a jwt if the login is successful.
	 *
	 * @param authenticationRequest
	 * @return a jwt in an {@link AuthenticationResponse}
	 */
	@PostMapping("/authenticate")
	public AuthenticationResponse authenticate(@RequestBody @Valid final AuthenticationRequest authenticationRequest) {
		String username = authenticationRequest.getUsername();
		
		log.debug("/authenticate called, trying to authenticate {}", username);
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,
				authenticationRequest.getPassword()));
		log.debug("user authenticated");

		final UserDetails userDetails = authenticationUserDetailsService
				.loadUserByUsername(username);

		final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		authenticationResponse.setAccessToken(tokenService.generateToken(userDetails));
		return authenticationResponse;
	}
}
