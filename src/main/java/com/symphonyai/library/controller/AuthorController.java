package com.symphonyai.library.controller;

import com.symphonyai.library.dto.AuthorRequest;
import com.symphonyai.library.dto.AuthorResponse;
import com.symphonyai.library.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorResponse create(@Valid @RequestBody AuthorRequest request) {
        return authorService.create(request);
    }

    @GetMapping
    public List<AuthorResponse> findAll() {
        return authorService.findAll();
    }

    @GetMapping("/{id}")
    public AuthorResponse findById(@PathVariable Long id) {
        return authorService.findById(id);
    }

    @PutMapping("/{id}")
    public AuthorResponse update(
            @PathVariable Long id,
            @Valid @RequestBody AuthorRequest request) {
        return authorService.update(id, request);
    }
}