package com.project.secretdiary.controller;

import com.project.secretdiary.dto.LoginMember;
import com.project.secretdiary.dto.response.friend.FriendPageResponse;
import com.project.secretdiary.dto.response.friend.WaitingFriendPageResponse;
import com.project.secretdiary.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
public class FriendController {
    private final FriendService friendService;

    @PostMapping("/apply/{id}")
    public ResponseEntity<Void> applyFriend(@CurrentUser LoginMember loginMember,
                                      @PathVariable("id") final String friendId) {
        friendService.apply(loginMember.getId(), friendId);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/accept/{id}")
    public ResponseEntity<Void> acceptFriend(@CurrentUser LoginMember loginMember,
                                       @PathVariable("id") final String friendId) {
        friendService.accept(loginMember.getId(), friendId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deny/{id}")
    public ResponseEntity<Void> denyFriend(@CurrentUser LoginMember loginMember,
                                     @PathVariable("id") final String friendId) {
        friendService.deny(loginMember.getId(), friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<FriendPageResponse> getFriends(@CurrentUser LoginMember loginMember, Pageable pageable) {

        return ResponseEntity.ok(friendService.findFriends(loginMember.getId(), pageable));
    }

    @GetMapping("/waiting")
    public ResponseEntity<WaitingFriendPageResponse> getWaiting(@CurrentUser LoginMember loginMember,
                                                                Pageable pageable) {
        return ResponseEntity.ok( friendService.findFriendsWithWaiting(loginMember.getId(), pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFriend(@CurrentUser LoginMember loginMember,
                                       @PathVariable("id") final String friendId) {
        friendService.delete(loginMember.getId(), friendId);
        return ResponseEntity.ok().build();
    }
}