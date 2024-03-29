package com.project.secretdiary.service;

import com.project.secretdiary.dto.request.member.FindMemberRequest;
import com.project.secretdiary.dto.request.MailRequest;
import com.project.secretdiary.dto.request.member.SignInRequest;
import com.project.secretdiary.dto.request.TokenRequest;
import com.project.secretdiary.dto.request.member.TokenReissueRequest;
import com.project.secretdiary.entity.Member;
import com.project.secretdiary.exception.*;
import com.project.secretdiary.jwt.JwtTokenProvider;
import com.project.secretdiary.dto.response.member.TokenResponse;
import com.project.secretdiary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate redisTemplate;
    private final MailService mailService;
    private static final int PASSWORD_LENGTH = 10;
    private static final String EMAIL_TITLE_FOR_PASSWORD = "[너와 나의 비밀 일기장] 임시 비밀번호 안내 메일입니다.";

    public TokenResponse signIn(final SignInRequest signInRequest) {

        Member member = memberRepository.findByUserId(signInRequest.getUserId())
                .orElseThrow(UserNotFoundException::new);

        if(!passwordEncoder.matches(signInRequest.getPassword(), member.getPassword())) {
            throw new PasswordNotMatchException();
        }

        TokenResponse tokenResponse = jwtTokenProvider.createToken(String.valueOf(member.getId()));

        redisTemplate.opsForValue()
                .set(String.valueOf(member.getId()),
                        tokenResponse.getRefreshToken(),
                        tokenResponse.getReExpireTime(), TimeUnit.MILLISECONDS);

        return tokenResponse;
    }

    @Transactional
    public void findPassword(final FindMemberRequest findMemberRequest) {
        Member member = memberRepository.findByUserId(findMemberRequest.getUserId())
                .orElseThrow(UserNotFoundException::new);

        if(!member.isSameEmail(findMemberRequest.getEmail())) {
            throw new EmailNotMatchException();
        }

        String tmpPassword = createPassword();
        member.changePassword(passwordEncoder.encode(tmpPassword));

        MailRequest mailRequest = new MailRequest(findMemberRequest.getEmail(), EMAIL_TITLE_FOR_PASSWORD,
                findMemberRequest.getUserId() +" 님의 임시 비밀번호는 " + tmpPassword + " 입니다.");

        mailService.sendMail(mailRequest);
    }

    private String createPassword() {
        char[] letters = new char[] {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E',
                'F','G','H','I','J','K','L','M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        StringBuilder tmpPassword= new StringBuilder();
        int index;
        for(int i = 0; i < PASSWORD_LENGTH; i++) {
            index = (int) (letters.length * Math.random());
            tmpPassword.append(letters[index]);
        }
        return tmpPassword.toString();
    }

    public String findUserId(final String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        return member.getUserId();
    }

    public TokenResponse reissue(final TokenReissueRequest tokenRequest) {
        validateToken(tokenRequest.getRefreshToken());
        String id = jwtTokenProvider.getId(tokenRequest.getRefreshToken());
        String refreshToken = (String)redisTemplate.opsForValue().get(id);

        if(!refreshToken.equals(tokenRequest.getRefreshToken())) {
            throw new InvalidTokenException();
        }

        Member member = memberRepository.findById(Long.valueOf(id))
                .orElseThrow(UserNotFoundException::new);

        TokenResponse tokenResponse = jwtTokenProvider.createToken(id);

        redisTemplate.opsForValue()
                .set(String.valueOf(member.getId()),
                        tokenResponse.getRefreshToken(),
                        tokenResponse.getReExpireTime(), TimeUnit.MILLISECONDS);
        return tokenResponse;
    }

    public void signOut(final TokenRequest tokenRequest) {
        validateToken(tokenRequest.getAccessToken());

        Long id = Long.valueOf(jwtTokenProvider.getId(tokenRequest.getAccessToken()));

        if(redisTemplate.opsForValue().get(String.valueOf(id)) != null) {
            redisTemplate.delete(String.valueOf(id));
        }

        Long expiration = jwtTokenProvider.getExpiration(tokenRequest.getAccessToken());
        redisTemplate.opsForValue()
                .set(tokenRequest.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);
    }

    private void validateToken(final String token) {
        if(!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException();
        }
    }
}