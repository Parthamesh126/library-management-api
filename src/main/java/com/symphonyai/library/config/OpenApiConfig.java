package com.symphonyai.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI libraryOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Library Management API")
						.description("REST API for managing authors, books, members, and borrowing records.")
						.version("1.0.0"));
	}

}
