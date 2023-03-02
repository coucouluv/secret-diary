package com.project.secretdiary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.secretdiary.dto.request.MemberRequest;
import com.project.secretdiary.response.ResponseService;
import com.project.secretdiary.service.MemberRegisterService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemberRegisterControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockBean
    private MemberRegisterService memberRegisterService;

    @Test
    @DisplayName("회원 가입 실패 - 잘못된 형식의 아이디 입력")
    void 잘못된_아이디() throws Exception {
        //given
        MemberRequest memberRequest =  MemberRequest.builder()
                .userId("oy123^")
                .name("박지훈")
                .email("789@naver.com")
                .password("asdf12345")
                .build();

        String userDtoJsonString = objectMapper.writeValueAsString(memberRequest);
        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/start/register")
                        .content(userDtoJsonString)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is5xxServerError());
    }
    @Test
    @DisplayName("회원 가입 실패 - 잘못된 형식의 이메일 입력")
    void 잘못된_이메일() throws Exception {
        //given
        MemberRequest memberRequest =  MemberRequest.builder()
                .userId("oy123")
                .name("박지훈")
                .email("789naver.com")
                .password("asdf12345")
                .build();

        String userDtoJsonString = objectMapper.writeValueAsString(memberRequest);
        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/start/register")
                        .content(userDtoJsonString)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is5xxServerError());
    }
    @Test
    @DisplayName("회원 가입 성공")
    void 회원가입_완료() throws Exception {
        //given
        MemberRequest memberRequest =  MemberRequest.builder()
                .userId("oy123")
                .name("박지훈")
                .email("789@naver.com")
                .password("asdf12345")
                .build();

        String userDtoJsonString = objectMapper.writeValueAsString(memberRequest);
        //when //then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/start/register")
                        .content(userDtoJsonString)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

}