package com.example.demo.domain.member.service;

import com.example.demo.domain.member.dto.SignupRequest;
import com.example.demo.domain.member.entity.Member;
import com.example.demo.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void signup(SignupRequest request) {
        Member member = Member.builder()
                .memberKey(UUID.randomUUID().toString())
                .memberName(request.getMemberName())
                .memberId(request.getMemberId())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

    memberRepository.save(member);
    }
}
