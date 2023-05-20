package com.project.secretdiary.controller;

import com.project.secretdiary.dto.LoginMember;
import com.project.secretdiary.dto.request.member.ChangePasswordRequest;
import com.project.secretdiary.dto.request.member.ProfileRequest;
import com.project.secretdiary.dto.response.member.MemberResponse;
import com.project.secretdiary.dto.response.member.ProfileResponse;
import com.project.secretdiary.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/info")
    public ResponseEntity<MemberResponse> findMember(@CurrentUser final LoginMember loginMember) {
        return ResponseEntity.ok(memberService.findMember(loginMember.getId()));
    }

    @PostMapping("/info/password")
    public ResponseEntity<Void> updatePassword(@CurrentUser final LoginMember loginMember,
                                               @RequestBody @Valid final ChangePasswordRequest changePasswordRequest) {
        memberService.updatePassword(loginMember.getId(), changePasswordRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/info/profile")
    public ResponseEntity<Void> updateProfile(@CurrentUser final LoginMember loginMember,
                                              @RequestBody @Valid final ProfileRequest profileRequest) {
        memberService.updateProfile(loginMember.getId(), profileRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info/profile")
    public ResponseEntity<ProfileResponse> findProfile(@CurrentUser final LoginMember loginMember) {
        return ResponseEntity.ok(memberService.findProfile(loginMember.getId()));
    }

}
