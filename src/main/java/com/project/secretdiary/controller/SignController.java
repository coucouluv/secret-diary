package com.project.secretdiary.controller;

import com.project.secretdiary.dto.request.member.FindMemberRequest;
import com.project.secretdiary.dto.request.member.SignInRequest;
import com.project.secretdiary.dto.request.TokenRequest;
import com.project.secretdiary.dto.response.member.TokenResponse;
import com.project.secretdiary.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/start")
public class SignController {
    private final SignService signService;

    @PostMapping("/signin")
    public ResponseEntity<TokenResponse> signIn(final @RequestBody @Valid SignInRequest signInRequest) {
        return ResponseEntity.ok(signService.signIn(signInRequest));
    }

    @PostMapping("/signin/password")
    public ResponseEntity<Void> findPassword(final @RequestBody FindMemberRequest findMemberRequest) {
        signService.findPassword(findMemberRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/signin/userid")
    public ResponseEntity<String> findUserId(final @RequestParam("email") String email) {

        return ResponseEntity.ok(signService.findUserId(email));
    }

    @PostMapping("/signout")
    public ResponseEntity<Void> signOut(final @RequestBody @Valid TokenRequest tokenRequest) {
        signService.signOut(tokenRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(final @RequestBody @Valid TokenRequest tokenRequest) {
        return ResponseEntity.ok(signService.reissue(tokenRequest));
    }

}
