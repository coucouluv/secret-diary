package com.project.secretdiary.fixture;

import com.project.secretdiary.dto.request.member.MemberRequest;
import com.project.secretdiary.entity.MemberEntity;

public class MemberFixture {
    public static final Long 아이디 = 1L;
    public static final Long 친구_아이디 = 2L;
    public static final String 회원_로그인_아이디 = "userid";
    public static final String 친구_로그인_아이디 = "friendid";

    public static final String 회원_이름 = "회원이름";
    public static final String 친구_이름 = "친구이름";

    public static final String 회원_이메일 = "123@naver.com";
    public static final String 친구_이메일 = "456@naver.com";

    public static final String 비밀번호 = "test1234";

    public static final String 상태메세지 = "message";
    public static final String 이미지 = "url";

    public static MemberEntity 회원() {
        return MemberEntity.builder()
                .id(아이디)
                .userId(회원_로그인_아이디)
                .password(비밀번호)
                .name(회원_이름)
                .email(회원_이메일)
                .statusMessage(상태메세지)
                .url(이미지)
                .build();

    }

    public static MemberEntity 친구() {
        return MemberEntity.builder()
                .id(친구_아이디)
                .userId(친구_로그인_아이디)
                .password(null)
                .name(친구_이름)
                .email(친구_이메일)
                .build();

    }

    public static MemberRequest 회원가입_요청() {
        return new MemberRequest(아이디, 회원_로그인_아이디, 회원_이름, 회원_이메일, 비밀번호);
    }

    public static MemberRequest 친구_DTO() {
        return new MemberRequest(친구_아이디, 친구_로그인_아이디, 친구_이름, 친구_이메일, null);

    }

}
