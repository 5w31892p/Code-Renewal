package com.example.demo.domain.member.repository;

import com.example.demo.domain.member.dto.InfoResponse;

import java.util.Optional;

public interface MemberRepositoryCustom {

    boolean existsByMemberId(String memberId);

    Optional<InfoResponse> findByInfoEmailAndSeq(String email, Long seq);
}
