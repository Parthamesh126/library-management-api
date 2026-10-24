package com.symphonyai.library.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BorrowRequest(
    @NotNull(message = "Book ID is required")
    @Positive(message = "Book ID must be positive")
    Long bookId,

    @NotNull(message = "Member ID is required")
    @Positive(message = "Member ID must be positive")
    Long memberId
) {}