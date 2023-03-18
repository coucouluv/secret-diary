package com.project.secretdiary.repository;

import com.project.secretdiary.dto.response.DiaryResponse;
import com.project.secretdiary.entity.MemberEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.secretdiary.entity.QDiaryEntity.diaryEntity;

@Slf4j
@Repository
public class DiaryRepositoryCustomImpl implements DiaryRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public DiaryRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.queryFactory = jpaQueryFactory;
    }

    @Override
    public Slice<DiaryResponse> findByMemberAndFriend(MemberEntity member, MemberEntity friend, Pageable pageable) {
        JPAQuery<DiaryResponse> jpaQuery = queryFactory
                .select(Projections.constructor(DiaryResponse.class, diaryEntity.id,
                        diaryEntity.title, diaryEntity.url))
                .from(diaryEntity)
                .where(
                        eqFriendship(member,friend)
                                .or(eqFriendship(friend,member))
                )
                .orderBy(diaryEntity.saveDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()+1);
        List<DiaryResponse> diaryResponses = jpaQuery.fetch();

        if(diaryResponses.size() > pageable.getPageSize()) {
            diaryResponses.remove(pageable.getPageSize());
            return new SliceImpl<>(diaryResponses, pageable, true);
        }
        return new SliceImpl<>(diaryResponses, pageable, false);
    }

    private BooleanExpression eqFriendship(MemberEntity member, MemberEntity friend) {
        if(member == null || friend == null) {
            return null;
        }
        return diaryEntity.friend.eq(friend).and(diaryEntity.member.eq(member));
    }
}