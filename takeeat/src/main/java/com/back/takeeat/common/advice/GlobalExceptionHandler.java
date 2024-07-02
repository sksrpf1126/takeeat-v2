package com.back.takeeat.common.advice;

import com.back.takeeat.common.dto.ErrorResponse;
import com.back.takeeat.common.exception.BaseException;
import com.back.takeeat.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);

        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(errorCode.getStatus()));
    }

}
