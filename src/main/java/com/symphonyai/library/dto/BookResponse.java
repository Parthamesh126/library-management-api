package com.symphonyai.library.dto;

public record BookResponse(
	    Long id,
	    String title,
	    String isbn,
	    boolean available,
	    Long authorId,
	    String authorName
) {}
