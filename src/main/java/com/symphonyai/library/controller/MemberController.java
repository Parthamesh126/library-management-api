package com.symphonyai.library.controller;

import com.symphonyai.library.dto.BorrowResponse;
import com.symphonyai.library.dto.MemberRequest;
import com.symphonyai.library.dto.MemberResponse;
import com.symphonyai.library.service.BorrowService;
import com.symphonyai.library.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    private final BorrowService borrowService;
    
    public MemberController(
            MemberService memberService,
            BorrowService borrowService) {
        this.memberService = memberService;
        this.borrowService = borrowService;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse create(
            @Valid @RequestBody MemberRequest request) {
        return memberService.create(request);
    }

    @GetMapping
    public List<MemberResponse> findAll() {
        return memberService.findAll();
    }

    @GetMapping("/{id}")
    public MemberResponse findById(@PathVariable Long id) {
        return memberService.findById(id);
    }

    @PutMapping("/{id}")
    public MemberResponse update(
            @PathVariable Long id,
            @Valid @RequestBody MemberRequest request) {
        return memberService.update(id, request);
    }
    
    @GetMapping("/{id}/borrows")
    public List<BorrowResponse> getBorrowHistory(
            @PathVariable Long id) {
        return borrowService.getMemberBorrowHistory(id);
    }
}