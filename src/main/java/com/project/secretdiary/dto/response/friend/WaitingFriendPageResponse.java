package com.project.secretdiary.dto.response.friend;

import com.project.secretdiary.entity.Member;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class WaitingFriendPageResponse {
    private boolean hasNext;
    List<WaitingFriendResponse> friends;

    private WaitingFriendPageResponse(final boolean hasNext, final List<WaitingFriendResponse> friends) {
        this.hasNext = hasNext;
        this.friends = friends;
    }

    public static WaitingFriendPageResponse from(final Slice<Member> slice) {
        List<WaitingFriendResponse> friendResponses = slice.getContent()
                .stream()
                .map(WaitingFriendResponse::from)
                .collect(Collectors.toList());
        return new WaitingFriendPageResponse(slice.hasNext(), friendResponses);
    }
}
