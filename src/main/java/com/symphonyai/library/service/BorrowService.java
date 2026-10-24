package com.symphonyai.library.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.symphonyai.library.dto.BorrowRequest;
import com.symphonyai.library.dto.BorrowResponse;

@Component
public interface BorrowService {
	
	public BorrowResponse borrowBook(BorrowRequest request);
	
	public BorrowResponse returnBook(Long borrowId);
	
	public List<BorrowResponse> getMemberBorrowHistory(Long memberId);

}
