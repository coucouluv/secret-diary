package com.project.secretdiary.service;

import com.project.secretdiary.dto.response.friend.FriendPageResponse;
import com.project.secretdiary.dto.response.friend.WaitingFriendPageResponse;
import com.project.secretdiary.entity.Friend;
import com.project.secretdiary.entity.FriendStatus;
import com.project.secretdiary.entity.Member;
import com.project.secretdiary.exception.FriendException;
import com.project.secretdiary.exception.UserNotFoundException;
import com.project.secretdiary.repository.FriendRepository;
import com.project.secretdiary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendService {
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;

    public void applyFriend(final Long id, final String friendUserId) {
        Member member = getMember(id);
        Member friend = getFriend(friendUserId);

        if(friendRepository.existsByMemberAndFriend(member,friend)) {
            throw new FriendException("이미 신청한 친구입니다.");
        }

        Friend friendship = Friend.builder()
                .member(member)
                .friend(friend)
                .friendStatus(FriendStatus.WAITING)
                .build();
        friendRepository.save(friendship);
    }

    public void acceptFriend(final Long id, final String friendUserId) {
        Member member = getMember(id);
        Member friend = getFriend(friendUserId);

        Friend friendship = getFriendShip(friend, member);

        friendship.changeFriendStatus();

        Friend anotherFriendship = Friend.builder()
                .member(member)
                .friend(friend)
                .friendStatus(FriendStatus.COMPLETED)
                .build();
        friendRepository.save(anotherFriendship);
    }

    @Transactional(readOnly = true)
    public FriendPageResponse getFriends(final Long id, final Pageable pageable) {

        Slice<Member> friends = friendRepository.findFriendWithComplete(id, pageable);
        return FriendPageResponse.from(friends);
    }

    @Transactional(readOnly = true)
    public WaitingFriendPageResponse getFriendsWithWaiting(final Long id, final Pageable pageable) {

        Slice<Member> friends = friendRepository.findFriendWithWaiting(id, pageable);
        return WaitingFriendPageResponse.from(friends);
    }

    public void denyFriend(final Long id, final String friendUserId) {
        Member member = getMember(id);
        Member friend = getFriend(friendUserId);

        Friend friendship = getFriendShip(friend, member);

        friendship.isWaitingStatus();
        friendRepository.delete(friendship);
    }

    public void deleteFriend(final Long id, final String friendUserId) {
        Member member = getMember(id);
        Member friend = getFriend(friendUserId);

        Friend friendship = getFriendShip(member,friend);

        Friend anotherFriendship = getFriendShip(friend, member);

        friendship.isCompleteStatus();
        anotherFriendship.isCompleteStatus();

        friendRepository.delete(friendship);
        friendRepository.delete(anotherFriendship);
    }

    private Friend getFriendShip(final Member member, final Member friend) {
        return friendRepository.findByMemberAndFriend(member,friend)
                .orElseThrow(()-> new FriendException());
    }

    private Member getFriend(final String friendUserId) {
        return memberRepository.findByUserId(friendUserId)
                .orElseThrow(() -> new UserNotFoundException());
    }

    private Member getMember(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());
    }
}
