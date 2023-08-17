package com.oreoluwa.RedditClone.Services;

import com.oreoluwa.RedditClone.Entities.RefreshToken;

public interface RefreshTokenService {
    RefreshToken generateRefreshToken();
    void validateRefreshToken(String token);
    void deleteRefreshToken(String token);


}
