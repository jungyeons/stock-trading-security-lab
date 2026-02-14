package com.stocklab.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDtos {
    public record ProfileResponse(
            Long id,
            String username,
            String role,
            String fullName
    ) {}

    public record UpdateProfileRequest(
            @NotBlank @Size(min = 2, max = 120) String fullName
    ) {}
}
