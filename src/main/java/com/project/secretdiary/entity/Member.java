package com.project.secretdiary.entity;

import com.project.secretdiary.dto.request.member.ProfileRequest;
import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)//기본 생성자 막기 위해서
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name="user_id", length = 20)
    private String userId;

    @Column(nullable = false, length = 20)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @Column(name = "status_message", length = 100)
    private String statusMessage;
    private String url;

    @Builder
    public Member(final Long id, final String userId, final String name, final String email,
                  final String password, final String statusMessage, final String url) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.statusMessage = statusMessage;
        this.url = url;
    }

    public void changePassword(final String password) {
        this.password = password;
    }

    public void changeProfile(final ProfileRequest profileRequest) {
        this.statusMessage = profileRequest.getStatusMessage();
        this.url = profileRequest.getUrl();
    }

    public boolean isSameEmail(final String email) {
        return this.email.equals(email);
    }

}
