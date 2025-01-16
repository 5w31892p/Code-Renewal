package com.example.demo.domain.member.service;

import com.example.demo.common.jwtUtil.JwtUtil;
import com.example.demo.domain.member.dto.*;
import com.example.demo.domain.member.entity.Member;
import com.example.demo.domain.member.entity.MemberPermission;
import com.example.demo.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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
    @Transactional
    public DuplicateConfirmResponse duplicateConfirm(DuplicateConfirmRequest request) {
        boolean isDuplicate;

        switch (request.getType()) {
            case "memberId" -> isDuplicate = memberRepository.existsByMemberId(request.getValue());
            case "email" -> isDuplicate = memberRepository.existsByEmail(request.getValue());
            default -> throw new IllegalArgumentException("Incorrect type");
        }

        return new DuplicateConfirmResponse(isDuplicate);
    }


    @Override
    @Transactional
    public void signin(SigninRequest request, HttpServletResponse response) {
        String memberId = request.getMemberId();
        String password = request.getPassword();

        Member member = memberRepository.findByMemberId(memberId);
        if (member == null || !passwordEncoder.matches(password, member.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials, 회원정보가 올바르지 않습니다.");
        }

        // 로그인 성공 처리
        handleLoginSuccess(member, response);
    }

    private void handleLoginSuccess(Member member, HttpServletResponse response) {

        // 토큰 발급
        String accessToken = jwtUtil.createAccessToken(member.getEmail(), member.getPermission());
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
        String refreshToken = jwtUtil.createRefreshToken(member.getEmail(), member.getPermission());
        response.addHeader(JwtUtil.REFRESH_HEADER, refreshToken);

    }


    @Override
    public void signout(HttpServletResponse response) {

    }

    @Override
    @Transactional(readOnly = true)
    public InfoResponse getMyInfo(Long memberSeq, String email) {
        Optional<InfoResponse> memberInfo = memberRepository.findByInfoEmailAndSeq(email, memberSeq);



        return InfoResponse.builder()
                .memberId(memberInfo.get().getMemberId())
                .memberName(memberInfo.get().getMemberName())
                .email(memberInfo.get().getEmail())
                .build();
    }

    @Override
    public void findId() {

    }

    @Override
    public void findPassword() {

    }

    @Override
    public void updatePassword() {

    }
}