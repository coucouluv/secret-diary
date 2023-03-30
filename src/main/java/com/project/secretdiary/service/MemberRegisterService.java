package com.project.secretdiary.service;

import com.project.secretdiary.dto.request.member.MemberRequest;
import com.project.secretdiary.entity.MemberEntity;
import com.project.secretdiary.exception.RegisterFailedException;
import com.project.secretdiary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberRegisterService {
    private final MemberRepository memberRepository;
    private final DuplicateMemberService duplicateMemberService;
    private final PasswordEncoder passwordEncoder;

    public Long join(final MemberRequest memberRequest) {
        validateDuplicateMember(memberRequest);

        MemberEntity member = MemberEntity.builder()
                .userId(memberRequest.getUserId())
                .password(passwordEncoder.encode(memberRequest.getPassword()))
                .name(memberRequest.getName())
                .email(memberRequest.getEmail())
                .build();
        return memberRepository.save(member).getId();

    }

    private void validateDuplicateMember(final MemberRequest memberRequest) {
        if(duplicateMemberService.isDuplicatedUserId(memberRequest.getUserId())) {
            throw new RegisterFailedException("동일한 아이디가 존재합니다.");
        }
        if(duplicateMemberService.isDuplicatedEmail(memberRequest.getEmail())) {
            throw new RegisterFailedException("동일한 이메일이 존재합니다.");
        }

    }
}