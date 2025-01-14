package com.example.demo.domain.member.service;

import com.example.demo.domain.member.dto.MyPageResponse;
import com.example.demo.domain.member.dto.SigninRequest;
import com.example.demo.domain.member.dto.SignupRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {

    void signup(SignupRequest request);
    void duplicateConfirmName();
    void duplicateConfirmEmail();
    void signin(SigninRequest request, HttpServletResponse response);
    void signout(HttpServletResponse response);
    void getMyInfo(MyPageResponse response);
    void findId();
    void findPassword();
    void updatePassword();
}
