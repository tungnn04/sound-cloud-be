package com.example.soundcloudbe.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    RESOURCE_EXISTED(1001 , "Resource existed", HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_EXISTED(1002, "Resource do not exited", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003, "Password is invalid", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1004, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1005, "You do not have permission", HttpStatus.FORBIDDEN),
    EMAIL_INVALID(1006, "Email is invalid", HttpStatus.BAD_REQUEST),
    OTP_INVALID(1007, "OTP is invalid", HttpStatus.BAD_REQUEST),
    CONFIRM_PASSWORD_INVALID(1006, "Password is invalid", HttpStatus.BAD_REQUEST),;
    private final int code;
    private final String message;
    private final HttpStatusCode httpStatusCode;

    ErrorCode(int code, String message, HttpStatusCode httpStatusCode) {
        this.code = code;
        this.message = message;
        this.httpStatusCode = httpStatusCode;
    }

}
