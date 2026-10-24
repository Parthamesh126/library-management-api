package com.symphonyai.library.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.symphonyai.library.dto.AuthorRequest;
import com.symphonyai.library.dto.AuthorResponse;
import com.symphonyai.library.entity.Author;
import com.symphonyai.library.exception.DuplicateResourceException;
import com.symphonyai.library.exception.ResourceNotFoundException;
import com.symphonyai.library.repository.AuthorRepository;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService{
	
	private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

	@Override
	public AuthorResponse create(AuthorRequest request) {

		String email = normalizeEmail(request.email());
		
		if(email != null && authorRepository.existsByEmail(email)) {
			throw new DuplicateResourceException(
					"Author email already exists: " + email);
		}
		
		Author author = new Author();
		author.setName(request.name().trim());
		author.setEmail(email);
		
		return toResponse(authorRepository.save(author));
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<AuthorResponse> findAll() {
		return authorRepository.findAll()
				.stream()
				.map(this::toResponse)
				.toList();
	}
	
	@Override
	@Transactional(readOnly = true)
	public AuthorResponse findById(Long id) {
		return toResponse(getAuthor(id));
	}
	
	@Override
	public AuthorResponse update(Long id, AuthorRequest request) {
		Author author = getAuthor(id);
		String email = normalizeEmail(request.email());
		
		if (email != null
                && authorRepository.existsByEmailAndIdNot(email, id)) {
            throw new DuplicateResourceException(
                    "Author email already exists: " + email);
        }
		
		author.setName(request.name().trim());
        author.setEmail(email);

        return toResponse(authorRepository.save(author));
    }
	
	private Author getAuthor(Long id) {
		return authorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Author not found with ID: " + id));
	}
	
	private String normalizeEmail(String email) {
		
		if(email == null || email.isBlank()) return null;
		
		return email.trim().toLowerCase();
		
	}
	
	private AuthorResponse toResponse(Author author) {
        return new AuthorResponse(
                author.getId(),
                author.getName(),
                author.getEmail());
    }

}
