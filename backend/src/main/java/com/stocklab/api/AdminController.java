package com.stocklab.api;

import com.stocklab.api.dto.AdminDtos;
import com.stocklab.api.dto.AuditDtos;
import com.stocklab.api.dto.StockDtos;
import com.stocklab.model.Stock;
import com.stocklab.repository.AuditLogRepository;
import com.stocklab.repository.UserRepository;
import com.stocklab.service.AuditService;
import com.stocklab.service.StockService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final StockService stockService;
    private final AuditLogRepository auditLogRepository;
    private final AuditService auditService;

    public AdminController(
            UserRepository userRepository,
            StockService stockService,
            AuditLogRepository auditLogRepository,
            AuditService auditService
    ) {
        this.userRepository = userRepository;
        this.stockService = stockService;
        this.auditLogRepository = auditLogRepository;
        this.auditService = auditService;
    }

    @GetMapping("/users")
    public List<AdminDtos.UserSummary> users() {
        return userRepository.findAll().stream()
                .map(u -> new AdminDtos.UserSummary(u.getId(), u.getUsername(), u.getRole().name(), u.getFullName()))
                .toList();
    }

    @GetMapping("/stocks")
    public List<StockDtos.StockResponse> allStocks() {
        return stockService.listAll().stream()
                .map(s -> new StockDtos.StockResponse(s.getId(), s.getSymbol(), s.getName(), s.getCurrentPrice(), s.isActive()))
                .toList();
    }

    @PostMapping("/stocks")
    public StockDtos.StockResponse createStock(
            Principal principal,
            @Valid @RequestBody StockDtos.CreateOrUpdateStockRequest request
    ) {
        Stock stock = new Stock();
        stock.setSymbol(request.symbol().toUpperCase());
        stock.setName(request.name());
        stock.setCurrentPrice(request.currentPrice());
        stock.setActive(request.active());
        stock = stockService.save(stock);
        auditService.log("ADMIN_STOCK_CREATE", principal.getName(), stock.getSymbol());
        return new StockDtos.StockResponse(stock.getId(), stock.getSymbol(), stock.getName(), stock.getCurrentPrice(), stock.isActive());
    }

    @PutMapping("/stocks/{id}")
    public StockDtos.StockResponse updateStock(
            Principal principal,
            @PathVariable Long id,
            @Valid @RequestBody StockDtos.CreateOrUpdateStockRequest request
    ) {
        Stock stock = stockService.getById(id);
        stock.setSymbol(request.symbol().toUpperCase());
        stock.setName(request.name());
        stock.setCurrentPrice(request.currentPrice());
        stock.setActive(request.active());
        stock = stockService.save(stock);
        auditService.log("ADMIN_STOCK_UPDATE", principal.getName(), stock.getSymbol());
        return new StockDtos.StockResponse(stock.getId(), stock.getSymbol(), stock.getName(), stock.getCurrentPrice(), stock.isActive());
    }

    @GetMapping("/audits")
    public List<AuditDtos.AuditResponse> audits() {
        return auditLogRepository.findTop100ByOrderByCreatedAtDesc().stream()
                .map(a -> new AuditDtos.AuditResponse(a.getId(), a.getEventType(), a.getActor(), a.getDetails(), a.getCreatedAt()))
                .toList();
    }
}
