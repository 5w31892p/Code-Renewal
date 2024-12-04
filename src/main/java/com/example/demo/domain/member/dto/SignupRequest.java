package com.example.demo.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    private String memberName;
    private String memberId;
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;
    private String password;
}
