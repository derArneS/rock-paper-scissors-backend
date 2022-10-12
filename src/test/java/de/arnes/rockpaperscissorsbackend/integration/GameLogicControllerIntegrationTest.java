package de.arnes.rockpaperscissorsbackend.integration;

import static de.arnes.rockpaperscissorsbackend.rest.model.game.Shape.PAPER;
import static de.arnes.rockpaperscissorsbackend.rest.model.game.Shape.ROCK;
import static de.arnes.rockpaperscissorsbackend.rest.model.game.Shape.SCISSOR;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import de.arnes.rockpaperscissorsbackend.rest.model.game.Shape;

/**
 * 
 * @author Arne S.
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GameLogicControllerIntegrationTest {

	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;

	/**
	 * Checks if the value returned by /computer is an allowed shape.
	 */
	@Test
	public void computerShouldReturnShape() {
		assertThat(restTemplate.getForObject("http://localhost:" + port + "/computer", Shape.class)).isIn(ROCK, PAPER, SCISSOR);
	}
	
}
