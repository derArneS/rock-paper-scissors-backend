package de.arnes.rockpaperscissorsbackend.testimplementation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.Test;

import de.arnes.rockpaperscissorsbackend.model.users.User;

/**
 * Simple tests to check if the test-repo is functioning correctly.
 * 
 * @author Arne S.
 *
 */
public class TestUserRepositoryTest {

	private TestUserRepository testee = new TestUserRepository();

	/**
	 * Checks the write and read function.
	 * 
	 * @throws IOException
	 */
	@Test
	public void createUserShouldWriteUser() throws IOException {
		User newUser = new User("TestUser", "testuser@test-mail.test", "testpassword123");
		User createdUser = testee.createUser(newUser);
		User readuser = testee.readUserById(createdUser.getId()).get();

		assertEquals(newUser, readuser);
		assertEquals(readuser, createdUser);
	}

}
