package de.arnes.rockpaperscissorsbackend.rest.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import de.arnes.rockpaperscissorsbackend.rest.security.authentication.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Arne S.
 *
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

	private final AuthenticationFilter authenticationFilter;

	private final ExceptionHandlerFilter exceptionHandlerFilter;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain configure(final HttpSecurity http) throws Exception {
		log.debug("Security configured");
		return http.authorizeRequests() //
				.antMatchers(HttpMethod.GET, "/user/{id}").authenticated() //
				.antMatchers(HttpMethod.PUT, "/user/{id}").authenticated() //
				.antMatchers(HttpMethod.DELETE, "/user/{id}").authenticated() //
				.antMatchers(HttpMethod.GET, "/search/user").authenticated() //
				.anyRequest().anonymous() //
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //
				.and().addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class) //
				.addFilterBefore(exceptionHandlerFilter, authenticationFilter.getClass()) //
				.csrf().disable() //
				.headers().frameOptions().disable() // for h2 db console
				.and().build();
	}

}
