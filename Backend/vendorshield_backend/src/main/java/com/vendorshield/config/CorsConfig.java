package com.vendorshield.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();

		// Allowed Frontend Origins (Angular dev server)
		config.setAllowedOrigins(List.of("http://localhost:4200", "http://127.0.0.1:4200"));

		// Allowed HTTP Verbs
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

		// Allowed Headers
		config.setAllowedHeaders(
				Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "X-Requested-With"));

		// Exposed Headers (visible to Angular client)
		config.setExposedHeaders(List.of("Location", "Content-Disposition"));

		// Support cookies or authorization credentials
		config.setAllowCredentials(true);

		// Max age of preflight cache (1 hour)
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/api/**", config);

		return new CorsFilter(source);
	}
}