package com.symphonyai.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookRequest(
	    @NotBlank(message = "Book title is required")
	    @Size(max = 200)
	    String title,

	    @NotBlank(message = "ISBN is required")
	    @Size(max = 20)
	    String isbn,

	    @NotNull(message = "Author ID is required")
	    Long authorId
) {}