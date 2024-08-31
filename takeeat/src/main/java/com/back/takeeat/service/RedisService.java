package com.back.takeeat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public void setAuthCode(String email, String code){
        ValueOperations<String, String> valOperations = redisTemplate.opsForValue();
        //만료기간 5분
        valOperations.set(email,code,300, TimeUnit.SECONDS);
    }

    public String getAuthCode(String email){
        ValueOperations<String, String> valOperations = redisTemplate.opsForValue();
        return valOperations.get(email);
    }

}
