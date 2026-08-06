package com.example.library.service;

import com.example.library.dao.entity.Book;
import com.example.library.dao.entity.Loan;
import com.example.library.dao.entity.Member;
import com.example.library.dao.repository.BookRepository;
import com.example.library.dao.repository.LoanRepository;
import com.example.library.dao.repository.MemberRepository;
import com.example.library.dto.LoanResponseDto;
import com.example.library.dto.LoanRequestDto;
import com.example.library.exception.BookNotFoundException;
import com.example.library.exception.LoanNotFoundException;
import com.example.library.exception.MemberNotFoundException;
import com.example.library.mapper.LoanMapper;
import com.example.library.specification.LoanSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public LoanResponseDto borrowBook(LoanRequestDto dto) {
        Book book = bookRepository.findByIdAndDeletedFalse(dto.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + dto.getBookId()));

        Member member = memberRepository.findByIdAndDeletedFalse(dto.getMemberId())
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + dto.getMemberId()));

        Loan loan = LoanMapper.mapToEntity(book, member);
        Loan saved = loanRepository.save(loan);

        return LoanMapper.mapToDto(saved);
    }

    public List<LoanResponseDto> getCurrentLoansByMember(Long memberId) {
        return loanRepository.findByMemberIdAndReturnDateIsNull(memberId)
                .stream()
                .map(LoanMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<LoanResponseDto> getLoanHistoryByBook(Long bookId) {
        return loanRepository.findByBookIdOrderByBorrowDateDesc(bookId)
                .stream()
                .map(LoanMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<LoanResponseDto> getOverdueLoans() {
        return loanRepository.findOverdueLoans(LocalDate.now())
                .stream()
                .map(LoanMapper::mapToDto)
                .collect(Collectors.toList());
    }
    public List<LoanResponseDto> searchLoans(Long memberId, Long bookId, Boolean overdue, LocalDate startDate, LocalDate endDate) {

        Specification<Loan> spec = Specification.where(LoanSpecification.hasMemberId(memberId))
                .and(LoanSpecification.hasBookId(bookId))
                .and(LoanSpecification.isOverdue(overdue))
                .and(LoanSpecification.borrowedBetween(startDate, endDate))
                .and(LoanSpecification.fetchBookAndMember());

        List<Loan> loans = loanRepository.findAll(spec);

        return loans.stream()
                .map(LoanMapper::mapToDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public LoanResponseDto returnBook(Long loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found with id: " + loanId));

        if (loan.getReturnDate() != null) {
            throw new IllegalStateException("This book has already been returned");
        }

        loan.setReturnDate(LocalDate.now());
        loanRepository.save(loan);

        if (loan.getDueDate().isBefore(LocalDate.now())) {
            Member member = loan.getMember();
            member.setFineBalance(member.getFineBalance().add(BigDecimal.valueOf(5)));
            memberRepository.save(member);
        }

        return LoanMapper.mapToDto(loan);
    }
}
