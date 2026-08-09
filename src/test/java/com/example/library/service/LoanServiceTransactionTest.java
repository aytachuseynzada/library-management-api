package com.example.library.service;

import com.example.library.dao.entity.Book;
import com.example.library.dao.entity.Loan;
import com.example.library.dao.entity.Member;
import com.example.library.dao.repository.BookRepository;
import com.example.library.dao.repository.LoanRepository;
import com.example.library.dao.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@ActiveProfiles("test")
public class LoanServiceTransactionTest {

    @Autowired
    private LoanService loanService;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    @MockitoSpyBean
    private MemberRepository memberRepository;

    @Test
    void shouldRollbackTransactionWhenExceptionOccurs() {

        Book book = Book.builder()
                .title("Clean Code")
                .isbn("123456")
                .price(BigDecimal.TEN)
                .publishedYear(2008)
                .build();
        book = bookRepository.save(book);

        Member member = Member.builder()
                .name("Aytac")
                .email("aytac@test.com")
                .fineBalance(BigDecimal.ZERO)
                .build();
        member = memberRepository.save(member);

        Loan loan = Loan.builder()
                .book(book)
                .member(member)
                .borrowDate(LocalDate.now().minusDays(20))
                .dueDate(LocalDate.now().minusDays(5))
                .build();
        loan = loanRepository.save(loan);

        Long loanId = loan.getId();

        doThrow(new RuntimeException("Simulated failure"))
                .when(memberRepository).save(any(Member.class));

        assertThrows(RuntimeException.class,
                () -> loanService.returnBook(loanId));

        Loan savedLoan = loanRepository.findById(loanId).orElseThrow();
        assertNull(savedLoan.getReturnDate());

        Member savedMember = memberRepository.findById(member.getId()).orElseThrow();
        assertEquals(0, BigDecimal.ZERO.compareTo(savedMember.getFineBalance()));
    }
}