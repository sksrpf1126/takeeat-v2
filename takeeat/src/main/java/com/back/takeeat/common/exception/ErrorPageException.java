package com.back.takeeat.common.exception;

public class ErrorPageException extends RuntimeException {
    private final ErrorCode errorCode;

    public ErrorPageException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
