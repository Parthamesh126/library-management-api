package com.symphonyai.library.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.symphonyai.library.dto.BookResponse;
import com.symphonyai.library.entity.Author;
import com.symphonyai.library.entity.Book;
import com.symphonyai.library.exception.ResourceNotFoundException;
import com.symphonyai.library.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        Author author = new Author();
        author.setName("Robert Martin");
        author.setEmail("robert@example.com");

        book = new Book();
        book.setTitle("Clean Code");
        book.setIsbn("9780132350884");
        book.setAvailable(true);
        book.setAuthor(author);
    }

    @Test
    void getBookByIdSuccessfully() {
        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        BookResponse response = bookService.findById(1L);

        assertNotNull(response);
        assertEquals("Clean Code", response.title());
        assertEquals("9780132350884", response.isbn());
    }

    @Test
    void getBookByIdShouldFailWhenBookNotFound() {
        when(bookRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> bookService.findById(99L)
        );
    }

    @Test
    void getAllBooksSuccessfully() {

        List<BookResponse> response = bookService.findAll(true);

        assertNotNull(response);
    }

    @Test
    void getAllBooksShouldReturnEmptyListWhenNoBooksExist() {
        List<BookResponse> response = bookService.findAll(true);

        assertNotNull(response);
        assertEquals(0, response.size());
    }

    @Test
    void deleteBookSuccessfully() {
        when(bookRepository.findById(1L))
                .thenReturn(Optional.of(book));

        bookService.delete(1L);

        verify(bookRepository).delete(book);
    }

    @Test
    void deleteBookShouldFailWhenBookNotFound() {
        when(bookRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> bookService.delete(99L)
        );

        verify(bookRepository, never()).delete(book);
    }
}
