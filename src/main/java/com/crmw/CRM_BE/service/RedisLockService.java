package com.crmw.CRM_BE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisLockService {

    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public RedisLockService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public boolean tryLockDonor(Integer donorId, String username, long ttlSeconds) {
        String key = "donor:lock:" + donorId;
        Boolean success = stringRedisTemplate.opsForValue()
                .setIfAbsent(key, username, Duration.ofSeconds(ttlSeconds));
        return Boolean.TRUE.equals(success);
    }

    public String getCurrentLocker(Integer donorId) {
        String key = "donor:lock:" + donorId;
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void releaseLock(Integer donorId, String username) {
        String key = "donor:lock:" + donorId;
        String currentValue = stringRedisTemplate.opsForValue().get(key);
        if (username.equals(currentValue)) {
            stringRedisTemplate.delete(key);
        }
    }
}
