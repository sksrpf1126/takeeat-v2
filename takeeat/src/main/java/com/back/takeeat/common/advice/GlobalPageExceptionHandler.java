package com.back.takeeat.common.advice;

import com.back.takeeat.common.exception.ErrorCode;
import com.back.takeeat.common.exception.ErrorPageException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalPageExceptionHandler {

    @ExceptionHandler(ErrorPageException.class)
    public ModelAndView handleErrorPageException(HttpServletResponse response, ErrorPageException e) {
        ErrorCode errorCode = e.getErrorCode();
        response.setStatus(errorCode.getStatus());

        ModelAndView modelAndView = new ModelAndView("/error/" + errorCode.getStatus());
        modelAndView.addObject("message", errorCode.getMessage());

        return modelAndView;
    }

}
