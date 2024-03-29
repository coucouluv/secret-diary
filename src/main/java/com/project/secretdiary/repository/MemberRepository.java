package com.project.secretdiary.repository;

import com.project.secretdiary.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserId(String userId);
    Optional<Member> findByEmail(String email);
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);
}