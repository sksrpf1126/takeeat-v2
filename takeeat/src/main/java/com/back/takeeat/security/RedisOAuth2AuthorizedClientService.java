package com.back.takeeat.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, String principalName) {
        String key = "oauth:" + principalName;

        String accessToken = redisTemplate.opsForValue().get(key);

        if (accessToken != null) {
            OAuth2AccessToken token = new OAuth2AccessToken(
                    OAuth2AccessToken.TokenType.BEARER,
                    accessToken,
                    Instant.now(),
                    Instant.now().plus(1, ChronoUnit.HOURS));

            ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(clientRegistrationId);

            return (T) new OAuth2AuthorizedClient(
                    clientRegistration,
                    principalName,
                    token
            );
        }
            return null;
    }

    /**
     * @param authorizedClient 소셜 로그인의 정보를 담고 있는 객체
     * @param principal 스프링 시큐리티의 인증 객체
     * KAKAO -> 매 로그인 시 Access Token, Refresh Token 발급 (Access Token 만료시간 -> 6시간, Refresh Token -> 2달)
     * GOOGLE -> 매 로그인 시 Access Token 발급, 첫 로그인 시에만 Refresh Token 발급 (Access Token 만료시간 -> 1시간, Refresh Token -> 2주)
     */
    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        String email = principal.getName();
        String key = "oauth:" + email;
        redisTemplate.opsForValue().set(key, authorizedClient.getAccessToken().getTokenValue(), 60 * 60, TimeUnit.SECONDS);
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
        String key = "oauth:" + principalName;
        redisTemplate.delete(key);
    }
}
