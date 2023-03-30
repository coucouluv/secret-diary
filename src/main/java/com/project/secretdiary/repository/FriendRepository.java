package com.project.secretdiary.repository;

import com.project.secretdiary.entity.Friend;
import com.project.secretdiary.entity.MemberEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query("select f.friend from Friend f where f.member.id = :memberId and f.friendStatus = 'COMPLETED'")
    Slice<MemberEntity> findFriendWithComplete(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select f.member from Friend f where f.friend.id = :friendId and f.friendStatus = 'WAITING'")
    Slice<MemberEntity> findFriendWithWaiting(@Param("friendId") Long friendId, Pageable pageable);

    Optional<Friend> findByMemberAndFriend(MemberEntity member, MemberEntity friend);
    boolean existsByMemberAndFriend(MemberEntity member, MemberEntity friend);
}
