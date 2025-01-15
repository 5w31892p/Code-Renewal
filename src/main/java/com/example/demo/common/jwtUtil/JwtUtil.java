package com.example.demo.common.jwtUtil;


import com.example.demo.domain.member.entity.MemberPermission;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "Refresh";
    public static final String AUTHORIZATION_KEY = "auth";
    public static final String REFRESH_KEY = "refresh";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 60분 -> 1시간
    private static final long REFRESH_TOKEN_TIME = 60 * 60 * 60 * 1000L; // 60시간 -> 2.5일
    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // header 토큰을 가져오기
    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(REFRESH_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 생성
    public String createAccessToken(String email, MemberPermission permission) {
        return BEARER_PREFIX + Jwts.builder()
                .setSubject(email)
                .claim(AUTHORIZATION_KEY, permission.name())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_TIME))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String createRefreshToken(String email, MemberPermission permission) {
        return BEARER_PREFIX + Jwts.builder()
                .setSubject(email)
                .claim(REFRESH_KEY, permission)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_TIME))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token) throws SecurityException, MalformedJwtException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT 토큰 : {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 : {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.info("Malformed JWT token, 잘못된 JWT 토큰 : {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public JwtInfoResponse jwtInfoResponse(HttpServletRequest request) {
        String accessToken = resolveAccessToken(request);
        Claims userInfoFromToken = getUserInfoFromToken(accessToken);
        String refreshToken = resolveRefreshToken(request);
        String email = userInfoFromToken.getSubject();
        return JwtInfoResponse.builder().email(email).refreshToken(refreshToken).build();
    }
}
