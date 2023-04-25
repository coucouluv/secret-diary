package com.project.secretdiary.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)//기본 생성자 막기 위해서
@Table(name = "diary")
public class Diary {
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

    private String image;

    @Column(name = "save_date")
    private LocalDateTime saveDate;

    @Builder
    public Diary(final Long id, final Member friend, final Member member, final String title, final String text,
                 final String image, final LocalDateTime saveDate) {
        this.id = id;
        this.friend = friend;
        this.member = member;
        this.title = title;
        this.text = text;
        this.image = image;
        this.saveDate = saveDate;
    }

    public boolean validateMember(final Long memberId) {
        return member.getId().equals(memberId);
    }

    public boolean validateFriend(final Long friendId) {
        return friend.getId().equals(friendId);
    }

    public void update(final String title, final String text, final String image) {
        this.title = title;
        this.text = text;
        this.image = image;
        this.saveDate = LocalDateTime.now();
    }

}
