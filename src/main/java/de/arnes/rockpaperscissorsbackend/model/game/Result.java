package de.arnes.rockpaperscissorsbackend.model.game;

import lombok.Getter;

/**
 * Possible results of the game.
 * 
 * @author Arne S.
 *
 */
public enum Result {

	DRAW("It's a draw."),
	PLAYER_ONE("Player one wins!"),
	PLAYER_TWO("Player two wins!");
	
	@Getter
	private String msg;
	
	private Result(String msg) {
		this.msg = msg;
	}
}
