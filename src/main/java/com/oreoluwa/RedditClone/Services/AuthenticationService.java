package com.oreoluwa.RedditClone.Services;

import com.oreoluwa.RedditClone.Dtos.JwtAuthenticationResponse;
import com.oreoluwa.RedditClone.Dtos.RefreshTokenRequest;
import com.oreoluwa.RedditClone.Dtos.RegisterRequest;
import com.oreoluwa.RedditClone.Dtos.SigninRequest;
import com.oreoluwa.RedditClone.Entities.User;
import com.oreoluwa.RedditClone.Entities.VerificationToken;

public interface AuthenticationService {
    void signup(RegisterRequest request);

    JwtAuthenticationResponse signin(SigninRequest request) throws Throwable;
    String generateVerificationToken(User user);
    void verifyAccount(String token);
    User getCurrentUser();
    JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}