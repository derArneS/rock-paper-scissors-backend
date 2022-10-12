package de.arnes.rockpaperscissorsbackend.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import de.arnes.rockpaperscissorsbackend.rest.model.game.Shape;

/**
 * RestController for the game rock-paper-scissors.
 * 
 * @author Arne S.
 *
 */
@RestController
public class GameLogicController {
	
	/**
	 * @return the {@link Shape} chosen from the computer.
	 */
	@GetMapping("/computer")
	public Shape computer() {
		return Shape.ROCK; // most simple implementation, only returns rock (for testing purposes)
	}

}
