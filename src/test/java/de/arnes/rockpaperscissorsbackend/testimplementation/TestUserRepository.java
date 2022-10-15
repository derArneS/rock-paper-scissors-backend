package de.arnes.rockpaperscissorsbackend.testimplementation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import de.arnes.rockpaperscissorsbackend.model.users.IUserRepository;
import de.arnes.rockpaperscissorsbackend.model.users.User;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

/**
 * 
 * @author Arne S.
 *
 */
@Log4j2
public class TestUserRepository implements IUserRepository {

	private static final String USER_REPO = "./src/test/resources/users.txt";

	@Override
	public User createUser(@NonNull User userToCreate) {
		// Set the new id. This prevents the client from setting the id
		userToCreate.setId(UUID.randomUUID().toString());

		try (FileWriter writer = new FileWriter(USER_REPO, true);
				BufferedWriter bufferedWriter = new BufferedWriter(writer);
				PrintWriter printer = new PrintWriter(bufferedWriter)) {
			printer.println(userToCreate.getId() //
					+ "," //
					+ userToCreate.getUsername() //
					+ "," //
					+ userToCreate.getEmail() //
					+ "," //
					+ userToCreate.getPassword());
		} catch (IOException e) {
			log.error(e);
		}

		return userToCreate;
	}

	@Override
	public Optional<User> readUserById(@NonNull String id) {
		try (Stream<String> stream = Files.lines(Paths.get(USER_REPO))) {
			return stream.map(userLine -> {
				// splits the line into the user fields and creates a new User-object
				String[] parts = userLine.split(",");
				return new User(parts[0], parts[1], parts[2], parts[3]);
			}).filter(user -> {
				// filters the users by the id field. Only if the id is the same the user
				// will be returned
				return id.equals(user.getId());
			}).findFirst();
		} catch (IOException e) {
			log.error(e);
		}

		return Optional.empty();
	}

	@Override
	public User updateUserById(@NonNull String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User deleteUserbyId(@NonNull String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
