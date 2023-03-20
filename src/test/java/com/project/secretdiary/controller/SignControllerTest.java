package com.project.secretdiary.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.secretdiary.dto.request.FindMemberRequest;
import com.project.secretdiary.dto.request.SignInRequest;
import com.project.secretdiary.dto.request.TokenRequest;
import com.project.secretdiary.dto.response.TokenResponse;
import com.project.secretdiary.exception.CustomJwtException;
import com.project.secretdiary.exception.EmailNotMatchException;
import com.project.secretdiary.exception.PasswordNotMatchException;
import com.project.secretdiary.exception.UserNotFoundException;
import com.project.secretdiary.service.SignService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.project.secretdiary.fixture.MemberFixture.*;
import static com.project.secretdiary.fixture.TokenFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SignController.class)
@WithMockUser
class SignControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private SignService signService;

    @Test
    public void 로그인_성공() throws Exception {
        //given
        SignInRequest signInRequest = new SignInRequest(회원_로그인_아이디, 비밀번호);
        TokenResponse tokenResponse = 토큰_응답();
        given(signService.signIn(signInRequest))
                .willReturn(tokenResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/start/signin")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf().asHeader())
                        .content(objectMapper.writeValueAsString(signInRequest))
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void 회원정보가_존재하지_않을_때_로그인_실패() throws Exception {
        //given
        SignInRequest signInRequest = new SignInRequest(회원_로그인_아이디, 비밀번호);

        given(signService.signIn(any()))
                .willThrow(new UserNotFoundException());
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/start/signin")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf().asHeader())
                        .content(objectMapper.writeValueAsString(signInRequest))
        );
        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void 비밀번호를_잘못_입력했을시_로그인_실패() throws Exception {
        //given
        SignInRequest signInRequest = new SignInRequest(회원_로그인_아이디, 비밀번호);

        given(signService.signIn(any()))
                .willThrow(new PasswordNotMatchException());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/start/signin")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf().asHeader())
                        .content(objectMapper.writeValueAsString(signInRequest))
        );
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void 비밀번호_찾기_성공() throws Exception {
        //given
        FindMemberRequest findMemberRequest = new FindMemberRequest(회원_이메일, 회원_로그인_아이디);

        doNothing().when(signService).findPassword(findMemberRequest);
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/start/signin/password")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf().asHeader())
                        .content(objectMapper.writeValueAsString(findMemberRequest))
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }
    
    @Test
    public void 이메일이_일치하지_않을_떄_비밀번호_찾기_실패() throws Exception {
        //given
        FindMemberRequest findMemberRequest = new FindMemberRequest(회원_이메일, 회원_로그인_아이디);

        doThrow(new EmailNotMatchException()).when(signService).findPassword(any());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/start/signin/password")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf().asHeader())
                        .content(objectMapper.writeValueAsString(findMemberRequest))
        );
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void 아이디_찾기_성공() throws Exception {
        //given
        given(signService.findUserId(회원_이메일))
                .willReturn(회원_로그인_아이디);

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/start/signin/userid")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf().asHeader())
                        .param("email", 회원_이메일)
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }
    
    @Test
    public void 이메일이_존재하지_않을_때_아이디_찾기_실패() throws Exception {
        //given
        given(signService.findUserId(회원_이메일))
                .willThrow(new UserNotFoundException());
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/start/signin/userid")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf().asHeader())
                        .param("email", 회원_이메일)
        );
        //then
        resultActions.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void 로그아웃_성공() throws Exception {
        //given
        TokenRequest tokenRequest = new TokenRequest(액세스_토큰, 리프레시_토큰);
        doNothing().when(signService).signOut(tokenRequest);
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/start/signout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf().asHeader())
                        .content(objectMapper.writeValueAsString(tokenRequest))
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void 액세스_토큰이_유효하지_않을_때_로그아웃_실패() throws Exception {
        //given
        TokenRequest tokenRequest = new TokenRequest(액세스_토큰, 리프레시_토큰);
        doThrow(new CustomJwtException("유효하지 않은 token 입니다.")).when(signService).signOut(any());
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/start/signout")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf().asHeader())
                        .content(objectMapper.writeValueAsString(tokenRequest))
        );
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void 토큰_재발급_성공() throws Exception {
        //given
        TokenRequest tokenRequest = new TokenRequest(액세스_토큰, 리프레시_토큰);
        TokenResponse tokenResponse = 토큰_응답();
        given(signService.reissue(tokenRequest))
                .willReturn(tokenResponse);
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/start/reissue")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf().asHeader())
                        .content(objectMapper.writeValueAsString(tokenRequest))
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }


}