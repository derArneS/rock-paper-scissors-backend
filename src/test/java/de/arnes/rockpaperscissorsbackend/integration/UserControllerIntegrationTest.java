package de.arnes.rockpaperscissorsbackend.integration;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.google.gson.Gson;

import de.arnes.rockpaperscissorsbackend.model.properties.JwtData;
import de.arnes.rockpaperscissorsbackend.model.rest.authorization.UserPrincipal;
import de.arnes.rockpaperscissorsbackend.model.users.profile.UserProfile;
import de.arnes.rockpaperscissorsbackend.model.users.profile.UserProfileService;
import de.arnes.rockpaperscissorsbackend.rest.security.SecurityTokenService;

/**
 *
 * @author Arne S.
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserProfileService userService;

	@Autowired
	private JwtData jwtData;

	private UserProfile userProfile;

	@BeforeEach
	public void initDb() {
		UserProfile tempUserProfile = new UserProfile("Arne", "test@test.de", "password");
		userProfile = userService.create(tempUserProfile);
	}

	@AfterEach
	public void clearDb() {
		userService.deleteById(userProfile.getId());
	}

	/**
	 * Checks if the new user is created in the test-"DB" and if it's returned
	 * correctly.
	 */
	@Test
	public void createUserShouldCreateAndReturnNewUser() throws Exception {
		final Gson gson = new Gson();
		final String body = gson.toJson(
				new UserProfile("IntegrationTestUser", "IntegrationTestUser@IntegrationTestUser.test", "StrongPW"));

		// runs the endpoint /user locally
		final ResultActions result = mockMvc
				.perform(post(("/user")).contentType(MediaType.APPLICATION_JSON).content(body));

		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("username", is("IntegrationTestUser"))) //
				.andExpect(jsonPath("email", is("IntegrationTestUser@IntegrationTestUser.test"))) //
				.andExpect(jsonPath("_links.self.href", endsWith("/user"))); //

		final UserProfile createdUser = gson.fromJson(result.andReturn().getResponse().getContentAsString(),
				UserProfile.class);

		userService.deleteById(createdUser.getId());
	}

	/**
	 * Checks if correct error message is thrown.
	 */
	@Test
	public void createUserShouldThrowMissingUserNameException() throws Exception {
		// Body with a wrong format, username is missing
		final String body = "{\"email\":\"IntegrationTestUser@IntegrationTestUser.test\",\"password\":\"StrongPW\"}";

		// runs the endpoint /user locally
		final ResultActions result = mockMvc
				.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(body));

		result.andExpect(status().isBadRequest());
		result.andExpect(jsonPath("errorMessage", equalTo("The input is not valid."))); //
	}

	/**
	 * Checks if the saved {@link UserProfile} from {@link BeforeEach} is returned
	 * correctly from the repository.
	 *
	 * @throws Exception
	 */
	@Test
	public void readUserByIdShouldReturnUserProfile() throws Exception {
		final SecurityTokenService tokenService = new SecurityTokenService(jwtData);
		final UserPrincipal userPrincipal = new UserPrincipal("Arne", "password", Collections.emptyList());

		final ResultActions result = mockMvc.perform(
				get("/user?id=" + userProfile.getId()).header("auth-token", tokenService.generateToken(userPrincipal)));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("id", is(userProfile.getId())))
				.andExpect(jsonPath("username", is(userProfile.getUsername())))
				.andExpect(jsonPath("email", is(userProfile.getEmail())))
				.andExpect(jsonPath("_links.self.href", endsWith("/user?id=" + userProfile.getId())));
	}

	/**
	 * Checks if the authorization for the endpoint /user?id={id} with
	 * {@link HttpMethod} GET only returns the username, because the token is not
	 * included in the headers.
	 *
	 * @throws Exception
	 */
	@Test
	public void readUserByIdShouldReturnForbidden() throws Exception {
		final ResultActions result = mockMvc.perform(get("/user?id=" + userProfile.getId()));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("id").doesNotExist()).andExpect(jsonPath("username", is(userProfile.getUsername())))
				.andExpect(jsonPath("email").doesNotExist())
				.andExpect(jsonPath("_links.self.href", endsWith("/user?id=" + userProfile.getId())));
	}

	/**
	 * Checks if the endpoint /user?id={id} returns {@link HttpStatus} OK and an
	 * empty body when the id is not found in the repository.
	 *
	 * @throws Exception
	 */
	@Test
	public void readUserByIdShouldReturnNotFound() throws Exception {
		final SecurityTokenService tokenService = new SecurityTokenService(jwtData);
		final UserPrincipal userPrincipal = new UserPrincipal("Arne", "password", Collections.emptyList());

		final ResultActions result = mockMvc
				.perform(get("/user?id=123").header("auth-token", tokenService.generateToken(userPrincipal)));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$").doesNotExist());
	}

}
