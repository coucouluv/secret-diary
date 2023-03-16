package com.project.secretdiary.controller;

import com.project.secretdiary.dto.LoginMember;
import com.project.secretdiary.dto.response.FriendResponse;
import com.project.secretdiary.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend")
public class FriendController {
    private final FriendService friendService;

    @PostMapping("/apply/{id}")
    public ResponseEntity<Void> applyFriend(@CurrentUser LoginMember loginMember,
                                      @PathVariable("id") final String friendId) {
        friendService.applyFriend(loginMember.getId(), friendId);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/accept/{id}")
    public ResponseEntity<Void> acceptFriend(@CurrentUser LoginMember loginMember,
                                       @PathVariable("id") final String friendId) {
        friendService.acceptFriend(loginMember.getId(), friendId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deny/{id}")
    public ResponseEntity<Void> denyFriend(@CurrentUser LoginMember loginMember,
                                     @PathVariable("id") final String friendId) {
        friendService.denyFriend(loginMember.getId(), friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FriendResponse>> getFriends(@CurrentUser LoginMember loginMember) {
        List<FriendResponse> friends = friendService.getFriends(loginMember.getId());
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/waiting")
    public ResponseEntity<List<FriendResponse>> getWaiting(@CurrentUser LoginMember loginMember) {
        List<FriendResponse> friends = friendService.getWaitingFriendList(loginMember.getId());
        return ResponseEntity.ok(friends);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFriend(@CurrentUser LoginMember loginMember,
                                       @PathVariable("id") final String friendId) {
        friendService.deleteFriend(loginMember.getId(), friendId);
        return ResponseEntity.ok().build();
    }
}