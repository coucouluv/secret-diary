package com.project.secretdiary.repository;

import com.project.secretdiary.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByUserId(String userId);
    Optional<MemberEntity> findByEmail(String email);
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);
}