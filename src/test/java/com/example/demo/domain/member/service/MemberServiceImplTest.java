package com.example.demo.domain.member.service;

import com.example.demo.common.jwtUtil.JwtUtil;
import com.example.demo.domain.member.dto.*;
import com.example.demo.domain.member.entity.Member;
import com.example.demo.domain.member.entity.MemberPermission;
import com.example.demo.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private MemberServiceImpl memberService;

    @DisplayName("회원가입")
    @Test
    void testSignup() {
        // Given: 준비 단계
        SignupRequest signupRequest = SignupRequest.builder()
                .memberName("John Doe")
                .memberId("john123")
                .email("john.doe@example.com")
                .password("securePassword")
                .build();

        String encodedPassword = "encodedPassword123";
        when(passwordEncoder.encode("securePassword")).thenReturn(encodedPassword);


        // When: 동작 실행
        memberService.signup(signupRequest);

        // Then: 결과 확인
        ArgumentCaptor<Member> memberCaptor = ArgumentCaptor.forClass(Member.class);
        verify(memberRepository, times(1)).save(memberCaptor.capture());

        // AssertJ를 사용하여 검증
        Member savedMember = memberCaptor.getValue();
        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getMemberName()).isEqualTo("John Doe");
        assertThat(savedMember.getMemberId()).isEqualTo("john123");
        assertThat(savedMember.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(savedMember.getPassword()).isEqualTo(encodedPassword); // 암호화된 패스워드 검증
        assertThat(savedMember.getPermission()).isEqualTo(MemberPermission.MEMBER);
        assertThat(savedMember.getMemberKey()).isNotBlank(); // UUID 생성 여부 검증
    }

    @DisplayName("로그인 - 토큰 발급")
    @Test
    void signin() {
        // Given: 준비 단계
        SigninRequest signinRequest = SigninRequest.builder()
                .memberId("john123")
                .password("securePassword")
                .build();

        String encodedPassword = "encodedPassword123";
        Member member = Member.builder()
                .memberId("john123")
                .email("john.doe@example.com")
                .password(encodedPassword)
                .permission(MemberPermission.MEMBER)
                .build();

        when(memberRepository.findByMemberId("john123")).thenReturn(member);
        when(passwordEncoder.matches("securePassword", encodedPassword)).thenReturn(true);
        when(jwtUtil.createAccessToken(member.getEmail(), member.getPermission()))
                .thenReturn("엑세스토큰이다!!");
        when(jwtUtil.createRefreshToken(member.getEmail(), member.getPermission()))
                .thenReturn("리프레쉬토큰이다!!");

        MockHttpServletResponse response = new MockHttpServletResponse();

        // When: 동작 실행
        memberService.signin(signinRequest, response);

        // Then: 결과 확인
        assertThat(response.getHeader(JwtUtil.AUTHORIZATION_HEADER)).isEqualTo("엑세스토큰이다!!");
        assertThat(response.getHeader(JwtUtil.REFRESH_HEADER)).isEqualTo("리프레쉬토큰이다!!");

        verify(memberRepository, times(1)).findByMemberId("john123");
        verify(passwordEncoder, times(1)).matches("securePassword", encodedPassword);
        verify(jwtUtil, times(1)).createAccessToken(member.getEmail(), member.getPermission());
        verify(jwtUtil, times(1)).createRefreshToken(member.getEmail(), member.getPermission());
    }

    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    @Test
    void signinInvalidPassword() {
        // Given: 준비 단계
        SigninRequest signinRequest = SigninRequest.builder()
                .memberId("john123")
                .password("wrongPassword")
                .build();

        String encodedPassword = "encodedPassword123";
        Member member = Member.builder()
                .memberId("john123")
                .email("john.doe@example.com")
                .password(encodedPassword)
                .permission(MemberPermission.MEMBER)
                .build();

        when(memberRepository.findByMemberId("john123")).thenReturn(member);
        when(passwordEncoder.matches("wrongPassword", encodedPassword)).thenReturn(false);

        MockHttpServletResponse response = new MockHttpServletResponse();

        // When & Then: 예외 검증
        assertThrows(ResponseStatusException.class, () -> memberService.signin(signinRequest, response));
    }

    @DisplayName("로그인 실패 - 회원 정보 없음")
    @Test
    void signinMemberNotFound() {
        // Given: 준비 단계
        SigninRequest signinRequest = SigninRequest.builder()
                .memberId("unknownUser")
                .password("securePassword")
                .build();

        when(memberRepository.findByMemberId("unknownUser")).thenReturn(null);

        MockHttpServletResponse response = new MockHttpServletResponse();
        // When & Then: 예외 검증
        assertThrows(ResponseStatusException.class, () -> memberService.signin(signinRequest, response));
    }

    @DisplayName("중복확인 성공")
    @Test
    void duplicateConfirm() {

        //given
        DuplicateConfirmRequest requestId = DuplicateConfirmRequest.builder()
                .type("memberId")
                .value("newPeople").build();

        DuplicateConfirmRequest requestEmail = DuplicateConfirmRequest.builder()
                .type("email")
                .value("haha55@naver.com").build();

        when(memberRepository.existsByMemberId("newPeople")).thenReturn(true);
        when(memberRepository.existsByEmail("haha55@naver.com")).thenReturn(true);
        // When
        DuplicateConfirmResponse responseId = memberService.duplicateConfirm(requestId);
        DuplicateConfirmResponse responseEmail = memberService.duplicateConfirm(requestEmail);

        // Then
        assertNotNull(responseId);
        assertTrue(responseId.isDuplicate());
        assertNotNull(responseEmail);
        assertTrue(responseEmail.isDuplicate());
    }

    @DisplayName("내 정보 조회 성공")
    @Test
    void getMyInfo() {
        //given
        String email = "john.doe@example.com";
        Member member = Member.builder()
                .memberId("john123")
                .email(email)
                .password("encodedPassword")
                .permission(MemberPermission.MEMBER)
                .build();

        InfoResponse myInfo = InfoResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .build();

        when(memberRepository.findByInfoEmail(email)).thenReturn(Optional.of(myInfo));

        //when
        InfoResponse response = memberService.getMyInfo(email);

        //then
        assertNotNull(response);
        assertThat(response.getMemberId()).isEqualTo("john123");
        assertThat(response.getEmail()).isEqualTo(email);

        verify(memberRepository, times(1)).findByInfoEmail(email);
    }

    @DisplayName("내 정보 조회 실패 - 회원 정보 없음")
    @Test
    void getMyInfo_MemberNotFound() {
        // given
        String email = "unknown@example.com";
        when(memberRepository.findByInfoEmail(email)).thenReturn(Optional.empty());

        // When & Then=
        assertThrows(NoSuchElementException.class, () -> memberService.getMyInfo(email));
        verify(memberRepository, times(1)).findByInfoEmail(email);
    }
}