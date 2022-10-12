package de.arnes.rockpaperscissorsbackend.rest.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import de.arnes.rockpaperscissorsbackend.model.game.Shape;
import de.arnes.rockpaperscissorsbackend.rest.model.game.ComputerPlayer;

/**
 * RestController for the game rock-paper-scissors.
 * 
 * @author Arne S.
 *
 */
@RestController
public class GameLogicController {

	/**
	 * Let's the computer player play its turn.
	 * 
	 * @return the {@link Shape} chosen from the computer.
	 */
	@GetMapping("/computer")
	public EntityModel<ComputerPlayer> computer() {
		ComputerPlayer comPlayer = ComputerPlayer.play();
		
		// Creation of the entity model, contains the data generated from the computer playing
		// together with links to the next endpoints
		EntityModel<ComputerPlayer> entityModel = EntityModel.of(comPlayer,
				linkTo(methodOn(GameLogicController.class).computer()).withSelfRel());
		
		return entityModel;
	}

}
