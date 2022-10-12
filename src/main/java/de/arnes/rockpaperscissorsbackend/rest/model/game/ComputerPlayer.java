package de.arnes.rockpaperscissorsbackend.rest.model.game;

import de.arnes.rockpaperscissorsbackend.model.game.Shape;
import lombok.Getter;

public class ComputerPlayer {

	@Getter
	private Shape shape;
	
	private ComputerPlayer() {
		
	}
	
	private ComputerPlayer(Shape shape) {
		this.shape = shape;
	}
	
	public static ComputerPlayer play() {
		return new ComputerPlayer(Shape.randomShape());
	}
}
