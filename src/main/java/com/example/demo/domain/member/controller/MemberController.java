package com.example.demo.domain.member.controller;


import com.example.demo.common.ApiResponse;
import com.example.demo.domain.member.dto.SigninRequest;
import com.example.demo.domain.member.dto.SignupRequest;
import com.example.demo.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입
     *
     * @param request 회원가입 요청 정보
     * @return 성공 시 200 OK, 실패 시 적절한 상태 코드와 메시지 반환
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Validated @RequestBody SignupRequest request) {

        try {
            memberService.signup(request);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (ResponseStatusException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ApiResponse.error((HttpStatus) ex.getStatusCode(), "Signup failed", ex.getReason()));
        }
    }

//    @PostMapping("/name/validation")
//    public ResponseEntity<ApiResponse<Void>> duplicateConfirmName(@Validated @RequestBody SigninRequest request) {}
//    @PostMapping("/password/validation")
//    public ResponseEntity<ApiResponse<Void>> duplicateConfirmName(@Validated @RequestBody SigninRequest request) {}

    /**
     * 로그인
     *
     * @param request 로그인 요청 정보 (사용자 ID 및 비밀번호 포함)
     * @param response HTTP 응답 객체 (AccessToken 및 RefreshToken 헤더 설정)
     * @return 성공 시 200 OK 응답과 null 데이터를 포함한 ApiResponse 반환
     * @throws ResponseStatusException 로그인 실패 시 발생 (상태 코드와 메시지 포함)
     *
     * 응답 코드:
     * - 200: 로그인 성공
     * - 401: 잘못된 자격 증명 (Invalid credentials)
     * - 400: 잘못된 요청 형식
     */
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<Void>> signin(@Validated @RequestBody SigninRequest request, HttpServletResponse response) {
        try {
            memberService.signin(request, response);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (ResponseStatusException ex) {
            return ResponseEntity
                    .status(ex.getStatusCode())
                    .body(ApiResponse.error((HttpStatus) ex.getStatusCode(), "Signin failed", ex.getReason()));
        }

    }
}
