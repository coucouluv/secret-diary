package com.project.secretdiary.service;

import com.project.secretdiary.dto.request.diary.DiariesRequest;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public DiaryPageResponse getDiaries(final Long memberId, final Long friendId, final DiariesRequest diariesRequest) {

        List<DiaryResponse> diaryResponses = diaryRepositoryCustomImpl
                .findByMemberAndFriend(memberId, friendId, diariesRequest);

        Long nextId = diaryResponses.get(diaryResponses.size()-1).getDiaryId();
        boolean hasNext = hasNext(diaryResponses, diariesRequest.getSize());
        return new DiaryPageResponse(hasNext, nextId, diaryResponses);
    }

    @Transactional(readOnly = true)
    public DiaryDetailResponse findDiary(final Long memberId, final Long diaryId) {

        Diary diary = getDiary(diaryId);
        validateMemberAndFriend(diary, memberId);

        return DiaryDetailResponse.of(diary, memberId);
    }

    public void deleteDiary(final Long memberId, final Long diaryId) {
        Member member = getMember(memberId);
        Diary diary = getDiary(diaryId);

        validateMember(diary, member.getId());
        diaryRepository.delete(diary);
    }

    private boolean hasNext(final List<DiaryResponse> diaryResponses, final int size) {
        if(diaryResponses.size() > size) {
            diaryResponses.remove(size);
            return true;
        }
        return false;
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

