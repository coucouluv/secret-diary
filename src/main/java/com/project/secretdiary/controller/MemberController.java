package com.project.secretdiary.controller;

import com.project.secretdiary.dto.LoginMember;
import com.project.secretdiary.dto.request.ChangePwdRequest;
import com.project.secretdiary.dto.request.ProfileRequest;
import com.project.secretdiary.dto.response.MemberResponse;
import com.project.secretdiary.dto.response.ProfileResponse;
import com.project.secretdiary.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/info")
    public ResponseEntity<MemberResponse> getMember(@CurrentUser LoginMember loginMember) {
        return ResponseEntity.ok(memberService.getMember(loginMember.getId()));
    }

    @PostMapping("/info/password")
    public ResponseEntity<Void> updatePassword(@CurrentUser LoginMember loginMember,
                                               @RequestBody final ChangePwdRequest changePwdRequest) {
        memberService.updatePassword(loginMember.getId(), changePwdRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/info/profile")
    public ResponseEntity<Void> updateProfile(@CurrentUser LoginMember loginMember,
                                              @RequestBody final ProfileRequest profileRequest) {
        memberService.updateProfile(loginMember.getId(), profileRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info/profile")
    public ResponseEntity<ProfileResponse> getProfile(@CurrentUser LoginMember loginMember) {
        return ResponseEntity.ok(memberService.getProfile(loginMember.getId()));
    }

    //멤버 탈퇴

}
