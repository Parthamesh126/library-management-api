package com.symphonyai.library.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.symphonyai.library.dto.AuthorResponse;
import com.symphonyai.library.service.AuthorService;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthorService authorService;

    @Test
    void getAuthorByIdSuccessfully() throws Exception {
        AuthorResponse author = new AuthorResponse(
                1L,
                "Robert Martin",
                "robert@example.com"
        );

        when(authorService.findById(1L))
                .thenReturn(author);

        mockMvc.perform(get("/api/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Robert Martin"))
                .andExpect(jsonPath("$.email").value("robert@example.com"));
    }

    @Test
    void getAllAuthorsSuccessfully() throws Exception {
        AuthorResponse author = new AuthorResponse(
                1L,
                "Robert Martin",
                "robert@example.com"
        );

        when(authorService.findAll())
                .thenReturn(List.of(author));

        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Robert Martin"))
                .andExpect(jsonPath("$[0].email").value("robert@example.com"));
    }
}