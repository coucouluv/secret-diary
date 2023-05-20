package com.project.secretdiary.repository;

import com.project.secretdiary.dto.request.diary.DiariesRequest;
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
    public List<DiaryResponse> findByMemberAndFriend(Long memberId, Long friendId, DiariesRequest diariesRequest) {
        JPAQuery<DiaryResponse> jpaQuery = queryFactory
                .select(Projections.constructor(DiaryResponse.class, diary.id,
                        diary.title, diary.image))
                .from(diary)
                .where(
                        ltDiaryId(diariesRequest.getId()),
                        eqFriendship(memberId,friendId)
                                .or(eqFriendship(friendId,memberId))
                )
                .orderBy(diary.id.desc())
                .limit(diariesRequest.getSize()+1);
        return jpaQuery.fetch();
    }

    private BooleanExpression eqFriendship(Long memberId, Long friendId) {
        if(memberId == null || friendId == null) {
            return null;
        }
        return diary.friend.id.eq(friendId).and(diary.member.id.eq(memberId));
    }

    private BooleanExpression ltDiaryId(Long diaryId) {
        if(diaryId == null) {
            return null;
        }
        return diary.id.lt(diaryId);
    }
}