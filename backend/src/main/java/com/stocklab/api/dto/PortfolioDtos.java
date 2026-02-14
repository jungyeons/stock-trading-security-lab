package com.stocklab.api.dto;

import java.math.BigDecimal;
import java.util.List;

public class PortfolioDtos {
    public record PositionResponse(
            String symbol,
            String name,
            Integer quantity,
            BigDecimal avgPrice,
            BigDecimal currentPrice,
            BigDecimal unrealizedPl
    ) {}

    public record PortfolioResponse(
            BigDecimal cashBalance,
            BigDecimal totalAssetValue,
            BigDecimal totalUnrealizedPl,
            List<PositionResponse> positions
    ) {}
}
