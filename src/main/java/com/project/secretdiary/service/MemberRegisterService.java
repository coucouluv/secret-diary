package com.project.secretdiary.service;

import com.project.secretdiary.dto.MemberDto;
import com.project.secretdiary.entity.MemberEntity;
import com.project.secretdiary.exception.RegisterFailedException;
import com.project.secretdiary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberRegisterService {
    private final MemberRepository memberRepository;
    private final DuplicateMemberService duplicateMemberService;
    private final PasswordEncoder passwordEncoder;

    public void join(MemberDto memberDto) {
        validateDuplicateMember(memberDto);
        MemberEntity member = MemberEntity.builder()
                .userId(memberDto.getUserId())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .name(memberDto.getName())
                .email(memberDto.getEmail())
                .build();
        memberRepository.save(member);

    }

    public void validateDuplicateMember(MemberDto memberDto) {
        if(duplicateMemberService.isDuplicatedUserId(memberDto.getUserId())) {
            throw new RegisterFailedException("동일한 아이디가 존재합니다.");
        }
        if(duplicateMemberService.isDuplicatedEmail(memberDto.getEmail())) {
            throw new RegisterFailedException("동일한 이메일이 존재합니다.");
        }

    }
}