package com.project.secretdiary.dto.response.friend;

import com.project.secretdiary.entity.Member;
import lombok.Getter;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FriendPageResponse {

    private boolean hasNext;
    List<FriendResponse> friends;

    private FriendPageResponse(final boolean hasNext, final List<FriendResponse> friends) {
        this.hasNext = hasNext;
        this.friends = friends;
    }

    public static FriendPageResponse from(final Slice<Member> slice) {
        List<FriendResponse> friendResponses = slice.getContent()
                .stream()
                .map(FriendResponse::from)
                .collect(Collectors.toList());
        return new FriendPageResponse(slice.hasNext(), friendResponses);
    }
}
