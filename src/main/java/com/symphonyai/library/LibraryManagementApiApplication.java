package com.symphonyai.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class LibraryManagementApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementApiApplication.class, args);
	}

}
