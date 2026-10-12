package com.vendorshield.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI vendorShieldOpenAPI() {
		return new OpenAPI().info(new Info().title("Vendor Management System (CRUD with Audit Trail)")
				.version("1.0.0")
				.description(
						"RESTful enterprise backend engine for managing third-party vendor lifecycles and append-only audit histories.")
				.contact(new Contact().name("VendorShield Engineering Team").email("dev@vendorshield.io"))
				.license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
				.servers(List.of(new Server().url("http://localhost:8080").description("Local Development Server")));
	}
}