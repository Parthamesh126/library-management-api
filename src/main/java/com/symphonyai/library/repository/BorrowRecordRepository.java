package com.symphonyai.library.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.symphonyai.library.entity.BorrowRecord;
import com.symphonyai.library.enumm.BorrowStatus;

@Repository
public interface BorrowRecordRepository
extends JpaRepository<BorrowRecord, Long> {

	List<BorrowRecord> findByMemberIdOrderByBorrowedAtDesc(Long memberId);
	
	long countByMemberIdAndStatusIn(
	    Long memberId, List<BorrowStatus> statuses);
	
	@Modifying
	@Query("""
	    UPDATE BorrowRecord br
	    SET br.status = com.symphonyai.library.enumm.BorrowStatus.OVERDUE
	    WHERE br.status = com.symphonyai.library.enumm.BorrowStatus.ACTIVE
	      AND br.dueDate < :today
	    """)
	int markOverdueRecords(@Param("today") LocalDate today);
}
