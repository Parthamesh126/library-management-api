package com.symphonyai.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.symphonyai.library.entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long>{

	boolean existsByEmail(String email);
	
	boolean existsByEmailAndIdNot(String email, Long id);
	
}
