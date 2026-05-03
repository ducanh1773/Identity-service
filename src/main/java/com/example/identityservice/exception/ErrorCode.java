package com.example.identityservice.exception;

public enum ErrorCode {
    USER_EXISTED(1001 , "User existed"),
    USERNAME_INVALID(1003 , "Username must be at least 3 characters"),
    INVALID_PASSWORD(1004, "Password must be at least 8 characters"),
    USER_NOT_EXISTED(1005 , "Wrong username or password"),
    PASSWORD_NOT_CORRECT(1005 , "Wrong username or password")
    ;

    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
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
