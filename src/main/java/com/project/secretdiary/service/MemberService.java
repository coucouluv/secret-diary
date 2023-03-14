package com.project.secretdiary.service;

import com.amazonaws.HttpMethod;
import com.project.secretdiary.dto.request.ProfileRequest;
import com.project.secretdiary.dto.request.ChangePwdRequest;
import com.project.secretdiary.dto.response.MemberResponse;
import com.project.secretdiary.dto.response.ProfileResponse;
import com.project.secretdiary.entity.MemberEntity;
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

    private final ImageUploader imageUploader;
    @Transactional(readOnly = true)
    public MemberResponse getMember(final Long id) {
        MemberEntity member = findMember(id);

        String preSignedUrl = null;
        if(member.getUrl() != null) {
            preSignedUrl = imageUploader.createPreSignedUrl(member.getUrl(), HttpMethod.GET);
        }

        return MemberResponse.of(member, preSignedUrl);
    }

    @Transactional
    public void updatePassword(final Long id, final ChangePwdRequest changePwdRequest) {
        MemberEntity member = findMember(id);

        if(!passwordEncoder.matches(changePwdRequest.getPassword(), member.getPassword())) {
            throw new PasswordNotMatchException();
        }

        member.changePassword(passwordEncoder.encode(changePwdRequest.getChangePassword()));

    }

    @Transactional
    public void updateProfile(final Long id, ProfileRequest profileRequest) {
        MemberEntity member = findMember(id);
        member.changeProfile(profileRequest);
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(final Long id) {
        MemberEntity member = findMember(id);

        return new ProfileResponse(member.getUrl(), member.getStatusMessage());
    }

    private MemberEntity findMember(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());
    }

}
