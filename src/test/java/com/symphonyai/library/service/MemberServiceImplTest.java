package com.symphonyai.library.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.symphonyai.library.dto.MemberResponse;
import com.symphonyai.library.entity.Member;
import com.symphonyai.library.exception.ResourceNotFoundException;
import com.symphonyai.library.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member();
        member.setName("Rahul Sharma");
        member.setEmail("rahul@example.com");
    }

    @Test
    void getMemberByIdSuccessfully() {
        when(memberRepository.findById(1L))
                .thenReturn(Optional.of(member));

        MemberResponse response = memberService.findById(1L);

        assertNotNull(response);
        assertEquals("Rahul Sharma", response.name());
        assertEquals("rahul@example.com", response.email());
    }

    @Test
    void getMemberByIdShouldFailWhenMemberNotFound() {
        when(memberRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> memberService.findById(99L)
        );
    }

    @Test
    void getAllMembersSuccessfully() {
        when(memberRepository.findAll())
                .thenReturn(List.of(member));

        List<MemberResponse> response = memberService.findAll();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Rahul Sharma", response.get(0).name());
    }

    @Test
    void getAllMembersShouldReturnEmptyListWhenNoMembersExist() {
        when(memberRepository.findAll())
                .thenReturn(List.of());

        List<MemberResponse> response = memberService.findAll();

        assertNotNull(response);
        assertEquals(0, response.size());
    }
}