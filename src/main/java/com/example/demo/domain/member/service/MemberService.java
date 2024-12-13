package com.example.demo.domain.member.service;

import com.example.demo.domain.member.dto.SigninRequest;
import com.example.demo.domain.member.dto.SignupRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {

    void signup(SignupRequest request);
    void signin(SigninRequest request, HttpServletResponse response);
}
