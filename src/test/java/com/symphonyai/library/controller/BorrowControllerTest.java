package com.symphonyai.library.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.symphonyai.library.dto.BorrowRequest;
import com.symphonyai.library.dto.BorrowResponse;
import com.symphonyai.library.enumm.BorrowStatus;
import com.symphonyai.library.service.BorrowService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BorrowController.class)
class BorrowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private BorrowService borrowService;

    @Test
    void borrowBookSuccessfully() throws Exception {

        LocalDate today = LocalDate.now();

        BorrowResponse response = new BorrowResponse(
                1L,
                1L,
                "Clean Code",
                1L,
                "Rahul Sharma",
                today,
                today.plusDays(14),
                null,
                BorrowStatus.ACTIVE
        );

        when(borrowService.borrowBook(any(BorrowRequest.class)))
                .thenReturn(response);

        BorrowRequest request = new BorrowRequest(1L, 1L);

        mockMvc.perform(post("/api/borrows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.bookId").value(1))
                .andExpect(jsonPath("$.bookTitle").value("Clean Code"))
                .andExpect(jsonPath("$.memberId").value(1))
                .andExpect(jsonPath("$.memberName").value("Rahul Sharma"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void returnBookSuccessfully() throws Exception {

        LocalDate today = LocalDate.now();

        BorrowResponse response = new BorrowResponse(
                1L,
                1L,
                "Clean Code",
                1L,
                "Rahul Sharma",
                today.minusDays(2),
                today.plusDays(12),
                today,
                BorrowStatus.RETURNED
        );

        when(borrowService.returnBook(1L))
                .thenReturn(response);

        mockMvc.perform(put("/api/borrows/1/return"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.bookTitle").value("Clean Code"))
                .andExpect(jsonPath("$.status").value("RETURNED"))
                .andExpect(jsonPath("$.returnedAt").value(today.toString()));
    }
}