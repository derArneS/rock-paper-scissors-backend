package de.arnes.rockpaperscissorsbackend.model.users.statistics;

import java.util.Optional;

import org.springframework.stereotype.Component;

import de.arnes.rockpaperscissorsbackend.model.game.Shape;
import de.arnes.rockpaperscissorsbackend.model.users.statistics.exception.UserStatisticsAlreadyCreatedException;
import de.arnes.rockpaperscissorsbackend.model.users.statistics.exception.UserStatisticsNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Arne S.
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserStatisticsService {

	private final IUserStatisticsRepository userStatisticsRepository;

	/**
	 * Creates an {@link UserStatistics} entry in the repo. Throws an
	 * {@link UserStatisticsAlreadyCreatedException} if the user already has an
	 * entry in the repo.
	 * 
	 * @param id of the user
	 * @return the created {@link UserStatistics}
	 */
	public UserStatistics create(final String id) {
		// This should never happen, because the user creates an entry only when the
		// user itself is created. If it happens, this method is being called somewhere
		// it shoudn'tÏ
		if (userStatisticsRepository.existsById(id)) {
			throw new UserStatisticsAlreadyCreatedException(
					String.format("The user with the id '%s' already has an entry.", id));
		}

		final UserStatistics stats = new UserStatistics();
		stats.setId(id);

		log.debug("saving stats for user with id '{}'", id);
		return userStatisticsRepository.save(stats);
	}

	public UserStatistics findById(final String id) {
		log.debug("reading stats for user with id '{}'", id);
		return userStatisticsRepository.findById(id)
				.orElseThrow(() -> new UserStatisticsNotFoundException("The user does not have statistics."));
	}

	/**
	 * Start of the building chain for adding shapes to the repo.
	 * 
	 * @param id of the user
	 * @return an instance of {@link UserStatisticsServiceBuilder}
	 */
	public UserStatisticsServiceBuilder start(final String id) {
		log.debug("start of updating the stats");
		final Optional<UserStatistics> statsOptional = userStatisticsRepository.findById(id);

		// This is technically an exception point, but this method shouldn't be called
		// if the user in question is not in the repo.
		UserStatistics stats = statsOptional.get();

		return new UserStatisticsServiceBuilder(stats);
	}

	/**
	 * 
	 * @author Arne S.
	 *
	 */
	public class UserStatisticsServiceBuilder {

		private final UserStatistics stats;

		private UserStatisticsServiceBuilder(UserStatistics stats) {
			this.stats = stats;
		}

		/**
		 * Adds the shape the user picked into the statistics.
		 * 
		 * @param shape of the player
		 * @return instance of the builder
		 */
		public UserStatisticsServiceBuilder addPlayerShape(Shape player) {
			log.debug(String.format("adding player shape '%s' to the stats", player.toString()));
			switch (player) {
			case ROCK:
				stats.addRock();
				break;
			case PAPER:
				stats.addPaper();
				break;
			case SCISSORS:
				stats.addScissors();
				break;
			default:
				break;
			}

			return this;
		}

		/**
		 * Adds the shape the computer picked into the statistics.
		 * 
		 * @param shape of the computer
		 * @return instance of the builder
		 */
		public UserStatisticsServiceBuilder addComputerShape(Shape computer) {
			log.debug(String.format("adding computer shape '%s' to the stats", computer.toString()));
			switch (computer) {
			case ROCK:
				stats.addComputerRock();
				break;
			case PAPER:
				stats.addComputerPaper();
				break;
			case SCISSORS:
				stats.addComputerScissors();
			default:
				break;
			}

			return this;
		}

		/**
		 * Saves the {@link UserStatistics} into the repo.
		 * 
		 * @return the saved {@link UserStatistics}
		 */
		public UserStatistics save() {
			log.debug("saving the stats");
			return userStatisticsRepository.save(stats);
		}
	}
}
