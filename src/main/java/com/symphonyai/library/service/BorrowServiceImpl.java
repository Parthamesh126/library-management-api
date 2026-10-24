package com.symphonyai.library.service;

import com.symphonyai.library.config.LibraryProperties;
import com.symphonyai.library.dto.BorrowRequest;
import com.symphonyai.library.dto.BorrowResponse;
import com.symphonyai.library.entity.*;
import com.symphonyai.library.enumm.BorrowStatus;
import com.symphonyai.library.exception.DuplicateResourceException;
import com.symphonyai.library.exception.ResourceNotFoundException;
import com.symphonyai.library.repository.BookRepository;
import com.symphonyai.library.repository.BorrowRecordRepository;
import com.symphonyai.library.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class BorrowServiceImpl implements BorrowService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BorrowRecordRepository borrowRepository;
    private final LibraryProperties libraryProperties;

    public BorrowServiceImpl(
            BookRepository bookRepository,
            MemberRepository memberRepository,
            BorrowRecordRepository borrowRepository,
            LibraryProperties libraryProperties) {
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
        this.borrowRepository = borrowRepository;
        this.libraryProperties = libraryProperties;
    }

    @Override
    public BorrowResponse borrowBook(BorrowRequest request) {
        LocalDate today = LocalDate.now();

        borrowRepository.markOverdueRecords(today);

        Book book = bookRepository.findByIdForUpdate(request.bookId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Book not found with ID: " + request.bookId()));

        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Member not found with ID: " + request.memberId()));

        if (!book.isAvailable()) {
            throw new DuplicateResourceException(
                    "Book is already borrowed: " + book.getTitle());
        }

        long activeBorrows = borrowRepository.countByMemberIdAndStatusIn(
                member.getId(),
                List.of(BorrowStatus.ACTIVE, BorrowStatus.OVERDUE));

        if (activeBorrows
                >= libraryProperties.maxActiveBorrowsPerMember()) {
            throw new IllegalStateException(
                    "Member has reached the maximum active borrowing limit");
        }

        int loanDays = libraryProperties.loanPeriodDays();

        if (loanDays <= 0) {
            throw new IllegalStateException(
                    "Library loan period must be greater than zero");
        }

        BorrowRecord record = new BorrowRecord();
        record.setBook(book);
        record.setMember(member);
        record.setBorrowedAt(today);
        record.setDueDate(today.plusDays(loanDays));
        record.setStatus(BorrowStatus.ACTIVE);

        book.setAvailable(false);

        BorrowRecord saved = borrowRepository.save(record);

        return toResponse(saved);
    }

    @Override
    public BorrowResponse returnBook(Long borrowId) {
        borrowRepository.markOverdueRecords(LocalDate.now());

        BorrowRecord record = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Borrow record not found with ID: " + borrowId));

        if (record.getStatus() == BorrowStatus.RETURNED) {
            throw new IllegalStateException(
                    "This book has already been returned");
        }

        record.setReturnedAt(LocalDate.now());
        record.setStatus(BorrowStatus.RETURNED);
        record.getBook().setAvailable(true);

        return toResponse(borrowRepository.save(record));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BorrowResponse> getMemberBorrowHistory(Long memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new ResourceNotFoundException(
                    "Member not found with ID: " + memberId);
        }

        borrowRepository.markOverdueRecords(LocalDate.now());

        return borrowRepository
                .findByMemberIdOrderByBorrowedAtDesc(memberId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private BorrowResponse toResponse(BorrowRecord record) {
        return new BorrowResponse(
                record.getId(),
                record.getBook().getId(),
                record.getBook().getTitle(),
                record.getMember().getId(),
                record.getMember().getName(),
                record.getBorrowedAt(),
                record.getDueDate(),
                record.getReturnedAt(),
                record.getStatus());
    }
}