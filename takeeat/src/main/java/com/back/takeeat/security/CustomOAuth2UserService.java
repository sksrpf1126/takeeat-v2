package com.back.takeeat.security;

import com.back.takeeat.domain.user.Member;
import com.back.takeeat.domain.user.MemberRoleType;
import com.back.takeeat.domain.user.ProviderType;
import com.back.takeeat.repository.MemberRepository;
import com.back.takeeat.security.oauth.KakaoResponse;
import com.back.takeeat.security.oauth.OAuth2Response;
import com.back.takeeat.security.oauth.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId().toUpperCase();
        ProviderType providerType = ProviderType.valueOf(provider);

        log.info("OAuth2 Login Provider : {}", provider);

        OAuth2Response oAuth2Response = null;

        if(providerType.equals(ProviderType.KAKAO)) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }else {
            return null;
        }

        Member findMember = memberRepository.findByEmail(oAuth2Response.getEmail())
                .orElse(Member.builder()
                        .email(oAuth2Response.getEmail())
                        .role(MemberRoleType.ROLE_GUEST)
                        .providerType(oAuth2Response.getProvider())
                        .build());

        return new PrincipalDetails(findMember, oAuth2User.getAttributes());
    }
}
