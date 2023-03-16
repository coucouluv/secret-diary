package com.project.secretdiary.repository;

import com.project.secretdiary.entity.FriendEntity;
import com.project.secretdiary.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<FriendEntity, Long> {
    List<FriendEntity> findByMember(MemberEntity member);
    List<FriendEntity> findByFriend(MemberEntity friend);
    Optional<FriendEntity> findByMemberAndFriend(MemberEntity member, MemberEntity friend);
    boolean existsByMemberAndFriend(MemberEntity member, MemberEntity friend);
}
