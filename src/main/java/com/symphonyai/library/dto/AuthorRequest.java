package com.symphonyai.library.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthorRequest(
	    @NotBlank(message = "Author name is required")
	    @Size(max = 100, message = "Name must not exceed 100 characters")
	    String name,

	    @Email(message = "Email must be valid")
	    @Size(max = 150)
	    String email
) {}
