package de.arnes.rockpaperscissorsbackend.model.users.profile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class UserProfile {

	@Id
	@Column
	private String id;

	@NotNull
	@Column(unique = true)
	private String username;

	@NotNull
	@Column(unique = true)
	private String email;

	@Column
	private String password;

	public UserProfile(@NotNull final String username, @NotNull final String email, @NotNull final String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

}
