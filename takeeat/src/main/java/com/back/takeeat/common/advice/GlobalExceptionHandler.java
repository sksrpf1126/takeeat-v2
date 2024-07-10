package com.back.takeeat.common.advice;

import com.back.takeeat.common.dto.ErrorResponse;
import com.back.takeeat.common.exception.BaseException;
import com.back.takeeat.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException throw Exception : {}", e);

        ErrorResponse errorResponse = null;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null || auth.getPrincipal().equals("anonymousUser")) {
            errorResponse = ErrorResponse.of(ErrorCode.MEMBER_UNAUTHORIZED);
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        } else {
            errorResponse = ErrorResponse.of(ErrorCode.MEMBER_ROLE_NOT_EXISTS);
            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
        }
    }

}
