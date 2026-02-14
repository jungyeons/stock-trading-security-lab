package com.stocklab.api;

import com.stocklab.api.dto.OrderDtos;
import com.stocklab.service.TradingService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final TradingService tradingService;

    public OrderController(TradingService tradingService) {
        this.tradingService = tradingService;
    }

    @PostMapping
    public OrderDtos.OrderResponse create(
            Principal principal,
            @Valid @RequestBody OrderDtos.CreateOrderRequest request
    ) {
        var order = tradingService.createOrder(principal.getName(), request.stockId(), request.side(), request.quantity());
        return new OrderDtos.OrderResponse(
                order.getId(),
                order.getStock().getSymbol(),
                order.getSide(),
                order.getQuantity(),
                order.getExecutedPrice(),
                order.getTotalAmount(),
                order.getCreatedAt()
        );
    }

    @GetMapping
    public List<OrderDtos.OrderResponse> list(Principal principal) {
        return tradingService.listOrders(principal.getName()).stream()
                .map(order -> new OrderDtos.OrderResponse(
                        order.getId(),
                        order.getStock().getSymbol(),
                        order.getSide(),
                        order.getQuantity(),
                        order.getExecutedPrice(),
                        order.getTotalAmount(),
                        order.getCreatedAt()
                ))
                .toList();
    }
}
