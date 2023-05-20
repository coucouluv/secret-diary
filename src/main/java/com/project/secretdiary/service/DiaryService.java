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

    public DiarySaveResponse saveDiary(final Long memberId, final DiaryRequest diaryRequest) {

        Member member = getMember(memberId);
        Member friend = getMember(diaryRequest.getId());

        Friend friendship = friendRepository.findByMemberAndFriend(member,friend)
                .orElseThrow(()-> new FriendNotFoundException());

        friendship.isCompleteStatus();
        Diary diary = diaryRequest.toDiary(member, friend);

        return new DiarySaveResponse(diaryRepository.save(diary).getId());
    }

    public void updateDiary(final Long memberId, final Long diaryId, final DiaryUpdateRequest diaryUpdateRequest) {

        Diary diary = getDiary(diaryId);
        validateMember(diary, memberId);
        diary.update(diaryUpdateRequest.getTitle(), diaryUpdateRequest.getText(), diaryUpdateRequest.getImage());
    }

    @Transactional(readOnly = true)
    public DiaryPageResponse getDiaries(final Long memberId, final Long friendId,
                                        final Pageable pageable) {

        Slice<DiaryResponse> diaryResponses = diaryRepositoryCustomImpl
                .findByMemberAndFriend(memberId, friendId, pageable);
        return new DiaryPageResponse(diaryResponses.hasNext(), diaryResponses.getContent());
    }

    @Transactional(readOnly = true)
    public DiaryDetailResponse findDiary(final Long memberId, final Long diaryId) {

        Diary diary = getDiary(diaryId);
        validateMemberAndFriend(diary, memberId);

        return new DiaryDetailResponse(diary);
    }

    public void deleteDiary(final Long memberId, final Long diaryId) {
        Member member = getMember(memberId);
        Diary diary = getDiary(diaryId);

        validateMember(diary, member.getId());
        diaryRepository.delete(diary);
    }

    private void validateMemberAndFriend(final Diary diary, final Long memberId) {
        if(!diary.isSameMember(memberId) && !diary.isSameFriend(memberId)) {
            throw new NotDiaryMemberException();
        }
    }
    private void validateMember(final Diary diary, final Long memberId) {
        if(!diary.isSameMember(memberId)) {
            throw new NotDiaryMemberException();
        }
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException());
    }

    private Diary getDiary(final Long memberId) {
        return diaryRepository.findById(memberId)
                .orElseThrow(() -> new DiaryNotFoundException());
    }

}

