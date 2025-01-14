package com.example.demo.common;

import org.springframework.http.HttpStatus;

public class ApiResponse<T> {
    private int status;        // HTTP 상태 코드
    private String message;    // 응답 메시지
    private T data;            // 성공 시 반환 데이터
    private String error;      // 에러 메시지 (실패 시)

    private ApiResponse(int status, String message, T data, String error) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.error = error;
    }

    // 성공 응답
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), "성공, Successful", data, null);
    }

    // 에러 응답
    public static ApiResponse<Void> error(HttpStatus status, String message, String error) {
        return new ApiResponse<>(status.value(), message, null, error);
    }

    // Getters
    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }
}
