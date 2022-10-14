package de.arnes.rockpaperscissorsbackend.model.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 
 * @author Arne S.
 *
 */
@Getter
@EqualsAndHashCode
public class GameResponse {

	private final Result result;

	private final String message;

	public GameResponse(Result result) {
		this.result = result;
		this.message = result.getMsg();
	}
}
