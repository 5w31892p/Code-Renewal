package com.example.demo.domain.member.service;

import com.example.demo.domain.member.dto.SignupRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberServiceImpl memberService;

    @DisplayName("회원가입 성공")
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
}