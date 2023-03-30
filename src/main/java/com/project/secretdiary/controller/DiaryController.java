package com.project.secretdiary.controller;

import com.project.secretdiary.dto.LoginMember;
import com.project.secretdiary.dto.request.diary.DiaryRequest;
import com.project.secretdiary.dto.request.diary.DiaryUpdateRequest;
import com.project.secretdiary.dto.response.diary.DiaryDetailResponse;
import com.project.secretdiary.dto.response.diary.DiaryPageResponse;
import com.project.secretdiary.dto.response.diary.DiarySaveResponse;
import com.project.secretdiary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
public class DiaryController {
    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<DiarySaveResponse> saveDiary(@CurrentUser LoginMember loginMember,
                                                       final @RequestBody @Valid DiaryRequest diaryRequest) {
        DiarySaveResponse diarySaveResponse = diaryService.saveDiary(loginMember.getId(), diaryRequest);
        return ResponseEntity.ok(diarySaveResponse);
    }

    @PatchMapping
    public ResponseEntity<Void> updateDiary(@CurrentUser LoginMember loginMember,
                                            final @RequestBody @Valid DiaryUpdateRequest diaryUpdateRequest) {
        diaryService.updateDiary(loginMember.getId(), diaryUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiaryDetailResponse> getDiary(@CurrentUser LoginMember loginMember,
                                                        @PathVariable("id") Long diaryId) {
        return ResponseEntity.ok(diaryService.getDiary(loginMember.getId(), diaryId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDiary(@CurrentUser LoginMember loginMember,
                                            @PathVariable("id") Long diaryId) {
        diaryService.deleteDiary(loginMember.getId(), diaryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<DiaryPageResponse> getDiaries(@CurrentUser LoginMember loginMember,
                                                        @PathVariable("id") String friendId, final Pageable pageable) {
        return ResponseEntity.ok(diaryService.getDiaries(loginMember.getId(), friendId, pageable));
    }
}
