package com.project.secretdiary.service;

import com.project.secretdiary.dto.request.MemberRequest;
import com.project.secretdiary.entity.MemberEntity;
import com.project.secretdiary.exception.RegisterFailedException;
import com.project.secretdiary.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.project.secretdiary.fixture.MemberFixture.회원;
import static com.project.secretdiary.fixture.MemberFixture.회원가입_요청;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class MemberRegisterServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private DuplicateMemberService duplicateMemberService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberRegisterService memberRegisterService;

    @Test
    @DisplayName("정상적으로 회원 가입이 완료되어야 한다.")
    void 회원가입_완료() {
        //given
        MemberEntity member = 회원();
        MemberRequest memberRequest =  회원가입_요청();

        given(duplicateMemberService.isDuplicatedUserId(anyString()))
                .willReturn(false);
        given(duplicateMemberService.isDuplicatedEmail(anyString()))
                .willReturn(false);
        given(passwordEncoder.encode(anyString()))
                .willReturn(member.getPassword());

        given(memberRepository.save(any()))
                .willReturn(member);
        //when
        Long id = memberRegisterService.join(memberRequest);
        //then
        Assertions.assertThat(member.getId()).isEqualTo(id);

    }
    @Test
    @DisplayName("중복 아이디가 존재할 때 예외 처리 해야한다.")
    void 중복_아이디_존재() {
        //given
        MemberRequest memberRequest =  회원가입_요청();

        given(duplicateMemberService.isDuplicatedUserId(anyString()))
                .willReturn(true);

        //when //then
        Assertions.assertThatThrownBy(() -> memberRegisterService.join(memberRequest))
                .isInstanceOf(RegisterFailedException.class);
    }
    @Test
    @DisplayName("중복 이메일이 존재할 때 예외 처리 해야한다.")
    void 중복_이메일_존재() {
        //given
        MemberRequest memberRequest =  회원가입_요청();

        given(duplicateMemberService.isDuplicatedUserId(anyString()))
                .willReturn(false);
        given(duplicateMemberService.isDuplicatedEmail(anyString()))
                .willReturn(true);
        //when //then
        Assertions.assertThatThrownBy(() -> memberRegisterService.join(memberRequest))
                .isInstanceOf(RegisterFailedException.class);
    }
}
