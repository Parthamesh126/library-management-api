package com.symphonyai.library.service;

import com.symphonyai.library.dto.MemberRequest;
import com.symphonyai.library.dto.MemberResponse;
import com.symphonyai.library.entity.Member;
import com.symphonyai.library.exception.DuplicateResourceException;
import com.symphonyai.library.exception.ResourceNotFoundException;
import com.symphonyai.library.repository.MemberRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public MemberResponse create(MemberRequest request) {
        String email = normalizeEmail(request.email());

        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateResourceException(
                    "Member email already exists: " + email);
        }

        Member member = new Member();
        member.setName(request.name().trim());
        member.setEmail(email);
        member.setPhone(normalizePhone(request.phone()));

        return toResponse(memberRepository.save(member));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberResponse> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MemberResponse findById(Long id) {
        return toResponse(getMember(id));
    }

    @Override
    public MemberResponse update(Long id, MemberRequest request) {
        Member member = getMember(id);
        String email = normalizeEmail(request.email());

        if (memberRepository.existsByEmailAndIdNot(email, id)) {
            throw new DuplicateResourceException(
                    "Member email already exists: " + email);
        }

        member.setName(request.name().trim());
        member.setEmail(email);
        member.setPhone(normalizePhone(request.phone()));

        return toResponse(memberRepository.save(member));
    }

    private Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Member not found with ID: " + id));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private String normalizePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return null;
        }
        return phone.trim();
    }

    private MemberResponse toResponse(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getPhone());
    }
}