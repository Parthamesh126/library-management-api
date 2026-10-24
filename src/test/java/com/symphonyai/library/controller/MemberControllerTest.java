package com.symphonyai.library.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.symphonyai.library.dto.MemberResponse;
import com.symphonyai.library.service.BorrowService;
import com.symphonyai.library.service.MemberService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private BorrowService borrowService;

    @Test
    void getMemberByIdSuccessfully() throws Exception {

        MemberResponse member = new MemberResponse(
                1L,
                "Rahul Sharma",
                "rahul@example.com",
                "7878787888"
        );

        when(memberService.findById(1L))
                .thenReturn(member);

        mockMvc.perform(get("/api/members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rahul Sharma"))
                .andExpect(jsonPath("$.email").value("rahul@example.com"));
    }

    @Test
    void getAllMembersSuccessfully() throws Exception {

        MemberResponse member = new MemberResponse(
                1L,
                "Rahul Sharma",
                "rahul@example.com",
                "7878787888"
        );

        when(memberService.findAll())
                .thenReturn(List.of(member));

        mockMvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Rahul Sharma"))
                .andExpect(jsonPath("$[0].email").value("rahul@example.com"));
    }
}