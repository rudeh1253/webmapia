package com.nsl.webmapia.common.exception;

public class UnsupportedNotificationTypeException extends RuntimeException {
    private final ErrorCode errorCode;

    public UnsupportedNotificationTypeException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
