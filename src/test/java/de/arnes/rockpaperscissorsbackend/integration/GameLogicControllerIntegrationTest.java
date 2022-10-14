package de.arnes.rockpaperscissorsbackend.integration;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * 
 * @author Arne S.
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class GameLogicControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * Checks if the value returned by /computer is an allowed shape.
	 */
	@Test
	public void computerShouldReturnShape() throws Exception {
		// runs the endpoint /computer locally
		final ResultActions result = mockMvc.perform(get(("/computer")));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("shape", anyOf(is("ROCK"), is("PAPER"), is("SCISSORS")))) //
				.andExpect(jsonPath("_links.self.href", endsWith("/computer"))); //
	}

	/**
	 * Checks if the correct error message is thrown when a shape is missing.
	 * 
	 * @throws Exception
	 */
	@Test
	public void playShouldThrowMissingShapeExPlayerOne() throws Exception {
		// runs the endpoint /play locally
		final ResultActions result = mockMvc.perform(get(("/play")));

		result.andExpect(status().isBadRequest());
		result.andExpect(jsonPath("errorMessage", equalTo("Parameter 'playerOne' is missing."))); //
	}

	/**
	 * Checks if the correct error message is thrown when a shape is missing.
	 * 
	 * @throws Exception
	 */
	@Test
	public void playShouldThrowMissingShapeExPlayerTwo() throws Exception {
		// runs the endpoint /play locally
		final ResultActions result = mockMvc.perform(get(("/play?playerOne=PAPER")));

		result.andExpect(status().isBadRequest());
		result.andExpect(jsonPath("errorMessage", equalTo("Parameter 'playerTwo' is missing."))); //
	}

	/**
	 * Checks if the correct error message is thrown when a shape is in a wrong
	 * format.
	 * 
	 * @throws Exception
	 */
	@Test
	public void playShouldThrowWrongShapeExPlayerOne() throws Exception {
		// runs the endpoint /play locally
		final ResultActions result = mockMvc.perform(get(("/play?playerOne=abc")));

		result.andExpect(status().isBadRequest());
		result.andExpect(jsonPath("errorMessage", equalTo("The value of 'playerOne' (abc) is not a valid 'Shape'."))); //
	}

	/**
	 * Checks if the correct error message is thrown when a shape is in a wrong
	 * format.
	 * 
	 * @throws Exception
	 */
	@Test
	public void playShouldThrowWrongShapeExPlayerTwo() throws Exception {
		// runs the endpoint /play locally
		final ResultActions result = mockMvc.perform(get(("/play?playerOne=PAPER&playerTwo=abc")));

		result.andExpect(status().isBadRequest());
		result.andExpect(jsonPath("errorMessage", equalTo("The value of 'playerTwo' (abc) is not a valid 'Shape'."))); //
	}

	/**
	 * Checks if the match ends in a draw
	 */
	@Test
	public void playShouldReturnDraw() throws Exception {
		// runs the endpoint /play locally
		final ResultActions result = mockMvc.perform(get(("/play?playerOne=PAPER&playerTwo=PAPER")));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("result", is("DRAW"))) //
				.andExpect(jsonPath("message", is("It's a draw."))) //
				.andExpect(jsonPath("_links.self.href", endsWith("/play?playerOne=PAPER&playerTwo=PAPER")))
				.andExpect(jsonPath("_links.computer.href", endsWith("/computer"))); //
	}

	/**
	 * Checks if the match ends with player one winning.
	 */
	@Test
	public void playShouldReturnPlayerOneWinning() throws Exception {
		// runs the endpoint /play locally
		final ResultActions result = mockMvc.perform(get(("/play?playerOne=SCISSORS&playerTwo=PAPER")));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("result", is("PLAYER_ONE"))) //
				.andExpect(jsonPath("message", is("Player one wins!"))) //
				.andExpect(jsonPath("_links.self.href", endsWith("/play?playerOne=SCISSORS&playerTwo=PAPER"))) //
				.andExpect(jsonPath("_links.computer.href", endsWith("/computer"))); //
	}

	/**
	 * Checks if the match ends with player two winning.
	 */
	@Test
	public void playShouldReturnPlayerTwoWinning() throws Exception {
		// runs the endpoint /play locally
		final ResultActions result = mockMvc.perform(get(("/play?playerOne=PAPER&playerTwo=SCISSORS")));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("result", is("PLAYER_TWO"))) //
				.andExpect(jsonPath("message", is("Player two wins!"))) //
				.andExpect(jsonPath("_links.self.href", endsWith("/play?playerOne=PAPER&playerTwo=SCISSORS"))) //
				.andExpect(jsonPath("_links.computer.href", endsWith("/computer"))); //
	}
}
