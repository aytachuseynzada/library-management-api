package com.example.library.service;

import com.example.library.dao.entity.Loan;
import com.example.library.dao.entity.Member;
import com.example.library.dao.repository.LoanRepository;
import com.example.library.dao.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanCleanupService {

    private final LoanRepository loanRepository;
    private final MemberRepository memberRepository;

    private static final int LOST_THRESHOLD_DAYS = 90;
    private static final BigDecimal LOST_BOOK_FINE = BigDecimal.valueOf(50);

    public void markOverdueLoansAsLost() {

        LocalDate cutoffDate = LocalDate.now().minusDays(LOST_THRESHOLD_DAYS);
        List<Loan> overdueLoans = loanRepository.findLoansOverdueBeyond(cutoffDate);

        log.info("Found {} loans overdue beyond {} days", overdueLoans.size(), LOST_THRESHOLD_DAYS);

        for (Loan loan : overdueLoans) {
            loan.setStatus(com.example.library.enums.LoanStatus.LOST);
            loanRepository.save(loan);

            Member member = loan.getMember();
            member.setFineBalance(member.getFineBalance().add(LOST_BOOK_FINE));
            memberRepository.save(member);

            log.info("Loan id={} marked as LOST, member id={} fined {}",
                    loan.getId(), member.getId(), LOST_BOOK_FINE);
        }
    }
}
