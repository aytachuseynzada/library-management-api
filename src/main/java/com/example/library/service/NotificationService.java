package com.example.library.service;

import com.example.library.dao.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    @Async
    public void sendLostBookNotification(Member member, String bookTitle) {

        log.info("Sending lost-book notification to {} (thread: {})",
                member.getEmail(), Thread.currentThread().getName());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("Notification sent to {}: Your loan for '{}' has been marked as LOST, a fine has been applied to your account.",
                member.getEmail(), bookTitle);
    }
}
