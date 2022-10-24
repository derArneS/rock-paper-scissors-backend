package de.arnes.rockpaperscissorsbackend.spring.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 
 * @author Arne S.
 *
 */
@EnableWebMvc
@Configuration
@ComponentScan
public class Config implements WebMvcConfigurer {

	// enables CORS for all endpoints. Needed especially for CORS Preflight.
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "PUT", "POST", "DELETE", "OPTIONS");
    }

}
