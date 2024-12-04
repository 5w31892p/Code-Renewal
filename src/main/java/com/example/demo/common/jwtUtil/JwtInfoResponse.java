package com.example.demo.common.jwtUtil;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtInfoResponse {
    private String email;
    private String refreshToken;
}
