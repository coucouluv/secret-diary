package com.project.secretdiary.jwt;

import com.project.secretdiary.entity.MemberEntity;
import com.project.secretdiary.exception.UserNotFoundException;
import com.project.secretdiary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        MemberEntity member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("아이디가 존재하지 않습니다."));
        return new CustomUserDetails(member);
    }
}
