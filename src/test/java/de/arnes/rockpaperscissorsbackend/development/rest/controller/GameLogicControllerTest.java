package de.arnes.rockpaperscissorsbackend.development.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.junit.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.util.ReflectionTestUtils;

import de.arnes.rockpaperscissorsbackend.model.game.GameResponse;
import de.arnes.rockpaperscissorsbackend.model.game.Result;
import de.arnes.rockpaperscissorsbackend.model.game.Shape;
import de.arnes.rockpaperscissorsbackend.rest.controller.GameLogicController;

/**
 *
 * @author Arne S.
 *
 */
public class GameLogicControllerTest {

	private final GameLogicController testee = new GameLogicController();

	/**
	 * Checks if the method createPlayResponse generates the correct
	 * {@link EntityModel}.
	 */
	@Test
	public void createPlayResponseShouldReturnCorrectEntityModel() {
		final EntityModel<GameResponse> expected = EntityModel.of(new GameResponse(Result.DRAW),
				linkTo(methodOn(GameLogicController.class).play(Shape.ROCK, Shape.ROCK)).withSelfRel(), //
				linkTo(methodOn(GameLogicController.class).computer()).withRel("computer"));

		// Method needs reflection because it is private.
		final EntityModel<GameResponse> actual = ReflectionTestUtils.invokeMethod(testee, "createPlayResponse",
				Shape.ROCK, Shape.ROCK, Result.DRAW);

		assertEquals(expected, actual);
	}

}
