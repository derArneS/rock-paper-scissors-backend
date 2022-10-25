package de.arnes.rockpaperscissorsbackend.rest.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import de.arnes.rockpaperscissorsbackend.model.users.profile.UserProfile;
import de.arnes.rockpaperscissorsbackend.model.users.profile.UserProfileService;
import de.arnes.rockpaperscissorsbackend.model.users.profile.exception.UserAlreadyExistsException;
import de.arnes.rockpaperscissorsbackend.model.users.profile.exception.UserNotFoundException;
import de.arnes.rockpaperscissorsbackend.model.users.statistics.UserDTO;
import de.arnes.rockpaperscissorsbackend.model.users.statistics.UserStatistics;
import de.arnes.rockpaperscissorsbackend.model.users.statistics.UserStatisticsService;
import de.arnes.rockpaperscissorsbackend.rest.security.SecurityTokenService;
import lombok.extern.slf4j.Slf4j;

/**
 * RestController for user profiles and basic authentication.
 *
 * @author Arne S.
 *
 */
@Slf4j
@CrossOrigin
@RestController
public class UserController {

	@Value("${server.port}")
	private int serverPort;

	private final UserProfileService userService;

	private final UserStatisticsService userStatisticsService;

	private final SecurityTokenService securityTokenService;

	public UserController(final UserProfileService userService, SecurityTokenService securityTokenService,
			UserStatisticsService userStatisticsService) {
		this.userService = userService;
		this.userStatisticsService = userStatisticsService;
		this.securityTokenService = securityTokenService;
	}

	/**
	 * Validates the input, then creates the user and saves him in the repository.
	 *
	 * @param userToCreate
	 * @return {@link EntityModel} of the {@link UserProfile} created from the
	 *         input.
	 */
	@PostMapping("/user")
	public ResponseEntity<EntityModel<UserProfile>> createUser(@RequestBody @Valid final UserProfile userToCreate) {
		log.debug("post@/user called");

		// TODO: needs testing
		if (userService.readByUsername(userToCreate.getUsername()).isPresent())
			throw new UserAlreadyExistsException("The username you are trying to create already exists");

		final UserProfile savedUser = userService.create(userToCreate);

		// Builds the uri of the newly created user so it can easily be read again.
		final URI location = ServletUriComponentsBuilder.fromCurrentRequest().port(serverPort).path("/{id}")
				.buildAndExpand(savedUser.getId()).toUri();

		// the hashed password should never be given out to the client.
		savedUser.setPassword(null);

		return ResponseEntity.created(location).body(EntityModel.of(savedUser, //
				linkTo(methodOn(UserController.class).createUser(userToCreate)).withSelfRel())); //
	}

	/**
	 * Read the user from the repository. If there is no user with the given id a
	 * {@link UserNotFoundException} is thrown. Will never return a password for the
	 * user, because the hashed password should not be returned to the client.
	 *
	 * @param username
	 * @return {@link EntityModel} of the {@link UserProfile} read from the from the
	 *         repository.
	 */
	@GetMapping(value = "/user", params = "id")
	public EntityModel<UserDTO> readUserById(@RequestHeader(value = "auth-token", required = false) final String jwt,
			@RequestParam final String id) {
		// TODO: needs more testing
		log.debug("get@/user?id={} called", id);
		UserProfile readUser = userService.readById(id)
				.orElseThrow(() -> new UserNotFoundException(String.format("User with id '%s' not found.", id)));

		UserStatistics stats = null;
		if (!verifyUser(jwt, readUser.getUsername())) {
			log.debug("jwt doesn't fit the read user");
			readUser = anonUser(readUser);
		} else {
			log.debug("jwt fits the user, reading his stats");
			stats = userStatisticsService.findById(readUser.getId());
		}

		// the hashed password should never be given out to the client.
		readUser.setPassword(null);

		return EntityModel.of(new UserDTO(readUser, stats), //
				linkTo(methodOn(UserController.class).readUserById(null, id)).withSelfRel()); //
	}

	/**
	 * Read the user from the repository. If there is no user with the given
	 * username a {@link UserNotFoundException} is thrown. Will never return a
	 * password for the user, because the hashed password should not be returned to
	 * the client.
	 *
	 * @param username
	 * @return {@link EntityModel} of the {@link UserProfile} read from the from the
	 *         repository.
	 */
	@GetMapping(value = "/user", params = "username")
	public EntityModel<UserDTO> readUserByUsername(
			@RequestHeader(value = "auth-token", required = false) final String jwt,
			@RequestParam final String username) {
		// TODO: needs testing
		log.debug("get@/user?username={} called", username);
		UserProfile readUser = userService.readByUsername(username).orElseThrow(
				() -> new UserNotFoundException(String.format("User with username '%s' not found.", username)));

		UserStatistics stats = null;
		if (!verifyUser(jwt, readUser.getUsername())) {
			log.debug("jwt doesn't fit the read user");
			readUser = anonUser(readUser);
		} else {
			log.debug("jwt fits the user, reading his stats");
			stats = userStatisticsService.findById(readUser.getId());
		}

		// the hashed password should never be given out to the client.
		readUser.setPassword(null);

		return EntityModel.of(new UserDTO(readUser, stats), //
				linkTo(methodOn(UserController.class).readUserByUsername(null, username)).withSelfRel()); //
	}

