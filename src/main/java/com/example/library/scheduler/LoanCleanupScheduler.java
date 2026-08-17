package com.example.library.scheduler;

import com.example.library.service.LoanCleanupService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoanCleanupScheduler {

    private final LoanCleanupService loanCleanupService;

    @Scheduled(cron = "0 0 2 * * *")
    public void runDailyLoanCleanup() {
        loanCleanupService.markOverdueLoansAsLost();
    }
}
