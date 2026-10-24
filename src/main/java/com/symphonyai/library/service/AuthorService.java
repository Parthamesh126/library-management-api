package com.symphonyai.library.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.symphonyai.library.dto.AuthorRequest;
import com.symphonyai.library.dto.AuthorResponse;

@Component
public interface AuthorService {
	
	public AuthorResponse create(AuthorRequest request);

	public List<AuthorResponse> findAll();
	
	public AuthorResponse findById(Long id);
	
	public AuthorResponse update(Long id, AuthorRequest request);
}
