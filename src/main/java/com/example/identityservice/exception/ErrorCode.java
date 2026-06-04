package com.example.identityservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_EXISTED(HttpStatus.INTERNAL_SERVER_ERROR, 1001, "User existed"),
    USERNAME_INVALID(HttpStatus.BAD_REQUEST , 1003, "Username must be at least 3 characters"),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST , 1004, "Password must be at least 8 characters"),
    USER_NOT_EXISTED(HttpStatus.NOT_FOUND, 1005, "Wrong username or password"),
    PASSWORD_NOT_CORRECT(HttpStatus.UNAUTHORIZED , 1005, "Wrong username or password"),
    UNAUTHORIZED(HttpStatus.FORBIDDEN , 1006, "You do not have permission");

    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(HttpStatusCode statusCode , int code, String message) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    ErrorCode() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
