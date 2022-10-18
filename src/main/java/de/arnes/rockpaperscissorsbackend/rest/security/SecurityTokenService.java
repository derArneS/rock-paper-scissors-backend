package de.arnes.rockpaperscissorsbackend.rest.security;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;

import de.arnes.rockpaperscissorsbackend.model.properties.JwtData;
import de.arnes.rockpaperscissorsbackend.model.rest.authorization.exception.ExpiredTokenException;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Arne S.
 *
 */
@Slf4j
@Service
public class SecurityTokenService {

	private final Algorithm hmac512;

	private final JWTVerifier verifier;

	private final JwtData jwtData;

	public SecurityTokenService(final JwtData jwtData) {
		this.jwtData = jwtData;
		this.hmac512 = Algorithm.HMAC512(jwtData.getSecret());
		this.verifier = JWT.require(hmac512).build();
	}

	/**
	 * Creates and encodes the jwt with the secret. The jwt has an expiration time
	 * set in application.properties.
	 *
	 * @param userDetails
	 * @return created jwt
	 */
	public String generateToken(final UserDetails userDetails) {
		Long expirationInMs = jwtData.getExpirationInMs();
		log.debug("generate jwt with expire-time of '{}'", expirationInMs);
		return JWT.create().withSubject(userDetails.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + expirationInMs)).sign(hmac512);
	}

	/**
	 * Validates the jwt. Throws {@link ExpiredTokenException} if the token is
	 * expired.
	 *
	 * @param token
	 * @return the username hidden in the jwt, if the verification is successful
	 */
	public String validateTokenAndGetUsername(final String token) {
		try {
			return verifier.verify(token).getSubject();
		} catch (final JWTVerificationException verificationEx) {
			log.warn("token invalid: {}", verificationEx.getMessage());
			if (verificationEx.getMessage().startsWith("The Token has expired on"))
				throw new ExpiredTokenException("token invalid: " + verificationEx.getMessage());

			return null;
		}
	}

}
