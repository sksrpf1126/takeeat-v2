package com.back.takeeat.security.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final RedisTemplate<String, String> redisTemplate;
    private final OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Override
    public void
    logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        //소셜 회원이 로그아웃 하는 경우
        //Redis에 저장한 Token 삭제
        if (authentication instanceof OAuth2AuthenticationToken) {
            oAuth2AuthorizedClientService.removeAuthorizedClient(null, authentication.getName());
        }

    }
}
