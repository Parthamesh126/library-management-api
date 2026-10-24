package com.symphonyai.library.controller;

import com.symphonyai.library.dto.BorrowRequest;
import com.symphonyai.library.dto.BorrowResponse;
import com.symphonyai.library.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {

    private final BorrowService borrowService;

    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BorrowResponse borrowBook(
            @Valid @RequestBody BorrowRequest request) {
        return borrowService.borrowBook(request);
    }

    @PutMapping("/{id}/return")
    public BorrowResponse returnBook(@PathVariable Long id) {
        return borrowService.returnBook(id);
    }

    @GetMapping("/members/{memberId}/history")
    public List<BorrowResponse> getMemberHistory(
            @PathVariable Long memberId) {
        return borrowService.getMemberBorrowHistory(memberId);
    }
}