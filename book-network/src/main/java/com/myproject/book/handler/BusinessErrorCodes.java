package com.myproject.book.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;


public enum BusinessErrorCodes {

    NO_CODE(0, "No code", NOT_IMPLEMENTED),
    INCORRECT_CURRENT_PASSWORD(300, "Incorrect current password", BAD_REQUEST),
    NEW_PASSWORD_DOES_NOT_MATCH(301, "New password does not match", BAD_REQUEST),
    ACCOUNT_LOCKED(302, "Account locked", FORBIDDEN),
    ACCOUNT_DISABLED(303, "Account disabled", FORBIDDEN),
    BAD_CREDENTIALS(304, "Login and / or password is incorrect", FORBIDDEN),
    ;


    @Getter
    private final int errorCode;
    @Getter
    private final String errorDescription;
    @Getter
    private final HttpStatus httpStatus;


    BusinessErrorCodes(int errorCode, String errorDescription, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.httpStatus = httpStatus;
    }
}
