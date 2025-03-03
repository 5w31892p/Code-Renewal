package com.example.demo.domain.member.repository;

import com.example.demo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    Member findByMemberId(String memberId);
    Member findByMemberSeq(Long memberSeq);
    Member findByEmail(String email);
//    boolean existsByMemberId(String memberId);
    boolean existsByEmail(String email);
}
