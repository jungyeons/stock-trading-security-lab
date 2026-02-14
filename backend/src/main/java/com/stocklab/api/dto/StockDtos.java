package com.stocklab.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class StockDtos {
    public record StockResponse(
            Long id,
            String symbol,
            String name,
            BigDecimal currentPrice,
            boolean active
    ) {}

    public record CreateOrUpdateStockRequest(
            @NotBlank @Size(min = 1, max = 12) String symbol,
            @NotBlank @Size(min = 2, max = 120) String name,
            @NotNull @DecimalMin("0.01") BigDecimal currentPrice,
            boolean active
    ) {}
}
