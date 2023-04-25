package com.project.secretdiary.repository;

import com.project.secretdiary.dto.response.diary.DiaryResponse;
import com.project.secretdiary.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface DiaryRepositoryCustom {
    Slice<DiaryResponse> findByMemberAndFriend(Long memberId, Long friendId, Pageable pageable);
}
