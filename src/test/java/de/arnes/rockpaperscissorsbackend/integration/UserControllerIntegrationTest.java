package de.arnes.rockpaperscissorsbackend.integration;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.google.gson.Gson;

import de.arnes.rockpaperscissorsbackend.configuration.UserTestConfiguration;
import de.arnes.rockpaperscissorsbackend.model.users.User;

/**
 * 
 * @author Arne S.
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@Import(UserTestConfiguration.class)
public class UserControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * Checks if the new user is created in the test-"DB" and if it's returned
	 * correctly.
	 */
	@Test
	public void createUserShouldCreateAndReturnNewUser() throws Exception {
		String body = new Gson()
				.toJson(new User("IntegrationTestUser", "IntegrationTestUser@IntegrationTestUser.test", "StrongPW"));

		// runs the endpoint /user locally
		final ResultActions result = mockMvc
				.perform(post(("/user")).contentType(MediaType.APPLICATION_JSON).content(body));

		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("username", is("IntegrationTestUser"))) //
				.andExpect(jsonPath("email", is("IntegrationTestUser@IntegrationTestUser.test"))) //
				.andExpect(jsonPath("password", is("StrongPW"))) //
				.andExpect(jsonPath("_links.self.href", endsWith("/user"))); //
	}

	/**
	 * Checks if correct error message is thrown.
	 */
	@Test
	public void createUserShouldThrowMissingUserNameException() throws Exception {
		// Body with a wrong format, username is missing
		String body = "{\"email\":\"IntegrationTestUser@IntegrationTestUser.test\",\"password\":\"StrongPW\"}";

		// runs the endpoint /user locally
		final ResultActions result = mockMvc
				.perform(post(("/user")).contentType(MediaType.APPLICATION_JSON).content(body));

		result.andExpect(status().isBadRequest());
		result.andExpect(jsonPath("errorMessage", equalTo("The input is not valid."))); //
	}

}
