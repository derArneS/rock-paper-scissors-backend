package de.arnes.rockpaperscissorsbackend.model.game;

import lombok.Getter;

/**
 * 
 * @author Arne S.
 *
 */
public class ComputerPlayer {

	@Getter
	private final Shape shape;
	
	private ComputerPlayer(Shape shape) {
		this.shape = shape;
	}
	
	public static ComputerPlayer play() {
		return new ComputerPlayer(Shape.randomShape());
	}
}
