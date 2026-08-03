package com.example.library.service;

import com.example.library.dao.entity.Book;
import com.example.library.dao.entity.Loan;
import com.example.library.dao.entity.Member;
import com.example.library.dao.repository.BookRepository;
import com.example.library.dao.repository.LoanRepository;
import com.example.library.dao.repository.MemberRepository;
import com.example.library.dto.LoanRequestDto;
import com.example.library.dto.LoanResponseDto;
import com.example.library.exception.BookNotFoundException;
import com.example.library.exception.LoanNotFoundException;
import com.example.library.exception.MemberNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private LoanService loanService;

    @Test
    void shouldBorrowBookSuccessfully() {

        Book book = new Book();
        book.setId(1L);
        book.setTitle("1984");

        Member member = new Member();
        member.setId(1L);
        member.setName("Aytac");

        LoanRequestDto dto = new LoanRequestDto();
        dto.setBookId(1L);
        dto.setMemberId(1L);

        Loan savedLoan = Loan.builder()
                .id(1L)
                .book(book)
                .member(member)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .build();

        when(bookRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(book));
        when(memberRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(member));
        when(loanRepository.save(any(Loan.class)))
                .thenReturn(savedLoan);

        LoanResponseDto result = loanService.borrowBook(dto);

        assertEquals("1984", result.getBookTitle());
        assertEquals("Aytac", result.getMemberName());
        assertFalse(result.isOverdue());

        verify(loanRepository).save(any(Loan.class));
    }

    @Test
    void shouldThrowBookNotFoundExceptionWhenBorrowingNonExistingBook() {

        LoanRequestDto dto = new LoanRequestDto();
        dto.setBookId(1L);
        dto.setMemberId(1L);

        when(bookRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                BookNotFoundException.class,
                () -> loanService.borrowBook(dto)
        );
    }

    @Test
    void shouldThrowMemberNotFoundExceptionWhenBorrowingWithNonExistingMember() {

        Book book = new Book();
        book.setId(1L);

        LoanRequestDto dto = new LoanRequestDto();
        dto.setBookId(1L);
        dto.setMemberId(1L);

        when(bookRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.of(book));
        when(memberRepository.findByIdAndDeletedFalse(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                MemberNotFoundException.class,
                () -> loanService.borrowBook(dto)
        );
    }

    @Test
    void shouldReturnCurrentLoansByMember() {

        Book book = new Book();
        book.setId(1L);
        book.setTitle("1984");

        Member member = new Member();
        member.setId(1L);
        member.setName("Aytac");

        Loan loan = Loan.builder()
                .id(1L)
                .book(book)
                .member(member)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .build();

        when(loanRepository.findByMemberIdAndReturnDateIsNull(1L))
                .thenReturn(List.of(loan));

        List<LoanResponseDto> result = loanService.getCurrentLoansByMember(1L);

        assertEquals(1, result.size());
        assertEquals("1984", result.get(0).getBookTitle());

        verify(loanRepository).findByMemberIdAndReturnDateIsNull(1L);
    }

    @Test
    void shouldReturnLoanHistoryByBook() {

        Book book = new Book();
        book.setId(1L);
        book.setTitle("1984");

        Member member = new Member();
        member.setId(1L);
        member.setName("Aytac");

        Loan loan = Loan.builder()
                .id(1L)
                .book(book)
                .member(member)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .build();

        when(loanRepository.findByBookIdOrderByBorrowDateDesc(1L))
                .thenReturn(List.of(loan));

        List<LoanResponseDto> result = loanService.getLoanHistoryByBook(1L);

        assertEquals(1, result.size());
        assertEquals("Aytac", result.get(0).getMemberName());

        verify(loanRepository).findByBookIdOrderByBorrowDateDesc(1L);
    }

    @Test
    void shouldReturnOverdueLoans() {

        Book book = new Book();
        book.setId(1L);
        book.setTitle("1984");

        Member member = new Member();
        member.setId(1L);
        member.setName("Aytac");

        Loan overdueLoan = Loan.builder()
                .id(1L)
                .book(book)
                .member(member)
                .borrowDate(LocalDate.now().minusDays(20))
                .dueDate(LocalDate.now().minusDays(6))
                .returnDate(null)
                .build();

        when(loanRepository.findOverdueLoans(any(LocalDate.class)))
                .thenReturn(List.of(overdueLoan));

        List<LoanResponseDto> result = loanService.getOverdueLoans();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isOverdue());

        verify(loanRepository).findOverdueLoans(any(LocalDate.class));
    }
    @Test
    void shouldReturnBookOnTimeWithoutFine() {

        Book book = new Book();
        book.setId(1L);
        book.setTitle("1984");

        Member member = new Member();
        member.setId(1L);
        member.setName("Aytac");
        member.setFineBalance(BigDecimal.ZERO);

        Loan loan = Loan.builder()
                .id(1L)
                .book(book)
                .member(member)
                .borrowDate(LocalDate.now().minusDays(5))
                .dueDate(LocalDate.now().plusDays(9))
                .returnDate(null)
                .build();

        when(loanRepository.findById(1L))
                .thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class)))
                .thenReturn(loan);

        LoanResponseDto result = loanService.returnBook(1L);

        assertEquals(LocalDate.now(), result.getReturnDate());
        assertEquals(BigDecimal.ZERO, member.getFineBalance());

        verify(loanRepository).save(loan);
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void shouldReturnBookLateAndApplyFine() {

        Book book = new Book();
        book.setId(1L);
        book.setTitle("1984");

        Member member = new Member();
        member.setId(1L);
        member.setName("Aytac");
        member.setFineBalance(BigDecimal.ZERO);

        Loan loan = Loan.builder()
                .id(1L)
                .book(book)
                .member(member)
                .borrowDate(LocalDate.now().minusDays(20))
                .dueDate(LocalDate.now().minusDays(6))
                .returnDate(null)
                .build();

        when(loanRepository.findById(1L))
                .thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class)))
                .thenReturn(loan);

        LoanResponseDto result = loanService.returnBook(1L);

        assertEquals(LocalDate.now(), result.getReturnDate());
        assertEquals(BigDecimal.valueOf(5), member.getFineBalance());

        verify(loanRepository).save(loan);
        verify(memberRepository).save(member);
    }

    @Test
    void shouldThrowLoanNotFoundExceptionWhenReturningNonExistingLoan() {

        when(loanRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                LoanNotFoundException.class,
                () -> loanService.returnBook(1L)
        );
    }

    @Test
    void shouldThrowIllegalStateExceptionWhenBookAlreadyReturned() {

        Loan loan = Loan.builder()
                .id(1L)
                .returnDate(LocalDate.now().minusDays(1))
                .build();

        when(loanRepository.findById(1L))
                .thenReturn(Optional.of(loan));

        assertThrows(
                IllegalStateException.class,
                () -> loanService.returnBook(1L)
        );
    }
}
