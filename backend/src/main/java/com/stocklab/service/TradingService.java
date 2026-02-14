package com.stocklab.service;

import com.stocklab.model.*;
import com.stocklab.repository.PositionRepository;
import com.stocklab.repository.TradeOrderRepository;
import com.stocklab.repository.UserRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TradingService {
    private final UserRepository userRepository;
    private final StockService stockService;
    private final PositionRepository positionRepository;
    private final TradeOrderRepository tradeOrderRepository;
    private final AuditService auditService;

    public TradingService(
            UserRepository userRepository,
            StockService stockService,
            PositionRepository positionRepository,
            TradeOrderRepository tradeOrderRepository,
            AuditService auditService
    ) {
        this.userRepository = userRepository;
        this.stockService = stockService;
        this.positionRepository = positionRepository;
        this.tradeOrderRepository = tradeOrderRepository;
        this.auditService = auditService;
    }

    @Transactional
    public TradeOrder createOrder(String username, Long stockId, OrderSide side, Integer quantity) {
        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("user not found"));
        Stock stock = stockService.getById(stockId);
        if (!stock.isActive()) {
            throw new BadRequestException("stock is inactive");
        }
        BigDecimal price = stock.getCurrentPrice();
        BigDecimal total = price.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);

        Position position = positionRepository.findByUserAndStock(user, stock).orElseGet(() -> {
            Position p = new Position();
            p.setUser(user);
            p.setStock(stock);
            return p;
        });

        if (side == OrderSide.BUY) {
            if (user.getCashBalance().compareTo(total) < 0) {
                throw new BadRequestException("insufficient cash");
            }
            int oldQty = position.getQuantity();
            BigDecimal oldCost = position.getAvgPrice().multiply(BigDecimal.valueOf(oldQty));
            BigDecimal newCost = oldCost.add(total);
            int newQty = oldQty + quantity;
            position.setQuantity(newQty);
            position.setAvgPrice(newCost.divide(BigDecimal.valueOf(newQty), 2, RoundingMode.HALF_UP));
            user.setCashBalance(user.getCashBalance().subtract(total));
        } else {
            if (position.getQuantity() < quantity) {
                throw new BadRequestException("insufficient holdings");
            }
            position.setQuantity(position.getQuantity() - quantity);
            user.setCashBalance(user.getCashBalance().add(total));
            if (position.getQuantity() == 0) {
                position.setAvgPrice(BigDecimal.ZERO);
            }
        }

        positionRepository.save(position);
        TradeOrder order = new TradeOrder();
        order.setUser(user);
        order.setStock(stock);
        order.setSide(side);
        order.setQuantity(quantity);
        order.setExecutedPrice(price);
        order.setTotalAmount(total);
        tradeOrderRepository.save(order);
        auditService.log("ORDER_CREATED", username, side + " " + stock.getSymbol() + " x" + quantity);
        return order;
    }

    public List<TradeOrder> listOrders(String username) {
        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("user not found"));
        return tradeOrderRepository.findByUserOrderByCreatedAtDesc(user);
    }
}
