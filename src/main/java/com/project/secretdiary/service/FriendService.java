package com.project.secretdiary.service;

import com.project.secretdiary.dto.request.MailRequest;
import com.project.secretdiary.dto.response.friend.FriendPageResponse;
import com.project.secretdiary.dto.response.friend.WaitingFriendPageResponse;
import com.project.secretdiary.entity.Friend;
import com.project.secretdiary.entity.FriendStatus;
import com.project.secretdiary.entity.Member;
import com.project.secretdiary.exception.ExistingUserException;
import com.project.secretdiary.exception.InvalidFriendStatusException;
import com.project.secretdiary.exception.FriendNotFoundException;
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
    private final MailService mailService;

    public void apply(final Long id, final String friendUserId) {
        Member member = getMember(id);
        Member friend = getFriend(friendUserId);

        if(friendRepository.existsByMemberAndFriend(member,friend)) {
            throw new InvalidFriendStatusException("이미 신청한 친구입니다.");
        }
        friendRepository.save(applyFriend(member, friend));
    }

    public void accept(final Long id, final String friendUserId) {
        Member member = getMember(id);
        Member friend = getFriend(friendUserId);

        Friend friendship = getFriendShip(friend, member);
        friendship.changeFriendStatus();
        friendRepository.save(acceptFriend(member, friend));
    }

    @Transactional(readOnly = true)
    public FriendPageResponse findFriends(final Long id, final Pageable pageable) {

        Slice<Member> friends = friendRepository.findFriendWithComplete(id, pageable);
        return FriendPageResponse.from(friends);
    }

    @Transactional(readOnly = true)
    public WaitingFriendPageResponse findFriendsWithWaiting(final Long id, final Pageable pageable) {

        Slice<Member> friends = friendRepository.findFriendWithWaiting(id, pageable);
        return WaitingFriendPageResponse.from(friends);
    }

    public void deny(final Long id, final String friendUserId) {
        Member member = getMember(id);
        Member friend = getFriend(friendUserId);
        Friend friendship = getFriendShip(friend, member);

        friendship.isWaitingStatus();
        friendRepository.delete(friendship);
    }

    public void delete(final Long id, final String friendUserId) {
        Member member = getMember(id);
        Member friend = getFriend(friendUserId);

        Friend friendship = getFriendShip(member,friend);
        Friend anotherFriendship = getFriendShip(friend, member);

        friendship.isCompleteStatus();
        anotherFriendship.isCompleteStatus();

        friendRepository.delete(friendship);
        friendRepository.delete(anotherFriendship);
    }

    public void invite(final Long id, final String email) {
        Member member = getMember(id);
        existsMember(email);
        MailRequest mailRequest = new MailRequest(email, "[너와 나의 비밀 일기장] " + member.getUserId()+
                " 님이 보내는 초대장", "secret-diary.site 로 초대합니다.");
        mailService.sendMail(mailRequest);
    }

    private void existsMember(final String email) {
        if(memberRepository.existsByEmail(email)) {
            throw new ExistingUserException();
        }
    }

    private Friend acceptFriend(final Member member, final Member friend) {
        return Friend.builder()
                .member(member)
                .friend(friend)
                .friendStatus(FriendStatus.COMPLETED)
                .build();
    }
    private Friend applyFriend(final Member member, final Member friend) {
        return Friend.builder()
                .member(member)
                .friend(friend)
                .friendStatus(FriendStatus.WAITING)
                .build();
    }
    private Friend getFriendShip(final Member member, final Member friend) {
        return friendRepository.findByMemberAndFriend(member,friend)
                .orElseThrow(FriendNotFoundException::new);
    }

    private Member getFriend(final String friendUserId) {
        return memberRepository.findByUserId(friendUserId)
                .orElseThrow(UserNotFoundException::new);
    }

    private Member getMember(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }
}
