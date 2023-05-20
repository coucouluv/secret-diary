package com.project.secretdiary.repository;

import com.project.secretdiary.dto.request.diary.DiariesRequest;
import com.project.secretdiary.dto.response.diary.DiaryResponse;
import com.project.secretdiary.entity.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface DiaryRepositoryCustom {
    List<DiaryResponse> findByMemberAndFriend(Long memberId, Long friendId, DiariesRequest diariesRequest);
}
