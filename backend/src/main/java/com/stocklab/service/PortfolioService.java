package com.stocklab.service;

import com.stocklab.api.dto.PortfolioDtos;
import com.stocklab.model.AppUser;
import com.stocklab.repository.PositionRepository;
import com.stocklab.repository.UserRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.springframework.stereotype.Service;

@Service
public class PortfolioService {
    private final UserRepository userRepository;
    private final PositionRepository positionRepository;

    public PortfolioService(UserRepository userRepository, PositionRepository positionRepository) {
        this.userRepository = userRepository;
        this.positionRepository = positionRepository;
    }

    public PortfolioDtos.PortfolioResponse getPortfolio(String username) {
        AppUser user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("user not found"));
        var positions = positionRepository.findByUser(user);
        var responseItems = new ArrayList<PortfolioDtos.PositionResponse>();
        BigDecimal totalUnrealized = BigDecimal.ZERO;
        BigDecimal totalMarketValue = BigDecimal.ZERO;

        for (var p : positions) {
            BigDecimal current = p.getStock().getCurrentPrice();
            BigDecimal marketValue = current.multiply(BigDecimal.valueOf(p.getQuantity()));
            BigDecimal cost = p.getAvgPrice().multiply(BigDecimal.valueOf(p.getQuantity()));
            BigDecimal pl = marketValue.subtract(cost);
            totalUnrealized = totalUnrealized.add(pl);
            totalMarketValue = totalMarketValue.add(marketValue);
            responseItems.add(new PortfolioDtos.PositionResponse(
                    p.getStock().getSymbol(),
                    p.getStock().getName(),
                    p.getQuantity(),
                    p.getAvgPrice(),
                    current,
                    pl
            ));
        }
        return new PortfolioDtos.PortfolioResponse(
                user.getCashBalance(),
                user.getCashBalance().add(totalMarketValue),
                totalUnrealized,
                responseItems
        );
    }
}
