package com.example.demo.domain.member.repository;

import com.example.demo.domain.member.dto.InfoResponse;

import java.util.Optional;

public interface MemberRepositoryQuery {

    boolean existsByMemberId(String memberId);

    Optional<InfoResponse> findByInfoEmail(String email);
}
