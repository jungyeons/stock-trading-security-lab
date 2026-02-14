package com.stocklab.api.dto;

import java.time.Instant;

public class AuditDtos {
    public record AuditResponse(
            Long id,
            String eventType,
            String actor,
            String details,
            Instant createdAt
    ) {}
}
