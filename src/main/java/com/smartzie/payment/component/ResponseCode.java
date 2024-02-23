package com.smartzie.payment.component;

import lombok.Getter;

@Getter
public enum ResponseCode {
    NO_END_POINT(404, "ENDPOINT NOT FOUND"),
    SUCCESS(200, "OK"),
    SERVER_ERROR(500, "INTERNAL SERVER ERROR"),
    DATABASE_ERROR(501, "INTERNAL SERVER ERROR"),
    VALIDATION(100, "ERROR MAPPING"),
    TypeMismatch(101, "ERROR MAPPING"),
    JWTUnverified(40, "Token Unverified"),
    JWTExp(401, "Token is invalid or expired"),
    EXPECTATION_FAILED(417, "The Request's Expect Header Could not be met."),
    BAD_REQUEST(400, "BAD REQUEST"),
    DUPLICATE_DATA(409, "Data is already exist."),
    OPT(102, "Error OTP"),
    NO_DATA(101, "DATA TIDAK DITEMUKAN");
    private final Integer code;
    private final String message;

    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}