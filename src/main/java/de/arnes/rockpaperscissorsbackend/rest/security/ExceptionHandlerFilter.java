package de.arnes.rockpaperscissorsbackend.rest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.gson.Gson;

import de.arnes.rockpaperscissorsbackend.model.rest.authorization.exception.BadUsernameException;
import de.arnes.rockpaperscissorsbackend.model.rest.authorization.exception.ExpiredTokenException;
import de.arnes.rockpaperscissorsbackend.rest.advice.ErrorMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * This filter is first in the chain. If a filter later in lane throws an
 * exception it will be thrown until here. If its an
 * {@link ExpiredTokenException} it will be catched and a
 * {@link HttpStatus}.BAD_REQUEST together with the message of the exception
 * responded.
 *
 * @author Arne S.
 *
 */
@Slf4j
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

	@Override
	public void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (final ExpiredTokenException | BadUsernameException ex) {
			log.debug("{} in filterchain catched", ex.getClass());
			final ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());

			response.setStatus(HttpStatus.BAD_REQUEST.value());
			response.getWriter().write(new Gson().toJson(errorMessage));
		}
	}

}
