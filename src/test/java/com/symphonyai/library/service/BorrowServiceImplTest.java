package com.symphonyai.library.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.symphonyai.library.config.LibraryProperties;
import com.symphonyai.library.dto.BorrowRequest;
import com.symphonyai.library.dto.BorrowResponse;
import com.symphonyai.library.entity.Book;
import com.symphonyai.library.entity.BorrowRecord;
import com.symphonyai.library.entity.Member;
import com.symphonyai.library.enumm.BorrowStatus;
import com.symphonyai.library.exception.DuplicateResourceException;
import com.symphonyai.library.exception.ResourceNotFoundException;
import com.symphonyai.library.repository.BookRepository;
import com.symphonyai.library.repository.BorrowRecordRepository;
import com.symphonyai.library.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class BorrowServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BorrowRecordRepository borrowRepository;

    @Mock
    private LibraryProperties libraryProperties;

    @InjectMocks
    private BorrowServiceImpl borrowService;

    private Book book;
    private Member member;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setTitle("Clean Code");
        book.setIsbn("9780132350884");
        book.setAvailable(true);

        member = new Member();
        member.setName("Rahul Sharma");
        member.setEmail("rahul@example.com");
    }


	@Test
	void borrowBookSuccessfully() throws Exception {
	
	    setId(book, 1L);
	    setId(member, 1L);
	
	    when(borrowRepository.markOverdueRecords(any()))
	            .thenReturn(0);
	
	    when(bookRepository.findByIdForUpdate(1L))
	            .thenReturn(Optional.of(book));
	
	    when(memberRepository.findById(1L))
	            .thenReturn(Optional.of(member));
	
	    when(borrowRepository.countByMemberIdAndStatusIn(
	            eq(1L), anyList()))
	            .thenReturn(0L);
	
	    when(libraryProperties.maxActiveBorrowsPerMember())
	            .thenReturn(5);
	
	    when(libraryProperties.loanPeriodDays())
	            .thenReturn(14);
	
	    when(borrowRepository.save(any(BorrowRecord.class)))
	            .thenAnswer(invocation -> invocation.getArgument(0));
	
	    BorrowResponse response = borrowService.borrowBook(
	            new BorrowRequest(1L, 1L));
	
	    assertEquals("Clean Code", response.bookTitle());
	    assertEquals("Rahul Sharma", response.memberName());
	    assertEquals(BorrowStatus.ACTIVE, response.status());
	
	    assertNotNull(response.borrowedAt());
	    assertEquals(
	            response.borrowedAt().plusDays(14),
	            response.dueDate());
	
	    assertFalse(book.isAvailable());
	
	    verify(borrowRepository).save(any(BorrowRecord.class));
	    verify(bookRepository).findByIdForUpdate(1L);
	    verify(memberRepository).findById(1L);
	}
	
	private void setId(Object entity, Long id) throws Exception {
	    Field field = entity.getClass().getDeclaredField("id");
	    field.setAccessible(true);
	    field.set(entity, id);
	}

    @Test
    void borrowBookFailsWhenBookUnavailable() {
        book.setAvailable(false);

        when(borrowRepository.markOverdueRecords(any()))
                .thenReturn(0);

        when(bookRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(book));

        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(member));

        assertThrows(
                DuplicateResourceException.class,
                () -> borrowService.borrowBook(
                        new BorrowRequest(1L, 1L))
        );

        verify(borrowRepository, never())
                .save(any(BorrowRecord.class));
    }
    
    @Test
    void borrowBookShouldFailWhenBookNotFound() {

        when(borrowRepository.markOverdueRecords(any()))
                .thenReturn(0);

        when(bookRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> borrowService.borrowBook(
                        new BorrowRequest(1L, 1L))
        );

        verify(borrowRepository, never())
                .save(any(BorrowRecord.class));
    }

	@Test
	void borrowBookShouldFailWhenBookUnavailable() throws Exception {
	
	    setId(book, 1L);
	    setId(member, 1L);
	    book.setAvailable(false);
	
	    when(borrowRepository.markOverdueRecords(any()))
	            .thenReturn(0);
	
	    when(bookRepository.findByIdForUpdate(1L))
	            .thenReturn(Optional.of(book));
	
	    when(memberRepository.findById(1L))
	            .thenReturn(Optional.of(member));
	
	    assertThrows(
	            DuplicateResourceException.class,
	            () -> borrowService.borrowBook(
	                    new BorrowRequest(1L, 1L))
	    );
	
	    verify(borrowRepository, never())
	            .save(any(BorrowRecord.class));
	}
    
    @Test
    void borrowBookShouldFailWhenMemberNotFound() {

        when(borrowRepository.markOverdueRecords(any()))
                .thenReturn(0);

        when(bookRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(book));

        when(memberRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> borrowService.borrowBook(
                        new BorrowRequest(1L, 1L))
        );

        verify(borrowRepository, never())
                .save(any(BorrowRecord.class));
    }
    
    @Test
    void borrowBookShouldFailWhenMemberReachesBorrowLimit() throws Exception {

        setId(book, 1L);
        setId(member, 1L);

        when(borrowRepository.markOverdueRecords(any()))
                .thenReturn(0);

        when(bookRepository.findByIdForUpdate(1L))
                .thenReturn(Optional.of(book));

        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(member));

        when(borrowRepository.countByMemberIdAndStatusIn(
                eq(1L), anyList()))
                .thenReturn(5L);

        when(libraryProperties.maxActiveBorrowsPerMember())
                .thenReturn(5);

        assertThrows(
                IllegalStateException.class,
                () -> borrowService.borrowBook(
                        new BorrowRequest(1L, 1L))
        );

        verify(borrowRepository, never())
                .save(any(BorrowRecord.class));
    }

    
	
	@Test
	void returnBookSuccessfully() throws Exception {
	    setId(book, 1L);
	    setId(member, 1L);
	
	    book.setAvailable(false);
	
	    BorrowRecord record = new BorrowRecord();
	    setId(record, 1L);
	    record.setBook(book);
	    record.setMember(member);
	    record.setStatus(BorrowStatus.ACTIVE);
	
	    when(borrowRepository.findById(1L))
	            .thenReturn(Optional.of(record));
	
	    when(borrowRepository.save(any(BorrowRecord.class)))
	            .thenAnswer(invocation -> invocation.getArgument(0));
	
	    BorrowResponse response = borrowService.returnBook(1L);
	
	    assertEquals(BorrowStatus.RETURNED, response.status());
	    assertTrue(book.isAvailable());
	
	    verify(borrowRepository).save(record);
	}
	
	@Test
	void returnBookShouldFailWhenBorrowRecordNotFound() {
	    when(borrowRepository.findById(99L))
	            .thenReturn(Optional.empty());
	
	    assertThrows(
	            ResourceNotFoundException.class,
	            () -> borrowService.returnBook(99L)
	    );
	
	    verify(borrowRepository, never())
	            .save(any(BorrowRecord.class));
	}
	
	@Test
	void getMemberBorrowHistorySuccessfully() throws Exception {
	
	    setId(member, 1L);
	    setId(book, 1L);
	
	    BorrowRecord record = new BorrowRecord();
	    setId(record, 1L);
	    record.setBook(book);
	    record.setMember(member);
	    record.setStatus(BorrowStatus.ACTIVE);
	
	    when(memberRepository.existsById(1L))
	            .thenReturn(true);
	
	    when(borrowRepository.findByMemberIdOrderByBorrowedAtDesc(1L))
	            .thenReturn(List.of(record));
	
	    var response = borrowService.getMemberBorrowHistory(1L);
	
	    assertNotNull(response);
	    assertEquals(1, response.size());
	    assertEquals("Clean Code", response.get(0).bookTitle());
	    assertEquals(BorrowStatus.ACTIVE, response.get(0).status());
	}
	
	@Test
	void getMemberBorrowHistoryShouldFailWhenMemberNotFound() {
	
	    assertThrows(
	            ResourceNotFoundException.class,
	            () -> borrowService.getMemberBorrowHistory(99L)
	    );
	}   
    
}