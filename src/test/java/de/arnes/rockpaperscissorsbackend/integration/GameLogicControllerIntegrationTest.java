package de.arnes.rockpaperscissorsbackend.integration;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import de.arnes.rockpaperscissorsbackend.rest.controller.GameLogicController;

/**
 * 
 * @author Arne S.
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(GameLogicController.class)
public class GameLogicControllerIntegrationTest {


	@Autowired
	private MockMvc mockMvc;

	/**
	 * Checks if the value returned by /computer is an allowed shape.
	 */
	@Test
	public void computerShouldReturnShape() throws Exception {
		//runs the endpoint /computer locally
		final ResultActions result = mockMvc.perform(get(("/computer")));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("shape", anyOf(is("ROCK"), is("PAPER"), is("SCISSORS")))) //
				.andExpect(jsonPath("_links.self.href", endsWith("/computer"))); //
	}

}
