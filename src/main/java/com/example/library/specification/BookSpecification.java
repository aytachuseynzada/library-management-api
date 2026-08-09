package com.example.library.specification;

import com.example.library.dao.entity.Book;
import org.springframework.data.jpa.domain.Specification;

public final class BookSpecification {
    public static Specification<Book> hasTitle(String title) {
        return (root, query, cb) ->
                title == null || title.isBlank()
                        ? cb.conjunction()
                        : cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Book> hasAuthorName(String authorName) {
        return (root, query, cb) ->
                authorName == null || authorName.isBlank()
                        ? cb.conjunction()
                        : cb.equal(root.join("author").get("name"), authorName);
    }

    public static Specification<Book> publishedBetween(Integer startYear, Integer endYear) {
        return (root, query, cb) -> {
            if (startYear == null && endYear == null) {
                return cb.conjunction();
            }
            if (startYear != null && endYear != null) {
                return cb.between(root.get("publishedYear"), startYear, endYear);
            }
            if (startYear != null) {
                return cb.greaterThanOrEqualTo(root.get("publishedYear"), startYear);
            }
            return cb.lessThanOrEqualTo(root.get("publishedYear"), endYear);
        };
    }

    public static Specification<Book> isNotDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get("deleted"));
    }
}
