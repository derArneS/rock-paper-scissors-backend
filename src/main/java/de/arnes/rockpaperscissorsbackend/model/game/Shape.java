package de.arnes.rockpaperscissorsbackend.model.game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The three shapes needed for rock-paper-scissors.
 * 
 * @author Arne S.
 *
 */
public enum Shape {

	ROCK,
	PAPER,
	SCISSORS;

	private static final List<Shape> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();

	public static Shape randomShape() {
		return VALUES.get(RANDOM.nextInt(SIZE));
	}

}
