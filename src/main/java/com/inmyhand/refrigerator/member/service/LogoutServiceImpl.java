package com.inmyhand.refrigerator.member.service;

import com.inmyhand.refrigerator.common.redis.RedisKeyManager;
import com.inmyhand.refrigerator.common.redis.RedisUtil;
import com.inmyhand.refrigerator.member.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final RedisUtil redisUtil;
    private final RedisKeyManager redisKeyManager;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public void logout(Long userId) {
        refreshTokenRepository.deleteByMemberEntityId(userId);
        redisUtil.delete(redisKeyManager.getLoginKey(userId));
    }
}
