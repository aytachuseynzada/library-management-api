package com.example.library.dao.repository;

import com.example.library.dao.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {
    List<Loan> findByMemberIdAndReturnDateIsNull(Long memberId);

    List<Loan> findByBookIdOrderByBorrowDateDesc(Long bookId);

    @Query("SELECT l FROM Loan l WHERE l.returnDate IS NULL AND l.dueDate < :today")
    List<Loan> findOverdueLoans(@Param("today") LocalDate today);
}
