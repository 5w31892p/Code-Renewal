package com.example.demo.domain.member.controller;


import com.example.demo.common.ApiResponse;
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

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> signup(@Validated @RequestBody SignupRequest request) {
        memberService.signup(request);
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Signup successful",
                null
        );

        return ResponseEntity.ok(response);
    }
}
