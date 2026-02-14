package com.stocklab.api;

import com.stocklab.api.dto.StockDtos;
import com.stocklab.service.StockService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stocks")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public List<StockDtos.StockResponse> list() {
        return stockService.listActive().stream()
                .map(s -> new StockDtos.StockResponse(s.getId(), s.getSymbol(), s.getName(), s.getCurrentPrice(), s.isActive()))
                .toList();
    }

    @GetMapping("/{id}")
    public StockDtos.StockResponse get(@PathVariable Long id) {
        var s = stockService.getById(id);
        return new StockDtos.StockResponse(s.getId(), s.getSymbol(), s.getName(), s.getCurrentPrice(), s.isActive());
    }
}
