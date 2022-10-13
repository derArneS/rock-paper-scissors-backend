package de.arnes.rockpaperscissorsbackend.model.game;

import lombok.Getter;

/**
 * 
 * @author Arne S.
 *
 */
@Getter
public class GameResponse {
	
	private final Result result;
	
	private final String message;

	public GameResponse(Result result) {
		this.result = result;
		this.message = result.getMsg();
	}
}
