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

import com.symphonyai.library.dto.BookResponse;
import com.symphonyai.library.service.BookService;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

	@Test
	void getAllBooksSuccessfully() throws Exception {
	
	    BookResponse book = new BookResponse(
	            1L,
	            "Clean Code",
	            "9780132350884",
	            true,
	            1L,
	            "Robert Martin"
	    );
	
	    when(bookService.findAll(true))
	            .thenReturn(List.of(book));
	
	    mockMvc.perform(get("/api/books")
	                    .param("available", "true"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$[0].title").value("Clean Code"))
	            .andExpect(jsonPath("$[0].isbn").value("9780132350884"))
	            .andExpect(jsonPath("$[0].available").value(true));
	}

    @Test
    void getAllBooksShouldReturnEmptyList() throws Exception {
        when(bookService.findAll(true))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
