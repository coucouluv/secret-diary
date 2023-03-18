package com.project.secretdiary.repository;

import com.project.secretdiary.dto.response.DiaryResponse;
import com.project.secretdiary.entity.MemberEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface DiaryRepositoryCustom {
    Slice<DiaryResponse> findByMemberAndFriend(MemberEntity member, MemberEntity friend, Pageable pageable);
}
