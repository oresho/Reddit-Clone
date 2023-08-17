package com.oreoluwa.RedditClone.Services.Impl;

import com.oreoluwa.RedditClone.Dtos.JwtAuthenticationResponse;
import com.oreoluwa.RedditClone.Dtos.RefreshTokenRequest;
import com.oreoluwa.RedditClone.Dtos.RegisterRequest;
import com.oreoluwa.RedditClone.Dtos.SigninRequest;
import com.oreoluwa.RedditClone.Entities.*;
import com.oreoluwa.RedditClone.Entities.User;
import com.oreoluwa.RedditClone.ExceptionHandlers.SpringRedditException;
import com.oreoluwa.RedditClone.Repositories.UserRepository;
import com.oreoluwa.RedditClone.Repositories.VerificationTokenRepository;
import com.oreoluwa.RedditClone.Services.AuthenticationService;
import com.oreoluwa.RedditClone.Services.JwtService;
import com.oreoluwa.RedditClone.Services.MailService;
import com.oreoluwa.RedditClone.Services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final RefreshTokenService refreshTokenService;
    @Value("${ACTIVATION_LINK}")
    private String ACTIVATION_LINK;
    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;

    @Override
    @Transactional
    public void signup(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .created(Instant.now())
                .enabled(false)
                .build();
        userRepository.save(user);

        String token = generateVerificationToken(user);
        String message = mailService.buildMail("Thank you for signing up to Spring Reddit, please click on the below url to activate your account : "
                + ACTIVATION_LINK + "/" + token);

        mailService.sendMail(new NotificationEmail("Please Activate your account", user.getEmail(), message));
    }

    @Override
    public JwtAuthenticationResponse signin(SigninRequest request) throws Throwable {
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        }
        catch (DisabledException e){
            throw new SpringRedditException(e.getMessage() + ", please verify your account");
        }
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        // return a JWT to our signed-in user
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder()
                .authenticationToken(jwt)
                .expiresAt(Instant.now().plusMillis(jwtExpirationInMillis))
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .username(request.getUsername())
                .build();
    }

    @Override
    public String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }
    @Override
    @Transactional
    public void verifyAccount(String token) {
        // Verify if token Exists
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
        verificationTokenOptional.orElseThrow(() -> new SpringRedditException("Invalid Token"));
        // Verify if token belongs to a registered User
        String username = verificationTokenOptional.get().getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User Not Found with id - " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser() {
                User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
    }
    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = userRepository.findByUsername(refreshTokenRequest.getUsername()).get();
        String token = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtExpirationInMillis))
                .username(refreshTokenRequest.getUsername())
                .build();
    }
}