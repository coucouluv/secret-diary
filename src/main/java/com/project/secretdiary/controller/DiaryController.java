package com.project.secretdiary.controller;

import com.project.secretdiary.dto.LoginMember;
import com.project.secretdiary.dto.request.diary.DiariesRequest;
import com.project.secretdiary.dto.request.diary.DiaryRequest;
import com.project.secretdiary.dto.request.diary.DiaryUpdateRequest;
import com.project.secretdiary.dto.response.diary.DiaryDetailResponse;
import com.project.secretdiary.dto.response.diary.DiaryPageResponse;
import com.project.secretdiary.dto.response.diary.DiarySaveResponse;
import com.project.secretdiary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diaries")
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<DiarySaveResponse> saveDiary(@CurrentUser final LoginMember loginMember,
                                                       final @RequestBody @Valid DiaryRequest diaryRequest) {
        DiarySaveResponse diarySaveResponse = diaryService.saveDiary(loginMember.getId(), diaryRequest);
        return ResponseEntity.ok(diarySaveResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateDiary(@CurrentUser final LoginMember loginMember,
                                            @PathVariable("id") final Long diaryId,
                                            @RequestBody @Valid final DiaryUpdateRequest diaryUpdateRequest) {
        diaryService.updateDiary(loginMember.getId(), diaryId, diaryUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiaryDetailResponse> findDiary(@CurrentUser final LoginMember loginMember,
                                                        @PathVariable("id") final Long diaryId) {
        return ResponseEntity.ok(diaryService.findDiary(loginMember.getId(), diaryId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiary(@CurrentUser final LoginMember loginMember,
                                            @PathVariable("id") final Long diaryId) {
        diaryService.deleteDiary(loginMember.getId(), diaryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{friendId}/friends")
    public ResponseEntity<DiaryPageResponse> getDiaries(@CurrentUser final LoginMember loginMember,
                                                        @PathVariable("friendId") final Long friendId,
                                                        @ModelAttribute @Valid DiariesRequest diariesRequest) {
        return ResponseEntity.ok(diaryService.getDiaries(loginMember.getId(), friendId, diariesRequest));
    }
}
