package com.example.library.controller;

import com.example.library.dto.LoanRequestDto;
import com.example.library.dto.LoanResponseDto;
import com.example.library.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
