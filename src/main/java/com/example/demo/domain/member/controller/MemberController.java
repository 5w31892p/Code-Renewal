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
