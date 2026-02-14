package com.stocklab.api.dto;

import com.stocklab.model.OrderSide;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

public class OrderDtos {
    public record CreateOrderRequest(
            @NotNull Long stockId,
            @NotNull OrderSide side,
            @NotNull @Min(1) Integer quantity
    ) {}

    public record OrderResponse(
            Long orderId,
            String symbol,
            OrderSide side,
            Integer quantity,
            BigDecimal executedPrice,
            BigDecimal totalAmount,
            Instant createdAt
    ) {}
}
