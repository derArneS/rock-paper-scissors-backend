package de.arnes.rockpaperscissorsbackend.model.users.statistics;

import de.arnes.rockpaperscissorsbackend.model.users.profile.UserProfile;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Arne S.
 *
 */
@Getter
@Setter
public class UserDTO {

	private String id;

	private String username;

	private String email;

	private String password;

	private int rock;

	private int paper;

	private int scissors;

	private int computerRock;

	private int computerPaper;

	private int computerScissors;

	public UserDTO(UserProfile profile, UserStatistics stats) {
		if (null != profile) {
			this.id = profile.getId();
			this.username = profile.getUsername();
			this.email = profile.getEmail();
			this.password = profile.getPassword();
		}
		
		if (null != stats) {
			this.rock = stats.getRock();
			this.paper = stats.getPaper();
			this.scissors = stats.getScissors();
			this.computerRock = stats.getComputerRock();
			this.computerPaper = stats.getComputerPaper();
			this.computerScissors = stats.getComputerScissors();
		}

	}

}
