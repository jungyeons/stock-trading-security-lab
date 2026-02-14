package com.stocklab.service;

import com.stocklab.api.dto.AuthDtos;
import com.stocklab.model.AppUser;
import com.stocklab.model.Role;
import com.stocklab.repository.UserRepository;
import com.stocklab.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuditService auditService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            AuditService auditService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.auditService = auditService;
    }

    @Transactional
    public AuthDtos.AuthResponse signup(AuthDtos.SignupRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BadRequestException("username already exists");
        }
        AppUser user = new AppUser();
        user.setUsername(request.username());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        user.setFullName(request.fullName());
        userRepository.save(user);
        auditService.log("SIGNUP_SUCCESS", user.getUsername(), "new user signup");
        return buildAuth(user);
    }

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        } catch (Exception ex) {
            auditService.log("LOGIN_FAILED", request.username(), "invalid credentials for password=" + request.password());
            throw new BadRequestException("invalid credentials");
        }
        AppUser user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadRequestException("invalid credentials"));
        auditService.log("LOGIN_SUCCESS", user.getUsername(), "login succeeded");
        return buildAuth(user);
    }

    public void logout(String username) {
        auditService.log("LOGOUT", username, "logout");
    }

    private AuthDtos.AuthResponse buildAuth(AppUser user) {
        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        return new AuthDtos.AuthResponse(token, user.getUsername(), user.getRole().name());
    }
}
