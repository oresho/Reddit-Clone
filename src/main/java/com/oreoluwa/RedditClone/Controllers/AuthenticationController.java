package com.oreoluwa.RedditClone.Controllers;

import com.oreoluwa.RedditClone.Dtos.JwtAuthenticationResponse;
import com.oreoluwa.RedditClone.Dtos.RefreshTokenRequest;
import com.oreoluwa.RedditClone.Dtos.RegisterRequest;
import com.oreoluwa.RedditClone.Dtos.SigninRequest;
import com.oreoluwa.RedditClone.Services.AuthenticationService;
import com.oreoluwa.RedditClone.Services.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("signup")
    public ResponseEntity signup(@RequestBody RegisterRequest registerRequest) {
        authenticationService.signup(registerRequest);
        return new ResponseEntity(OK);
    }

    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authenticationService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", OK);
    }

    @PostMapping("signin")
    public ResponseEntity<JwtAuthenticationResponse> login(@RequestBody SigninRequest signinRequest) throws Throwable {
        return new ResponseEntity<>(authenticationService.signin(signinRequest), OK);
    }

    @PostMapping("refresh/token")
    public ResponseEntity<JwtAuthenticationResponse> refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return new ResponseEntity<>(authenticationService.refreshToken(refreshTokenRequest), OK);
    }

    @PostMapping("signout")
    public ResponseEntity<String> signout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
    }
}
