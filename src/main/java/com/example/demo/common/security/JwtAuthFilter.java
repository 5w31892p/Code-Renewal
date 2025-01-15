package com.example.demo.common.security;

import com.example.demo.common.ApiResponse;
import com.example.demo.common.jwtUtil.JwtUtil;
import com.example.demo.common.jwtUtil.SecurityException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.resolveAccessToken(request);

        if (token != null) {
            try {
                if (!jwtUtil.validateToken(token)) {
                    jwtExceptionHandler(response, "Invalid or expired token", HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                Claims claims = jwtUtil.getUserInfoFromToken(token);
                setAuthentication(claims.getSubject());
            } catch (JwtException | SecurityException e) {
                log.error("Token validation error: {}", e.getMessage());
                jwtExceptionHandler(response, e.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }


    public Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = this.createAuthentication(email);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) throws IOException {
        ApiResponse<Void> apiResponse = ApiResponse.error(
                HttpStatus.valueOf(statusCode),
                "JWT validation failed",
                msg
        );

        // ObjectMapper로 ApiResponse를 JSON으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);

        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }
}

