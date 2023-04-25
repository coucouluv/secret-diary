package com.project.secretdiary.repository;

import com.project.secretdiary.dto.response.diary.DiaryResponse;
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

import static com.project.secretdiary.entity.QDiary.diary;


@Slf4j
@Repository
public class DiaryRepositoryCustomImpl implements DiaryRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public DiaryRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.queryFactory = jpaQueryFactory;
    }

    @Override
    public Slice<DiaryResponse> findByMemberAndFriend(Long memberId, Long friendId, Pageable pageable) {
        JPAQuery<DiaryResponse> jpaQuery = queryFactory
                .select(Projections.constructor(DiaryResponse.class, diary.id,
                        diary.title, diary.image))
                .from(diary)
                .where(
                        eqFriendship(memberId,friendId)
                                .or(eqFriendship(friendId,memberId))
                )
                .orderBy(diary.saveDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()+1);
        List<DiaryResponse> diaryResponses = jpaQuery.fetch();

        if(diaryResponses.size() > pageable.getPageSize()) {
            diaryResponses.remove(pageable.getPageSize());
            return new SliceImpl<>(diaryResponses, pageable, true);
        }
        return new SliceImpl<>(diaryResponses, pageable, false);
    }

    private BooleanExpression eqFriendship(Long memberId, Long friendId) {
        if(memberId == null || friendId == null) {
            return null;
        }
        return diary.friend.id.eq(friendId).and(diary.member.id.eq(memberId));
    }
}