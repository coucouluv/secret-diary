package com.project.secretdiary.service;

import com.project.secretdiary.dto.request.member.FindMemberRequest;
import com.project.secretdiary.dto.request.MailRequest;
import com.project.secretdiary.dto.request.member.SignInRequest;
import com.project.secretdiary.dto.request.TokenRequest;
import com.project.secretdiary.entity.MemberEntity;
import com.project.secretdiary.exception.CustomJwtException;
import com.project.secretdiary.exception.EmailNotMatchException;
import com.project.secretdiary.exception.PasswordNotMatchException;
import com.project.secretdiary.exception.UserNotFoundException;
import com.project.secretdiary.jwt.JwtTokenProvider;
import com.project.secretdiary.dto.response.member.TokenResponse;
import com.project.secretdiary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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

    public TokenResponse signIn(final SignInRequest signInRequest) {

        MemberEntity member = memberRepository.findByUserId(signInRequest.getUserId())
                .orElseThrow(UserNotFoundException::new);

        if(!passwordEncoder.matches(signInRequest.getPassword(), member.getPassword())) {
            throw new PasswordNotMatchException();
        }

        TokenResponse tokenResponse = jwtTokenProvider.createToken(String.valueOf(member.getId()));

        redisTemplate.opsForValue()
                .set(member.getUserId(),
                        tokenResponse.getRefreshToken(),
                        tokenResponse.getReExpireTime(), TimeUnit.MILLISECONDS);

        return tokenResponse;
    }

    @Transactional
    public void findPassword(final FindMemberRequest findMemberRequest) {
        MemberEntity member = memberRepository.findByUserId(findMemberRequest.getUserId())
                .orElseThrow(UserNotFoundException::new);

        if(!member.isSameEmail(findMemberRequest.getEmail())) {
            throw new EmailNotMatchException();
        }

        String tmpPassword = createPassword();
        member.changePassword(passwordEncoder.encode(tmpPassword));

        MailRequest mailRequest = new MailRequest();
        mailRequest.setAddress(findMemberRequest.getEmail());
        mailRequest.setTitle("[너와 나의 비밀 일기장] 임시 비밀번호 안내 메일입니다.");
        mailRequest.setMessage("안녕하세요. [너와 나의 일기장] 에서 임시 비밀번호를 알려 드립니다.\n" +
                findMemberRequest.getUserId() +" 님의 임시 비밀번호는 " + tmpPassword + " 입니다." );

        mailService.sendMail(mailRequest);
    }

    private String createPassword() {
        char[] letters = new char[] {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E',
                'F','G','H','I','J','K','L','M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        StringBuilder tmpPassword= new StringBuilder();
        int index = 0;
        for(int i = 0; i < PASSWORD_LENGTH; i++) {
            index = (int) (letters.length * Math.random());
            tmpPassword.append(letters[index]);
        }
        return tmpPassword.toString();
    }

    public String findUserId(final String email) {
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        return member.getUserId();
    }

    public TokenResponse reissue(final TokenRequest tokenRequest) {
        validateToken(tokenRequest.getRefreshToken());
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenRequest.getAccessToken());
        String refreshToken = (String)redisTemplate.opsForValue().get(authentication.getName());

        if(ObjectUtils.isEmpty(refreshToken)) {
            throw new CustomJwtException("잘못된 요청입니다.");
        }
        if(!refreshToken.equals(tokenRequest.getRefreshToken())) {
            throw new CustomJwtException("refresh token 정보가 올바르지 않습니다.");
        }
        MemberEntity member = memberRepository.findByUserId(authentication.getName())
                .orElseThrow(UserNotFoundException::new);

        TokenResponse tokenResponse = jwtTokenProvider.createToken(String.valueOf(member.getId()));

        redisTemplate.opsForValue()
                .set(member.getUserId(),
                        tokenResponse.getRefreshToken(),
                        tokenResponse.getReExpireTime(), TimeUnit.MILLISECONDS);
        return tokenResponse;
    }

    public void signOut(final TokenRequest tokenRequest) {
        validateToken(tokenRequest.getAccessToken());

        Authentication authentication = jwtTokenProvider.getAuthentication(tokenRequest.getAccessToken());

        if(redisTemplate.opsForValue().get(authentication.getName()) != null) {
            redisTemplate.delete(authentication.getName());
        }

        Long expiration = jwtTokenProvider.getExpiration(tokenRequest.getAccessToken());
        redisTemplate.opsForValue()
                .set(tokenRequest.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);
    }

    private void validateToken(final String token) {
        if(!jwtTokenProvider.validateToken(token)) {
            throw new CustomJwtException("유효하지 않은 token 입니다.");
        }
    }
}