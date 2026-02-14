package com.stocklab.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {
    public record SignupRequest(
            @NotBlank @Size(min = 4, max = 50) String username,
            @NotBlank @Size(min = 8, max = 120) String password,
            @NotBlank @Size(min = 2, max = 120) String fullName
    ) {}

    public record LoginRequest(
            @NotBlank String username,
            @NotBlank String password
    ) {}

    public record AuthResponse(
            String token,
            String username,
            String role
    ) {}
}
