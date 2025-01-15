package com.example.demo.domain.member.controller;


import com.example.demo.common.ApiResponse;
import com.example.demo.common.security.UserDetailsImpl;
import com.example.demo.domain.member.dto.*;
import com.example.demo.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
        } catch (ResponseStatusException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(ApiResponse.error(e.getStatusCode(), "Signup failed", e.getReason()));
        }
    }

    /**
     *
     * @param request email, id 중복 확인 정보
     * @return 성공 시 200 OK 응답과 반환 데이터를 포함한 ApiResponse 반환
     */
    @PostMapping("/validation")
    public ResponseEntity<ApiResponse<DuplicateConfirmResponse>> duplicateConfirm(@RequestBody DuplicateConfirmRequest request) {
        try {
            DuplicateConfirmResponse response = memberService.duplicateConfirm(request);
            return ResponseEntity.ok(ApiResponse.success(response)); // 성공 응답에 데이터 포함
        } catch (ResponseStatusException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(ApiResponse.error(e.getStatusCode(), "Duplicate check failed", e.getReason()
                    ));
        }
    }

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
     */
    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<Void>> signin(@Validated @RequestBody SigninRequest request, HttpServletResponse response) {
        try {
            memberService.signin(request, response);
            return ResponseEntity.ok(ApiResponse.success(null));
        } catch (ResponseStatusException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(ApiResponse.error(e.getStatusCode(), "Signin failed", e.getReason()));
        }
    }


    /**
     *
     * @param userDetails  인증/인가 - 토큰
     * @return 성공 시 200 OK 응답과 반환 데이터를 포함한 ApiResponse 반환
     */
    @GetMapping("/my-pages")
    public ResponseEntity<ApiResponse<InfoResponse>> getMyPages(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            InfoResponse response = memberService.getMyInfo(userDetails.getUsername());
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (ResponseStatusException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(ApiResponse.error(e.getStatusCode(), "View my page failed", e.getReason()));
        }
    }
}
