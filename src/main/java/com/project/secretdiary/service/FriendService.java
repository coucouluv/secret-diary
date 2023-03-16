package com.project.secretdiary.service;

import com.project.secretdiary.dto.response.FriendResponse;
import com.project.secretdiary.entity.FriendEntity;
import com.project.secretdiary.entity.FriendStatus;
import com.project.secretdiary.entity.MemberEntity;
import com.project.secretdiary.exception.FriendException;
import com.project.secretdiary.exception.UserNotFoundException;
import com.project.secretdiary.repository.FriendRepository;
import com.project.secretdiary.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendService {
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;

    public void applyFriend(final Long id, final String friendUserId) {
        MemberEntity member = getMember(id);
        MemberEntity friend = getFriend(friendUserId);

        if(friendRepository.existsByMemberAndFriend(member,friend)) {
            throw new FriendException("이미 신청한 친구입니다.");
        }

        FriendEntity friendEntity = FriendEntity.builder()
                .member(member)
                .friend(friend)
                .friendStatus(FriendStatus.WAITING)
                .build();
        friendRepository.save(friendEntity);
    }

    public void acceptFriend(final Long id, final String friendUserId) {
        MemberEntity member = getMember(id);
        MemberEntity friend = getFriend(friendUserId);

        FriendEntity friendship = getFriendShip(friend, member);

        friendship.changeFriendStatus();

        FriendEntity friendEntity = FriendEntity.builder()
                .member(member)
                .friend(friend)
                .friendStatus(FriendStatus.COMPLETED)
                .build();
        friendRepository.save(friendEntity);
    }

    @Transactional(readOnly = true)
    public List<FriendResponse> getFriends(final Long id) {
        MemberEntity member = getMember(id);

        List<FriendEntity> friends = friendRepository.findByMember(member);
        List<FriendResponse> friendList = new ArrayList<>();

        for(FriendEntity friend: friends) {
            if(friend.getFriendStatus() == FriendStatus.COMPLETED) {
                FriendResponse friendResponse = new FriendResponse(friend.getFriend());
                friendList.add(friendResponse);
            }
        }
        return friendList;
    }
    @Transactional(readOnly = true)
    public List<FriendResponse> getWaitingFriendList(final Long id) {
        MemberEntity member = getMember(id);

        List<FriendEntity> friends = friendRepository.findByFriend(member);
        List<FriendResponse> waitingList = new ArrayList<>();

        for(FriendEntity friend: friends) {
            if(friend.getFriendStatus() == FriendStatus.WAITING) {
                FriendResponse friendResponse = new FriendResponse(friend.getFriend());
                waitingList.add(friendResponse);
            }
        }
        return waitingList;
    }

    public void denyFriend(final Long id, final String friendUserId) {
        MemberEntity member = getMember(id);
        MemberEntity friend = getFriend(friendUserId);

        FriendEntity friendship = getFriendShip(friend, member);

        friendship.isWaitingStatus();
        friendRepository.delete(friendship);
    }

    public void deleteFriend(final Long id, final String friendUserId) {
        MemberEntity member = getMember(id);
        MemberEntity friend = getFriend(friendUserId);

        FriendEntity friendship = getFriendShip(member,friend);

        FriendEntity anotherFriendship = getFriendShip(friend, member);

        friendship.isCompleteStatus();
        anotherFriendship.isCompleteStatus();

        friendRepository.delete(friendship);
        friendRepository.delete(anotherFriendship);
    }

    private FriendEntity getFriendShip(final MemberEntity member, final  MemberEntity friend) {
        return friendRepository.findByMemberAndFriend(member,friend)
                .orElseThrow(()-> new FriendException());
    }

    private MemberEntity getFriend(final String friendUserId) {
        return memberRepository.findByUserId(friendUserId)
                .orElseThrow(() -> new UserNotFoundException());
    }

    private MemberEntity getMember(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());
    }
}
