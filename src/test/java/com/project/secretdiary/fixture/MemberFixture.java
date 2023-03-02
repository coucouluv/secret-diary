package com.project.secretdiary.fixture;

import com.project.secretdiary.dto.request.MemberRequest;
import com.project.secretdiary.entity.MemberEntity;

public class MemberFixture {
    private static final Long ID = 1L;
    private static final Long ID_FOR_FRIEND = 2L;
    private static final String USER_ID = "회원";
    private static final String FRIEND_ID = "친구";

    private static final String USER_NAME = "회원이름";
    private static final String FRIEND_NAME = "친구이름";

    private static final String USER_EMAIL = "123@naver.com";
    private static final String FRIEND_EMAIL = "456@naver.com";

    private static final String PASS_WORD = "test";

    public static MemberEntity 회원() {
        return MemberEntity.builder()
                .id(ID)
                .userId(USER_ID)
                .password(PASS_WORD) //비밀번호 암호화
                .name(USER_NAME)
                .email(USER_EMAIL)
                .build();

    }

    public static MemberEntity 친구() {
        return MemberEntity.builder()
                .id(ID_FOR_FRIEND)
                .userId(FRIEND_ID)
                .password(null) //비밀번호 암호화
                .name(FRIEND_NAME)
                .email(FRIEND_EMAIL)
                .build();

    }

    public static MemberRequest 친구_DTO() {
        return MemberRequest.builder()
                .id(ID_FOR_FRIEND)
                .userId(FRIEND_ID)
                .email(FRIEND_EMAIL)
                .name(FRIEND_NAME).build();
    }

}
