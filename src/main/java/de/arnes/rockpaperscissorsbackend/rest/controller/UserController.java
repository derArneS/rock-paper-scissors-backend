package de.arnes.rockpaperscissorsbackend.rest.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import de.arnes.rockpaperscissorsbackend.model.users.UserProfile;
import de.arnes.rockpaperscissorsbackend.model.users.UserService;

/**
 * RestController for user profiles and basic authentication.
 * 
 * @author Arne S.
 *
 */
@RestController
public class UserController {

	@Value("${server.port}")
	private int serverPort;

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Validates the input, then creates the user and saves him in the repository.
	 * 
	 * @param userToCreate
	 * @return Entity model of the {@link UserProfile} created from the input.
	 */
	@PostMapping("/user")
	public ResponseEntity<EntityModel<UserProfile>> createUser(@RequestBody @Valid UserProfile userToCreate) {
		UserProfile savedUser = userService.save(userToCreate);

		// Builds the uri of the newly created user so it can easily be read again.
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().port(serverPort).path("/{id}")
				.buildAndExpand(savedUser.getId()).toUri();

		return ResponseEntity.created(location).body(EntityModel.of(savedUser, //
				linkTo(methodOn(UserController.class).createUser(userToCreate)).withSelfRel())); //
	}

}
