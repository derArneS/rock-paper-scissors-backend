package de.arnes.rockpaperscissorsbackend.development.rest.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import de.arnes.rockpaperscissorsbackend.model.properties.JwtData;
import de.arnes.rockpaperscissorsbackend.model.rest.authorization.UserPrincipal;
import de.arnes.rockpaperscissorsbackend.rest.security.SecurityTokenService;

/**
 *
 * @author Arne S.
 *
 */
class SecurityTokenServiceTest {

	private JwtData jwtData;

	private Algorithm hmac512;

	private com.auth0.jwt.JWTVerifier verifier;

	private SecurityTokenService testee;

	@BeforeEach
	public void setup() {
		this.jwtData = new JwtData();
		jwtData.setSecret("test");
		jwtData.setExpirationInMs(123L);

		this.hmac512 = Algorithm.HMAC512(jwtData.getSecret());
		this.verifier = JWT.require(hmac512).build();

		testee = new SecurityTokenService(jwtData);
	}

	@Test
	public void tokenShouldBeGenerated() {
		final UserPrincipal user = new UserPrincipal("Arne", "password", Collections.emptyList());
		final String jwt = testee.generateToken(user);

		final String actual = verifier.verify(jwt).getSubject();
		assertEquals(user.getUsername(), actual);
	}

	@Test
	public void tokenShouldBeValidated() {
		final String expected = "Arne";
		final String jwt = JWT.create().withSubject(expected)
				.withExpiresAt(new Date(System.currentTimeMillis() + jwtData.getExpirationInMs())).sign(hmac512);

		final String actual = testee.validateTokenAndGetUsername(jwt);
		assertEquals(expected, actual);
	}

}
