package com.oreoluwa.RedditClone.Services.Impl;

import com.oreoluwa.RedditClone.Entities.RefreshToken;
import com.oreoluwa.RedditClone.ExceptionHandlers.SpringRedditException;
import com.oreoluwa.RedditClone.Repositories.RefreshTokenRepository;
import com.oreoluwa.RedditClone.Services.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;
@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }
    @Override
    public void validateRefreshToken(String token) {
        refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new SpringRedditException("Invalid refresh Token"));
    }
    @Override
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}