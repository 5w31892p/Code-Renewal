package com.example.demo.domain.member.repository;

import com.example.demo.domain.member.dto.InfoResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.example.demo.domain.member.entity.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public boolean existsByMemberId(String memberId) {
        return jpaQueryFactory
                .selectOne() // SELECT 1과 동일, EXISTS와 유사한 동작
                .from(member)
                .where(member.memberId.eq(memberId))
                .setHint("org.hibernate.readOnly", true) // 읽기 전용 힌트
                .fetchFirst() != null; // null이 아니라면 true 반환

    }

    @Override
    public Optional<InfoResponse> findByInfoEmailAndSeq(String email, Long seq) {

        return Optional.ofNullable(jpaQueryFactory.select(Projections.constructor(InfoResponse.class,
                        member.memberId,
                        member.memberName,
                        member.email))
                .from(member)
                .where(member.email.eq(email), member.memberSeq.eq(seq))
                .setHint("org.hibernate.readOnly", true)
                .fetchOne());
    }
}