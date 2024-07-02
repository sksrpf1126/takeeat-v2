package com.back.takeeat.common.dto;

import com.back.takeeat.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private String message;

    private String code;

    private ErrorResponse(ErrorCode code) {
        this.message = code.getMessage();
        this.code = code.getCode();
    }

    public static ErrorResponse of(ErrorCode code) {
        return new ErrorResponse(code);
    }

}
