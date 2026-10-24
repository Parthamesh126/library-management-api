package com.symphonyai.library.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.symphonyai.library.dto.BookRequest;
import com.symphonyai.library.dto.BookResponse;
import com.symphonyai.library.entity.Author;
import com.symphonyai.library.entity.Book;
import com.symphonyai.library.exception.DuplicateResourceException;
import com.symphonyai.library.exception.ResourceNotFoundException;
import com.symphonyai.library.repository.AuthorRepository;
import com.symphonyai.library.repository.BookRepository;

@Service
@Transactional
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookServiceImpl(
            BookRepository bookRepository,
            AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public BookResponse create(BookRequest request) {
        String isbn = request.isbn().trim();

        if (bookRepository.existsByIsbn(isbn)) {
            throw new DuplicateResourceException(
                    "Book ISBN already exists: " + isbn);
        }

        Author author = getAuthor(request.authorId());

        Book book = new Book();
        book.setTitle(request.title().trim());
        book.setIsbn(isbn);
        book.setAuthor(author);
        book.setAvailable(true);

        return toResponse(bookRepository.save(book));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> findAll(Boolean available) {
        List<Book> books = available == null
                ? bookRepository.findAll()
                : bookRepository.findAllByAvailable(available);

        return books.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse findById(Long id) {
        return toResponse(getBook(id));
    }

    @Override
    public BookResponse update(Long id, BookRequest request) {
        Book book = getBook(id);
        String isbn = request.isbn().trim();

        if (bookRepository.existsByIsbnAndIdNot(isbn, id)) {
            throw new DuplicateResourceException(
                    "Book ISBN already exists: " + isbn);
        }

        Author author = getAuthor(request.authorId());

        book.setTitle(request.title().trim());
        book.setIsbn(isbn);
        book.setAuthor(author);

        return toResponse(bookRepository.save(book));
    }

    @Override
    public void delete(Long id) {
        Book book = getBook(id);
        bookRepository.delete(book);
    }

    private Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Book not found with ID: " + id));
    }

    private Author getAuthor(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Author not found with ID: " + id));
    }

    private BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.isAvailable(),
                book.getAuthor().getId(),
                book.getAuthor().getName());
    }
}