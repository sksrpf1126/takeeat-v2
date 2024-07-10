package com.back.takeeat.security.handler;

import com.back.takeeat.domain.user.Member;
import com.back.takeeat.domain.user.MemberRoleType;
import com.back.takeeat.security.oauth.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Member member = ((PrincipalDetails) authentication.getPrincipal()).getMember();

        //최초 로그인인 경우 추가 정보 입력을 위한 회원가입 페이지 리다이렉트
        if(member.getRole().equals(MemberRoleType.ROLE_GUEST)) {
            String redirectURL = UriComponentsBuilder.fromUriString("/member/policy-agreement")
                    .queryParam("loginType", "social")
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            request.getSession().setAttribute("email", member.getEmail());
            request.getSession().setAttribute("providerType", member.getProviderType());
            getRedirectStrategy().sendRedirect(request, response, redirectURL);
        } else {
            String redirectURL = UriComponentsBuilder.fromUriString("/market/info")
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();
            getRedirectStrategy().sendRedirect(request, response, redirectURL);
        }

    }
}
