package com.example.demo.domain.member.service;

import com.example.demo.domain.member.dto.*;
import jakarta.servlet.http.HttpServletResponse;

public interface MemberService {

    void signup(SignupRequest request);
    DuplicateConfirmResponse duplicateConfirm(DuplicateConfirmRequest request);
    void signin(SigninRequest request, HttpServletResponse response);
    void signout(HttpServletResponse response);
    InfoResponse getMyInfo(String email);
    void findId();
    void findPassword();
    void updatePassword();
}
