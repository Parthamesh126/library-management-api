package com.symphonyai.library.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.symphonyai.library.entity.Book;

import jakarta.persistence.LockModeType;

public interface BookRepository extends JpaRepository<Book, Long> {
	
	boolean existsByIsbn(String isbn);
	
	boolean existsByIsbnAndIdNot(String isbn, Long id);
	
    List<Book> findAllByAvailable(boolean available);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Book b WHERE b.id = :id")
    Optional<Book> findByIdForUpdate(@Param("id") Long id);

}
