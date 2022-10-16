package de.arnes.rockpaperscissorsbackend.model.rest.authorization;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 *
 * @author Arne S.
 *
 */
@Data
public class AuthenticationRequest {

	@NotNull
	private String username;

	@NotNull
	private String password;

}
