package com.example.library.dao.repository;

import com.example.library.dao.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Page<Member> findAllByDeletedFalse(Pageable pageable);
    Optional<Member> findByIdAndDeletedFalse(Long id);

    Optional<Member> findByEmailAndDeletedFalse(String email);
}
