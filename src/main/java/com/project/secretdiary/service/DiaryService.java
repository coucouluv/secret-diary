package com.project.secretdiary.service;

import com.amazonaws.HttpMethod;
import com.project.secretdiary.dto.request.diary.DiaryRequest;
import com.project.secretdiary.dto.request.diary.DiaryUpdateRequest;
import com.project.secretdiary.dto.response.diary.DiaryDetailResponse;
import com.project.secretdiary.dto.response.diary.DiaryPageResponse;
import com.project.secretdiary.dto.response.diary.DiaryResponse;
import com.project.secretdiary.dto.response.diary.DiarySaveResponse;
import com.project.secretdiary.entity.DiaryEntity;
import com.project.secretdiary.entity.Friend;
import com.project.secretdiary.entity.Member;
import com.project.secretdiary.exception.DiaryException;
import com.project.secretdiary.exception.DiaryNotFoundException;
import com.project.secretdiary.exception.FriendException;
import com.project.secretdiary.exception.UserNotFoundException;
import com.project.secretdiary.repository.DiaryRepository;
import com.project.secretdiary.repository.DiaryRepositoryCustomImpl;
import com.project.secretdiary.repository.FriendRepository;
import com.project.secretdiary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryService {
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final DiaryRepository diaryRepository;
    private final DiaryRepositoryCustomImpl diaryRepositoryCustomImpl;
    private final ImageUploader imageUploader;

    public DiarySaveResponse saveDiary(final Long id, final DiaryRequest diaryRequest) {

        Member member = getMember(id);
        Member friend = memberRepository.findByUserId(diaryRequest.getFriendUserId())
                .orElseThrow(() -> new UserNotFoundException());

        Friend friendship = friendRepository.findByMemberAndFriend(member,friend)
                .orElseThrow(()-> new FriendException("해당 친구가 존재하지 않습니다."));

        friendship.isCompleteStatus();

        DiaryEntity diary = DiaryEntity.builder()
                .title(diaryRequest.getTitle())
                .text(diaryRequest.getText())
                .url(diaryRequest.getUrl())
                .saveDate(LocalDateTime.now())
                .member(member)
                .friend(friend)
                .build();

        return new DiarySaveResponse(diaryRepository.save(diary).getId());
    }

    public void updateDiary(final Long id, final DiaryUpdateRequest diaryUpdateRequest) {

        DiaryEntity diary = getDiary(diaryUpdateRequest.getDiaryId());

        if(!diary.validateMember(id)) {
            throw new DiaryException("다이어리의 멤버가 일치하지 않습니다.");
        }
        diary.update(diaryUpdateRequest.getTitle(), diaryUpdateRequest.getText(), diaryUpdateRequest.getUrl());
    }

    @Transactional(readOnly = true)
    public DiaryPageResponse getDiaries(final Long id, final String friendUserId,
                                        final Pageable pageable) {
        Member member = getMember(id);

        Member friend = memberRepository.findByUserId(friendUserId)
                .orElseThrow(() -> new UserNotFoundException());

        Slice<DiaryResponse> diaryResponses = diaryRepositoryCustomImpl
                .findByMemberAndFriend(member, friend, pageable);
        return toDiaryPageResponse(diaryResponses);
    }

    @Transactional(readOnly = true)
    public DiaryDetailResponse getDiary(final Long id, final Long diaryId) {

        DiaryEntity diary = getDiary(diaryId);

        if(!diary.validateMember(id) && !diary.validateFriend(id)) {
            throw new DiaryException("해당 다이어리 권한이 없습니다.");
        }

        return new DiaryDetailResponse(diary);
    }

    public void deleteDiary(final Long id, final Long diaryId) {
        Member member = getMember(id);
        DiaryEntity diary = getDiary(diaryId);

        if(!diary.validateMember(member.getId())) {
            throw new DiaryException("다이어리의 멤버가 일치하지 않습니다.");
        }
        diaryRepository.delete(diary);
    }

    private String changeUrlToPreSignedUrl(final String url) {
        String preSignedUrl = imageUploader.createPreSignedUrl(url, HttpMethod.GET);
        return preSignedUrl;
    }

    private DiaryPageResponse toDiaryPageResponse(Slice<DiaryResponse> diaryResponses) {
        List<DiaryResponse> content = diaryResponses.getContent();
        for(DiaryResponse diaryResponse: content) {
            diaryResponse.changeUrl(changeUrlToPreSignedUrl(diaryResponse.getUrl()));
        }
        return new DiaryPageResponse(diaryResponses.hasNext(), content);
    }

    private Member getMember(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());
    }

    private DiaryEntity getDiary(final Long id) {
        return diaryRepository.findById(id)
                .orElseThrow(() -> new DiaryNotFoundException());
    }

}

