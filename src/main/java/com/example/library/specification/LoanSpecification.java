package com.example.library.specification;

import com.example.library.dao.entity.Book;
import com.example.library.dao.entity.Loan;
import jakarta.persistence.criteria.Fetch;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public final class LoanSpecification {

    private LoanSpecification() {
    }

    public static Specification<Loan> hasMemberId(Long memberId) {
        return (root, query, cb) ->
                memberId == null
                        ? cb.conjunction()
                        : cb.equal(root.get("member").get("id"), memberId);
    }

    public static Specification<Loan> hasBookId(Long bookId) {
        return (root, query, cb) ->
                bookId == null
                        ? cb.conjunction()
                        : cb.equal(root.get("book").get("id"), bookId);
    }

    public static Specification<Loan> isOverdue(Boolean overdue) {
        return (root, query, cb) -> {
            if (overdue == null) {
                return cb.conjunction();
            }
            if (overdue) {
                return cb.and(
                        cb.isNull(root.get("returnDate")),
                        cb.lessThan(root.get("dueDate"), LocalDate.now())
                );
            }
            return cb.or(
                    cb.isNotNull(root.get("returnDate")),
                    cb.greaterThanOrEqualTo(root.get("dueDate"), LocalDate.now())
            );
        };
    }

    public static Specification<Loan> borrowedBetween(LocalDate startDate, LocalDate endDate) {
        return (root, query, cb) -> {
            if (startDate == null && endDate == null) {
                return cb.conjunction();
            }
            if (startDate != null && endDate != null) {
                return cb.between(root.get("borrowDate"), startDate, endDate);
            }
            if (startDate != null) {
                return cb.greaterThanOrEqualTo(root.get("borrowDate"), startDate);
            }
            return cb.lessThanOrEqualTo(root.get("borrowDate"), endDate);
        };
    }
    public static Specification<Loan> fetchBookAndMember() {
        return (root, query, cb) -> {
            if (Long.class != query.getResultType()) {
                query.distinct(true);

                Fetch<Loan, Book> bookFetch = root.fetch("book");
                bookFetch.fetch("author");
                root.fetch("member");
            }
            return cb.conjunction();
        };
    }
}
