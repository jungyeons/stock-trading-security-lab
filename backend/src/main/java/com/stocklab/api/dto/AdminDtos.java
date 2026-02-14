package com.stocklab.api.dto;

public class AdminDtos {
    public record UserSummary(
            Long id,
            String username,
            String role,
            String fullName
    ) {}
}
