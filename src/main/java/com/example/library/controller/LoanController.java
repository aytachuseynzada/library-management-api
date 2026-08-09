package com.example.library.controller;

import com.example.library.dto.LoanRequestDto;
import com.example.library.dto.LoanResponseDto;
import com.example.library.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Tag(
        name = "Loan",
        description = "Book borrowing management APIs"
)
@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @Operation(
            summary = "Borrow a book",
            description = "Creates a new loan record for a member borrowing a book"
    )
    @PostMapping
    @ResponseStatus(CREATED)
    public LoanResponseDto borrowBook(@Valid @RequestBody LoanRequestDto dto) {
        return loanService.borrowBook(dto);
    }

    @Operation(
            summary = "Get current loans by member",
            description = "Returns books currently borrowed (not yet returned) by a member"
    )
    @GetMapping("/member/{memberId}/current")
    public List<LoanResponseDto> getCurrentLoansByMember(@PathVariable Long memberId) {
        return loanService.getCurrentLoansByMember(memberId);
    }

    @Operation(
            summary = "Get loan history for a book",
            description = "Returns the borrowing history of a specific book"
    )
    @GetMapping("/book/{bookId}/history")
    public List<LoanResponseDto> getLoanHistoryByBook(@PathVariable Long bookId) {
        return loanService.getLoanHistoryByBook(bookId);
    }

    @Operation(
            summary = "Get overdue loans",
            description = "Returns all loans that are overdue (not returned and past due date)"
    )
    @GetMapping("/overdue")
    public List<LoanResponseDto> getOverdueLoans() {
        return loanService.getOverdueLoans();
    }
    @Operation(
            summary = "Search loans",
            description = "Dynamically search loans by member, book, overdue status, and borrow date range"
    )
    @GetMapping("/search")
    public List<LoanResponseDto> searchLoans(
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) Boolean overdue,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return loanService.searchLoans(memberId, bookId, overdue, startDate, endDate);
    }
    @Operation(
            summary = "Return a book",
            description = "Marks a loan as returned; applies a fine if returned late"
    )
    @PutMapping("/{id}/return")
    public LoanResponseDto returnBook(@PathVariable Long id) {
        return loanService.returnBook(id);
    }
}
