package com.project.secretdiary.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)//기본 생성자 막기 위해서
@AllArgsConstructor
@Table(name = "diary")
public class DiaryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="friend_id")
    private Member friend;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 500)
    private String text;
    private String url; 

    @Column(name = "save_date")
    private LocalDateTime saveDate;

    public boolean validateMember(final Long memberId) {
        return member.getId().equals(memberId);
    }

    public boolean validateFriend(final Long friendId) {
        return friend.getId().equals(friendId);
    }

    public void update(final String title, final String text, final String url) {
        this.title = title;
        this.text = text;
        this.url = url;
        this.saveDate = LocalDateTime.now();
    }

}
