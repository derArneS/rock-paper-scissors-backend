package de.arnes.rockpaperscissorsbackend.model.game;

import lombok.Data;

/**
 * 
 * @author Arne S.
 *
 * @param <T>
 */
@Data
public class Resource<T> {
	
	private final String name;

	private final T resource;
}
