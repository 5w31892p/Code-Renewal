package com.example.demo.common.config;

import com.example.demo.common.jwtUtil.JwtUtil;
import com.example.demo.common.security.JwtAuthFilter;
import com.example.demo.common.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final String[] permitAllArray = {
            "/h2-console/**", "/api/**", "/favicon.ico",
            "/members/**",
            "/boards/**"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        /*
        XSS 클라이언트 공격 방지
        default-src 'self' 모든 리소스는 동일 출처에서만 로드
        img-src 'self' data: 이미지 소스는 동일 출처와 인라인 데이터(베이스64) 허용
        font-src 'self' 폰트는 동일 출처에서만 로드
        connect-src 'self' XMLHttpRequest, WebSocket 등은 동일 출처로 제한
        base-uri 'self' <base> 태그를 동일 출처로 제한하여 URL 조작 방지
        form-action 'self' 폼 제출은 동일 출처로 제한
        frame-ancestors 'none' 프레임 내 포함을 완전히 금지하여 클릭재킹 방지
        upgrade-insecure-requests HTTP 요청을 HTTPS로 자동 업그레이드
        block-all-mixed-content 혼합 콘텐츠(HTTP + HTTPS) 로딩 차단
        script-src 'self' 동일 출처의 스크립트만 허용
        script-src 'self' 'nonce-randomString'
        object-src 'none' <object> 태그 사용 금지
        style-src 'self' 'unsafe-inline' 동일 출처 스타일과 인라인 스타일을 허용
        report-uri 서버에서 CSP 위반 보고를 받을 엔드포인트
        report-to 더 현대적인 방식으로 JSON 기반 보고를 활성화
        */
        http.headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp
/*                        .policyDirectives(
                                "default-src 'self'; " +
                                "script-src 'self' 'nonce-randomString'; " +
                                "object-src 'none'; " +
                                "style-src 'self' 'unsafe-inline'; " +
                                "img-src 'self' data:; " +
                                "font-src 'self'; " +
                                "connect-src 'self'; " +
                                "base-uri 'self'; " +
                                "form-action 'self'; " +
                                "frame-ancestors 'none'; " +
                                "upgrade-insecure-requests; " +
                                "block-all-mixed-content; "
                               //  "report-uri /csp-violation-report-endpoint;"
                        )*/
                                .policyDirectives("script-src 'self'")
                        )

        );


        /*
        세션 관리 정책 상태 비저장(Stateless)으로 설정
        */
        http.sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        );

        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(permitAllArray).permitAll()

                .anyRequest().authenticated()
        );

        http.addFilterBefore(new JwtAuthFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class);

        http.cors(withDefaults());

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers("/v3/api-docs", "/h2-console/**", "/favicon.ico");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
