package de.arnes.rockpaperscissorsbackend.model.users;

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

	@Column
	@NotNull
	private String username;

	@Column
	@NotNull
	private String email;

	@Column
	@NotNull
	private String password;

	public UserProfile(@NotNull String username, @NotNull String email, @NotNull String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

}
