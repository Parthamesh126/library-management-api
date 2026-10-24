package com.symphonyai.library.dto;

import java.time.LocalDate;

import com.symphonyai.library.enumm.BorrowStatus;

public record BorrowResponse(
    Long id,
    Long bookId,
    String bookTitle,
    Long memberId,
    String memberName,
    LocalDate borrowedAt,
    LocalDate dueDate,
    LocalDate returnedAt,
    BorrowStatus status
) {}