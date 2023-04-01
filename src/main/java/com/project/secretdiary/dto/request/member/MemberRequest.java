package com.project.secretdiary.dto.request.member;

import com.project.secretdiary.entity.Member;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequest {
    private Long id;
    @NotBlank(message = "아이디를 입력하세요.")
    @Pattern(regexp = "^[a-zA-Z\\d_]{4,15}$",message = "아이디는 4~15자의 영어,숫자,'_'를 입력하세요.")
    private String userId;
    @NotBlank(message = "이름을 입력하세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z]{2,15}$", message = "이름은 2~15자의 영어 혹은 한글을 입력하세요.")
    private String name;

    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "이메일 형식에 맞게 입력하세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "최소 하나의 숫자, 영어를 포함하는 8자 이상의 비밀번호를 입력하세요.")
    private String password;


    public MemberRequest(Member member) {
        id = member.getId();
        userId = member.getUserId();
        name = member.getName();
        email = member.getEmail();
        password = member.getPassword();

    }

}