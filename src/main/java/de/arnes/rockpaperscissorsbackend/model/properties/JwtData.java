package de.arnes.rockpaperscissorsbackend.model.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Arne S.
 *
 */
@Data
@Component
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("app.jwt")
public class JwtData {

	/**
	 * JwtSecret for encoding the jwt
	 */
	private String secret;

	/**
	 * Time in ms until the token is expired
	 */
	private Long expirationInMs;

}
