package com.project.secretdiary.service;

import com.project.secretdiary.dto.request.member.ProfileRequest;
import com.project.secretdiary.dto.request.member.ChangePasswordRequest;
import com.project.secretdiary.dto.response.member.MemberResponse;
import com.project.secretdiary.dto.response.member.ProfileResponse;
import com.project.secretdiary.entity.Member;
import com.project.secretdiary.exception.PasswordNotMatchException;
import com.project.secretdiary.exception.UserNotFoundException;
import com.project.secretdiary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public MemberResponse findMember(final Long id) {
        Member member = getMember(id);
        return MemberResponse.of(member);
    }

    @Transactional
    public void updatePassword(final Long id, final ChangePasswordRequest changePasswordRequest) {
        Member member = getMember(id);

        if(!passwordEncoder.matches(changePasswordRequest.getPassword(), member.getPassword())) {
            throw new PasswordNotMatchException();
        }

        member.changePassword(passwordEncoder.encode(changePasswordRequest.getChangePassword()));

    }

    @Transactional
    public void updateProfile(final Long id, ProfileRequest profileRequest) {
        Member member = getMember(id);
        member.changeProfile(profileRequest);
    }

    @Transactional(readOnly = true)
    public ProfileResponse findProfile(final Long id) {
        Member member = getMember(id);

        return new ProfileResponse(member.getImage(), member.getStatusMessage());
    }

    private Member getMember(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());
    }

}
