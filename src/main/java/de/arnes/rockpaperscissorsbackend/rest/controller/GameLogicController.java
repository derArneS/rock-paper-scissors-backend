package de.arnes.rockpaperscissorsbackend.rest.controller;

import static de.arnes.rockpaperscissorsbackend.model.game.Result.DRAW;
import static de.arnes.rockpaperscissorsbackend.model.game.Result.PLAYER_ONE;
import static de.arnes.rockpaperscissorsbackend.model.game.Result.PLAYER_TWO;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import de.arnes.rockpaperscissorsbackend.model.game.ComputerPlayer;
import de.arnes.rockpaperscissorsbackend.model.game.GameResponse;
import de.arnes.rockpaperscissorsbackend.model.game.Resource;
import de.arnes.rockpaperscissorsbackend.model.game.Result;
import de.arnes.rockpaperscissorsbackend.model.game.Shape;
import lombok.extern.slf4j.Slf4j;

/**
 * RestController for the game rock-paper-scissors.
 *
 * @author Arne S.
 *
 */
@Slf4j
@RestController
public class GameLogicController {

	/**
	 * Endpoint to inform the client of all possible shapes in this instance of the
	 * game rock-paper-scissors.
	 * 
	 * @return collection of all possible shapes
	 */
	@GetMapping("/shapes")
	public CollectionModel<EntityModel<Resource<Shape>>> getShapes() {
		log.debug("get@/shapes called");
		final List<EntityModel<Resource<Shape>>> entityModels = Arrays.asList(Shape.values()).stream()
				.map(shape -> EntityModel.of(new Resource<Shape>(shape), // linkTo(method)))
						Link.of(ServletUriComponentsBuilder.fromCurrentContextPath().build().toString()
								+ "/play?playerOne=" + shape + "&playerTwo=%1", "play"),
						Link.of(ServletUriComponentsBuilder.fromCurrentContextPath().build().toString() + "/computer",
								"computer")))
				.collect(Collectors.toList());

		return CollectionModel.of(entityModels);
	}

	/**
	 * Let's the computer chose its shape.
	 *
	 * @return Entity model of the {@link Shape} chosen from the computer.
	 */
	@GetMapping("/computer")
	public EntityModel<ComputerPlayer> computer() {
		log.debug("get@/computer called");
		final ComputerPlayer comPlayer = ComputerPlayer.play();

		// Creation of the entity model, contains the data generated from the computer
		// playing together with links to the next endpoints
		final EntityModel<ComputerPlayer> entityModel = EntityModel.of(comPlayer,
				linkTo(methodOn(GameLogicController.class).computer()).withSelfRel(), //
				Link.of(ServletUriComponentsBuilder.fromCurrentContextPath().build().toString()
						+ "/play?playerOne=%1&playerTwo=" + comPlayer.getShape(), "play"));

		return entityModel;
	}

	/**
	 * Endpoint for playing Rock-Paper-Scissors. Accepts the shapes of the players
	 * and determines the winner.
	 *
	 * @param playerOne
	 * @param playerTwo
	 *
	 * @return Entity model of the {@link GameResponse} chosen from the computer.
	 */
	@GetMapping("/play")
	public EntityModel<GameResponse> play(@RequestParam("playerOne") final Shape playerOne,
			@RequestParam("playerTwo") final Shape playerTwo) {
		log.debug("get@/play called with playerOne: {}, playerTwo: {}", playerOne, playerTwo);

		// draw
		if (playerOne == playerTwo)
			return createPlayResponse(playerOne, playerTwo, DRAW);

		// Player one wins
		if (playerOne.getWinsAgainst().stream().anyMatch(shapeAsString -> Shape.valueOf(shapeAsString) == playerTwo))
			return createPlayResponse(playerOne, playerTwo, PLAYER_ONE);

		// Player two wins
		return createPlayResponse(playerOne, playerTwo, PLAYER_TWO);
	}

	/**
	 * Creates the entity model for the {@link GameResponse}.
	 *
	 * @param playerOne
	 * @param playerTwo
	 * @param result
	 *
	 * @return {@link EntityModel} of the {@link GameResponse} chosen from the
	 *         computer.
	 */
	private EntityModel<GameResponse> createPlayResponse(final Shape playerOne, final Shape playerTwo,
			final Result result) {
		log.debug("Result of the game: {}", result);

		return EntityModel.of(new GameResponse(result),
				linkTo(methodOn(GameLogicController.class).play(playerOne, playerTwo)).withSelfRel(), //
				linkTo(methodOn(GameLogicController.class).computer()).withRel("computer"));
	}

}
