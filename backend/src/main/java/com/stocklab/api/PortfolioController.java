package com.stocklab.api;

import com.stocklab.api.dto.PortfolioDtos;
import com.stocklab.service.PortfolioService;
import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {
    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping
    public PortfolioDtos.PortfolioResponse summary(Principal principal) {
        return portfolioService.getPortfolio(principal.getName());
    }
}
