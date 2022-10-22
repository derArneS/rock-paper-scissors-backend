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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import de.arnes.rockpaperscissorsbackend.model.users.UserProfile;
import de.arnes.rockpaperscissorsbackend.model.users.UserService;
import de.arnes.rockpaperscissorsbackend.model.users.exception.UserAlreadyExistsException;
import de.arnes.rockpaperscissorsbackend.model.users.exception.UserNotFoundException;
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

	private final UserService userService;

	public UserController(final UserService userService) {
		this.userService = userService;
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
		log.debug("/user called");

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
	public EntityModel<UserProfile> readUserById(@RequestParam final String id) {
		// TODO: only find all infos if the authorized user is searching himself
		log.debug("/user?id={} called", id);
		final UserProfile readUser = userService.readById(id)
				.orElseThrow(() -> new UserNotFoundException(String.format("User with id '%s' not found.", id)));

		// the hashed password should never be given out to the client.
		readUser.setPassword(null);

		return EntityModel.of(readUser, //
				linkTo(methodOn(UserController.class).readUserById(id)).withSelfRel()); //
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
	public EntityModel<UserProfile> readUserByUsername(@RequestParam final String username) {
		// TODO: only find all infos if the authorized user is searching himself
		// TODO: needs testing
		log.debug("/user?username={} called", username);
		final UserProfile readUser = userService.readByUsername(username).orElseThrow(
				() -> new UserNotFoundException(String.format("User with username '%s' not found.", username)));

		// the hashed password should never be given out to the client.
		readUser.setPassword(null);

		return EntityModel.of(readUser, //
				linkTo(methodOn(UserController.class).readUserByUsername(username)).withSelfRel()); //
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
	public EntityModel<UserProfile> readUserByEmail(@RequestParam final String email) {
		// TODO: only find all infos if the authorized user is searching himself
		// TODO: needs testing
		log.debug("/user?email={} called", email);
		final UserProfile readUser = userService.readByEmail(email).orElseThrow(
				() -> new UserNotFoundException(String.format("User with e-mail '%s' not found.", email)));

		// the hashed password should never be given out to the client.
		readUser.setPassword(null);

		return EntityModel.of(readUser, //
				linkTo(methodOn(UserController.class).readUserByEmail(email)).withSelfRel()); //
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
		log.debug("/search/user called with username '{}'", username);
		final List<UserProfile> readUsers = userService.findByUsernameContains(username);

		final List<EntityModel<UserProfile>> entityModels = readUsers.stream().map(user -> {
			user.setPassword(null);
			user.setEmail(null);
			return user;
		}).sorted((user1, user2) -> user1.getUsername().compareTo(user2.getUsername()))
				.map(user -> EntityModel.of(user,
						linkTo(methodOn(UserController.class).readUserById(user.getId())).withRel("readById")))
				.collect(Collectors.toList());

		return CollectionModel.of(entityModels);
	}

}
