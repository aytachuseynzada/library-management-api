package com.example.library.mapper;

import com.example.library.dao.entity.Book;
import com.example.library.dao.entity.Loan;
import com.example.library.dao.entity.Member;
import com.example.library.dto.LoanResponseDto;

import java.time.LocalDate;

public interface LoanMapper {
    static Loan mapToEntity(Book book, Member member) {
        return Loan.builder()
                .book(book)
                .member(member)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .returnDate(null)
                .build();
    }

    static LoanResponseDto mapToDto(Loan loan) {
        boolean isOverdue = loan.getReturnDate() == null
                && loan.getDueDate().isBefore(LocalDate.now());

        return LoanResponseDto.builder()
                .id(loan.getId())
                .bookId(loan.getBook().getId())
                .bookTitle(loan.getBook().getTitle())
                .memberId(loan.getMember().getId())
                .memberName(loan.getMember().getName())
                .borrowDate(loan.getBorrowDate())
                .dueDate(loan.getDueDate())
                .returnDate(loan.getReturnDate())
                .overdue(isOverdue)
                .status(loan.getStatus().name())
                .build();
    }
}
