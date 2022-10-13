package de.arnes.rockpaperscissorsbackend.model.game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import lombok.Getter;

/**
 * The three shapes needed for rock-paper-scissors.
 * 
 * @author Arne S.
 *
 */
public enum Shape {

	ROCK("SCISSORS"),
	PAPER("ROCK"),
	SCISSORS("PAPER");

	@Getter
	private List<String> winsAgainst;
	
	Shape(String... shapes) {
		winsAgainst = Arrays.asList(shapes);
	}
	
	private static final List<Shape> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
	private static final int SIZE = VALUES.size();
	private static final Random RANDOM = new Random();

	public static Shape randomShape() {
		return VALUES.get(RANDOM.nextInt(SIZE));
	}

}
