package com.project.secretdiary.repository;

import com.project.secretdiary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query("select d from Diary d join fetch d.member where d.id = :id")
    Optional<Diary> findById(@Param("id") Long id);
}

