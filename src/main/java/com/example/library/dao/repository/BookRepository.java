package com.example.library.dao.repository;

import com.example.library.dao.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long>, JpaSpecificationExecutor<Book> {
    @EntityGraph(attributePaths = "author")
    Page<Book> findAllByDeletedFalse(Pageable pageable);

    @EntityGraph(attributePaths = "author")
    Optional<Book> findByIdAndDeletedFalse(Long id);

    List<Book> findByPublishedYearBetweenAndDeletedFalse(Integer startYear, Integer endYear);

    List<Book> findByTitleContainingIgnoreCaseAndDeletedFalse(String title);

    @EntityGraph(attributePaths = "author")
    @Query("SELECT b FROM Book b WHERE b.author.name = :authorName AND b.deleted = false")
    List<Book> findByAuthorName(@Param("authorName") String authorName);
}
