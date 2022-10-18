package de.arnes.rockpaperscissorsbackend.rest.security.authentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import de.arnes.rockpaperscissorsbackend.rest.security.SecurityTokenService;
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
public class AuthenticationFilter extends OncePerRequestFilter {

	private final SecurityTokenService tokenService;

	private final AuthenticationUserDetailsService authenticationUserDetailsService;

	/*
	 * Checks if the client added a Bearer header for authentication. If not or if
	 * the username in the token is not in the repository, the authorization fails.
	 * In the @SecurityConfig is defined which endpoints need authorization.
	 */
	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain chain) throws ServletException, IOException {
		log.debug("AuthenticationFilter is called");

		// look for Bearer auth header
		final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
			// validation failed
			log.debug("no header Authorization provided");
			chain.doFilter(request, response);
			return;
		}

		final String token = header.substring(7);
		final String username = tokenService.validateTokenAndGetUsername(token);
		if (username == null) {
			// validation failed
			log.debug("username for authorization can't be found in the repository");
			chain.doFilter(request, response);
			return;
		}

		// set user details on spring security context
		final UserDetails userDetails = authenticationUserDetailsService.loadUserByUsername(username);
		final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
				null, userDetails.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// continue with authenticated user
		log.debug("user authentication successful");
		chain.doFilter(request, response);
	}

}
