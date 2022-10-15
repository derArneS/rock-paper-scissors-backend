package de.arnes.rockpaperscissorsbackend.model.users;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Arne S.
 *
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

	private String id;

	@NotNull
	private String username;

	@NotNull
	private String email;

	@NotNull
	private String password;

	public User(@NotNull String username, @NotNull String email, @NotNull String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

}
