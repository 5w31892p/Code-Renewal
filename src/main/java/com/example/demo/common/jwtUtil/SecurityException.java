package com.example.demo.common.jwtUtil;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SecurityException extends Throwable {

    private int statusCode;
    private String msg;

    public SecurityException(int statusCode, String msg) {
        this.statusCode = statusCode;
        this.msg = msg;
    }
}

