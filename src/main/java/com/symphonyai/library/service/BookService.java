package com.symphonyai.library.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.symphonyai.library.dto.BookRequest;
import com.symphonyai.library.dto.BookResponse;

@Component
public interface BookService {
	
	public BookResponse create(BookRequest request);

	public List<BookResponse> findAll(Boolean available);
	
	public BookResponse findById(Long id);
	
	public BookResponse update(Long id, BookRequest request);
	
	public void delete(Long id);
}
