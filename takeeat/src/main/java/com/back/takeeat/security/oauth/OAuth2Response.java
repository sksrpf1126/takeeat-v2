package com.back.takeeat.security.oauth;

import com.back.takeeat.domain.user.ProviderType;

public interface OAuth2Response {

    //제공자 (NAVER, KAKAO, GOOGLE ...)
    ProviderType getProvider();

    String getProviderId();

    String getEmail();

    String getName();

}
