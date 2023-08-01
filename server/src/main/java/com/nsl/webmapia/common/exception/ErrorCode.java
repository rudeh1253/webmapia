package com.nsl.webmapia.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_INPUT_TYPE(400, "C_001", "Invalid input type");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
