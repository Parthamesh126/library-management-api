package com.symphonyai.library.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.symphonyai.library.dto.MemberRequest;
import com.symphonyai.library.dto.MemberResponse;

@Component
public interface MemberService {
	
	public MemberResponse create(MemberRequest request);
	
	public List<MemberResponse> findAll();
	
	public MemberResponse findById(Long id);
	
	public MemberResponse update(Long id, MemberRequest request);

}