	/**
	 * Read the user from the repository. If there is no user with the given e-mail
	 * a {@link UserNotFoundException} is thrown. Will never return a password for
	 * the user, because the hashed password should not be returned to the client.
	 *
	 * @param username
	 * @return {@link EntityModel} of the {@link UserProfile} read from the from the
	 *         repository.
	 */
	@GetMapping(value = "/user", params = "email")
	public EntityModel<UserDTO> readUserByEmail(@RequestHeader(value = "auth-token", required = false) final String jwt,
			@RequestParam final String email) {
		// TODO: needs more testing
		log.debug("get@/user?email={} called", email);
		UserProfile readUser = userService.readByEmail(email)
				.orElseThrow(() -> new UserNotFoundException(String.format("User with e-mail '%s' not found.", email)));

		UserStatistics stats = null;
		if (!verifyUser(jwt, readUser.getUsername())) {
			log.debug("jwt doesn't fit the read user");
			readUser = anonUser(readUser);
		} else {
			log.debug("jwt fits the user, reading his stats");
			stats = userStatisticsService.findById(readUser.getId());
		}

		// the hashed password should never be given out to the client.
		readUser.setPassword(null);

		return EntityModel.of(new UserDTO(readUser, stats), //
				linkTo(methodOn(UserController.class).readUserByEmail(null, email)).withSelfRel()); //
	}

	/**
	 * Searches all {@link UserProfile} with an username that contains the provided
	 * string.
	 *
	 * @param username
	 * @return {@link CollectionModel} of {@link EntityModel}s of all users which
	 *         usernames contain the string.
	 */
	@GetMapping("/search/user")
	public CollectionModel<EntityModel<UserProfile>> searchUserByUsername(
			@RequestParam("username") final String username) {
		log.debug("get@/search/user called with username '{}'", username);
		final List<UserProfile> readUsers = userService.findByUsernameContains(username);

		final List<EntityModel<UserProfile>> entityModels = readUsers.stream().map(user -> {
			user.setPassword(null);
			user.setEmail(null);
			return user;
		}).sorted((user1, user2) -> user1.getUsername().compareTo(user2.getUsername()))
				.map(user -> EntityModel.of(user,
						linkTo(methodOn(UserController.class).readUserById(null, user.getId())).withRel("readById")))
				.collect(Collectors.toList());

		return CollectionModel.of(entityModels);
	}

	/**
	 * Deletes the user, if the id matches the id
	 * 
	 * @param bearerJwt
	 * @param username
	 * @return {@link ResponseEntity} with {@link HttpStatus} and without body
	 */
	@DeleteMapping("/user/{username}")
	public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String bearerJwt,
			@PathVariable("username") String username) {
		log.debug("delete@/user/{} called", username);

		if (null == bearerJwt || bearerJwt.length() < 8)
			return ResponseEntity.badRequest().build();

		UserProfile readUser = userService.readByUsername(username).orElseThrow(
				() -> new UserNotFoundException(String.format("User with username '%s' not found.", username)));

		if (!verifyUser(bearerJwt.substring(7), readUser.getUsername()))
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

		userService.deleteById(readUser.getId());

		return ResponseEntity.ok().build();
	}

	/**
	 * Checks if the client is authenticated and if the authenticated user is trying
	 * to read his own profile.
	 * 
	 * @param jwt
	 * @param username
	 * @return true when the authenticated user is reading his own profile,
	 *         otherwise false.
	 */
	private boolean verifyUser(final String jwt, final String username) {
		if (null == jwt || "".equals(jwt.trim())) {
			return false;
		}

		final String jwtUsername = securityTokenService.validateTokenAndGetUsername(jwt);

		if (null != jwtUsername && jwtUsername.equals(username))
			return true;

		return false;
	}

	/**
	 * Creates a copy of the given user and deletes the fields only the user himself
	 * should get.
	 * 
	 * @param readUser
	 * @return
	 */
	private UserProfile anonUser(final UserProfile readUser) {
		final UserProfile anonUser = readUser;

		anonUser.setId(null);
		anonUser.setEmail(null);

		return anonUser;
	}

}
