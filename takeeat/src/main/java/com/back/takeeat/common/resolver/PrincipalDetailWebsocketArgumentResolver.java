package com.back.takeeat.common.resolver;

import com.back.takeeat.common.exception.AuthException;
import com.back.takeeat.common.exception.ErrorCode;
import com.back.takeeat.security.oauth.PrincipalDetails;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.security.Principal;

public class PrincipalDetailWebsocketArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return PrincipalDetails.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message<?> message) throws Exception {
        Principal principal = SimpMessageHeaderAccessor.getUser(message.getHeaders());

        if (principal instanceof AbstractAuthenticationToken abstractAuthenticationToken) {
            return abstractAuthenticationToken.getPrincipal();
        }

        throw new AuthException(ErrorCode.FAIL_CONVERT);
    }
}
