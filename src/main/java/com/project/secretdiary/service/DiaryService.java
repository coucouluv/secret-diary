package com.project.secretdiary.service;

import com.project.secretdiary.dto.request.diary.DiaryRequest;
import com.project.secretdiary.dto.request.diary.DiaryUpdateRequest;
import com.project.secretdiary.dto.response.diary.DiaryDetailResponse;
import com.project.secretdiary.dto.response.diary.DiaryPageResponse;
import com.project.secretdiary.dto.response.diary.DiaryResponse;
import com.project.secretdiary.dto.response.diary.DiarySaveResponse;
import com.project.secretdiary.entity.Diary;
import com.project.secretdiary.entity.Friend;
import com.project.secretdiary.entity.Member;
import com.project.secretdiary.exception.*;
import com.project.secretdiary.repository.DiaryRepository;
import com.project.secretdiary.repository.DiaryRepositoryCustomImpl;
import com.project.secretdiary.repository.FriendRepository;
import com.project.secretdiary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryService {
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final DiaryRepository diaryRepository;
    private final DiaryRepositoryCustomImpl diaryRepositoryCustomImpl;

    public DiarySaveResponse saveDiary(final Long id, final DiaryRequest diaryRequest) {

        Member member = getMember(id);
        Member friend = getMember(diaryRequest.getId());

        Friend friendship = friendRepository.findByMemberAndFriend(member,friend)
                .orElseThrow(()-> new FriendNotFoundException());

        friendship.isCompleteStatus();

        Diary diary = diaryRequest.toDiary(member, friend);

        return new DiarySaveResponse(diaryRepository.save(diary).getId());
    }

    public void updateDiary(final Long id, final DiaryUpdateRequest diaryUpdateRequest) {

        Diary diary = getDiary(diaryUpdateRequest.getDiaryId());

        if(!diary.validateMember(id)) {
            throw new DiaryException("다이어리의 멤버가 일치하지 않습니다.");
        }
        diary.update(diaryUpdateRequest.getTitle(), diaryUpdateRequest.getText(), diaryUpdateRequest.getImage());
    }

    @Transactional(readOnly = true)
    public DiaryPageResponse getDiaries(final Long id, final Long friendId,
                                        final Pageable pageable) {

        Slice<DiaryResponse> diaryResponses = diaryRepositoryCustomImpl
                .findByMemberAndFriend(id, friendId, pageable);
        return new DiaryPageResponse(diaryResponses.hasNext(), diaryResponses.getContent());
    }

    @Transactional(readOnly = true)
    public DiaryDetailResponse getDiary(final Long id, final Long diaryId) {

        Diary diary = getDiary(diaryId);

        if(!diary.validateMember(id) && !diary.validateFriend(id)) {
            throw new DiaryException("해당 다이어리 권한이 없습니다.");
        }

        return new DiaryDetailResponse(diary);
    }

    public void deleteDiary(final Long id, final Long diaryId) {
        Member member = getMember(id);
        Diary diary = getDiary(diaryId);

        if(!diary.validateMember(member.getId())) {
            throw new DiaryException("다이어리의 멤버가 일치하지 않습니다.");
        }
        diaryRepository.delete(diary);
    }
    private Member getMember(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());
    }

    private Diary getDiary(final Long id) {
        return diaryRepository.findById(id)
                .orElseThrow(() -> new DiaryNotFoundException());
    }

}

