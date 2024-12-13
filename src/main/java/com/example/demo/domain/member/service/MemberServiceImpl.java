package com.example.demo.domain.member.service;

import com.example.demo.domain.member.dto.SigninRequest;
import com.example.demo.domain.member.dto.SignupRequest;
import com.example.demo.domain.member.entity.Member;
import com.example.demo.domain.member.entity.MemberPermission;
import com.example.demo.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void signup(SignupRequest request) {
        Member member = Member.builder()
                .memberKey(UUID.randomUUID().toString())
                .memberName(request.getMemberName())
                .memberId(request.getMemberId())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .permission(MemberPermission.MEMBER)
                .build();

    memberRepository.save(member);
    }

    @Override
    public void signin(SigninRequest request, HttpServletResponse response) {

    }


}
