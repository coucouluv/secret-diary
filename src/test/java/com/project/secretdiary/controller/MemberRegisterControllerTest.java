package com.project.secretdiary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.secretdiary.dto.request.member.MemberRequest;
import com.project.secretdiary.exception.RegisterFailedException;
import com.project.secretdiary.service.MemberRegisterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.project.secretdiary.fixture.MemberFixture.회원가입_요청;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MemberRegisterController.class)
class MemberRegisterControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    protected MemberRegisterService memberRegisterService;

    @Test
    @WithMockUser
    public void 회원가입_성공() throws Exception {
        //given
        MemberRequest memberRequest = 회원가입_요청();

        given(memberRegisterService.join(memberRequest))
                .willReturn(memberRequest.getId());

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/start/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf().asHeader())
                        .content(objectMapper.writeValueAsString(memberRequest))
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }
    
    @Test
    @WithMockUser
    public void 회원가입_정보_미입력시_회원가입_실패() throws Exception {
        //given
        MemberRequest memberRequest = new MemberRequest();
        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/start/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf().asHeader())
                        .content(objectMapper.writeValueAsString(memberRequest))
        );
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @WithMockUser
    public void 동일한_아이디가_존재할_때_회원가입_실패() throws Exception {
        //given
        MemberRequest memberRequest = 회원가입_요청();

        given(memberRegisterService.join(any()))
                .willThrow(new RegisterFailedException("동일한 아이디가 존재합니다."));

        //when
        ResultActions resultActions = mockMvc.perform(
                post("/api/start/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf().asHeader())
                        .content(objectMapper.writeValueAsString(memberRequest))
        );
        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }
}