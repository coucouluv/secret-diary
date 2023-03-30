package com.project.secretdiary.entity;

import com.project.secretdiary.exception.FriendException;
import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)//기본 생성자 막기 위해서
@AllArgsConstructor
@Table(name = "friend")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="friend_id")
    private MemberEntity friend;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private MemberEntity member;

    @Enumerated(EnumType.STRING)
    @Column(name="friend_status", length = 10)
    private FriendStatus friendStatus;

    public void changeFriendStatus() {
        if(this.friendStatus == FriendStatus.COMPLETED) {
            throw new FriendException("이미 친구가 된 멤버입니다.");
        }
        this.friendStatus = FriendStatus.COMPLETED;
    }

    public boolean isWaitingStatus() {
        if(this.friendStatus == FriendStatus.COMPLETED) {
            throw new FriendException("이미 친구가 된 멤버입니다.");
        }
        return true;
    }

    public boolean isCompleteStatus() {
        if(this.friendStatus == FriendStatus.WAITING) {
            throw new FriendException("친구 요청 중인 멤버입니다.");
        }
        return true;
    }
}

