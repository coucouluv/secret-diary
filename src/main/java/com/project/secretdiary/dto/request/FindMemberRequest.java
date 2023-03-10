package com.project.secretdiary.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor //기본 생성자 생성
@AllArgsConstructor //모든 필드 값을 파라미터로 받는 생성자 생성
public class FindMemberRequest {

    private String email;
    private String userId;
}