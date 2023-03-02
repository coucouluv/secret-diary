package com.project.secretdiary.controller;

import com.project.secretdiary.dto.request.MemberRequest;
import com.project.secretdiary.service.MemberRegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/start/register")
public class MemberRegisterController {
    private final MemberRegisterService memberRegisterService;

    @PostMapping("")
    public ResponseEntity<Void> memberRegister(
            final @RequestBody @Valid MemberRequest memberRequest) {
        memberRegisterService.join(memberRequest);
        return ResponseEntity.ok().build();
    }

}