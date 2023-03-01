package com.project.secretdiary.service;

import com.project.secretdiary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DuplicateMemberService {
    private final MemberRepository memberRepository;
    public boolean isDuplicatedUserId(String userId) {
        if(memberRepository.existsByUserId(userId)) {
            return true;
        }
        return false;
    }
    public boolean isDuplicatedEmail(String email) {
        if(memberRepository.existsByEmail(email)) {
            return true;
        }
        return false;
    }
}