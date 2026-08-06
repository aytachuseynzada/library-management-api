package com.example.library.dao.repository;

import com.example.library.dao.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Long> {
    @EntityGraph(attributePaths = "books")
    Page<Author> findAllByDeletedFalse(Pageable pageable);

    @EntityGraph(attributePaths = "books")
    Optional<Author> findByIdAndDeletedFalse(Long id);
}
