package com.example.demo.common;

public class ApiResponse<T> {
    private int status;        // HTTP 상태 코드
    private String message;    // 응답 메시지
//    private T data;            // 응답 데이터 (성공 시 내용)

    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
//        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

//    public T getData() {
//        return data;
//    }
}

