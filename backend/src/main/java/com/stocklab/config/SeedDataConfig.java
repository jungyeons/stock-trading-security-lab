package com.stocklab.config;

import com.stocklab.model.AppUser;
import com.stocklab.model.OrderSide;
import com.stocklab.model.Position;
import com.stocklab.model.Role;
import com.stocklab.model.Stock;
import com.stocklab.model.TradeOrder;
import com.stocklab.repository.PositionRepository;
import com.stocklab.repository.StockRepository;
import com.stocklab.repository.TradeOrderRepository;
import com.stocklab.repository.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SeedDataConfig {
    @Bean
    CommandLineRunner seedData(
            UserRepository userRepository,
            StockRepository stockRepository,
            PositionRepository positionRepository,
            TradeOrderRepository tradeOrderRepository,
            PasswordEncoder encoder
    ) {
        return args -> {
            if (userRepository.count() == 0) {
                AppUser user = new AppUser();
                user.setUsername("user1");
                user.setPasswordHash(encoder.encode("User1234!"));
                user.setRole(Role.USER);
                user.setFullName("Demo User");
                userRepository.save(user);

                AppUser admin = new AppUser();
                admin.setUsername("admin1");
                admin.setPasswordHash(encoder.encode("Admin1234!"));
                admin.setRole(Role.ADMIN);
                admin.setFullName("Demo Admin");
                userRepository.save(admin);
            }

            if (stockRepository.count() == 0) {
                List<Stock> stocks = List.of(
                        create("AAPL", "Apple Inc.", "188.25"),
                        create("MSFT", "Microsoft Corp.", "421.77"),
                        create("GOOGL", "Alphabet Inc.", "174.91"),
                        create("AMZN", "Amazon.com Inc.", "182.13"),
                        create("TSLA", "Tesla Inc.", "207.48"),
                        create("NVDA", "NVIDIA Corp.", "910.04"),
                        create("META", "Meta Platforms Inc.", "496.10"),
                        create("NFLX", "Netflix Inc.", "612.43"),
                        create("AMD", "Advanced Micro Devices", "182.57"),
                        create("INTC", "Intel Corp.", "43.16")
                );
                stockRepository.saveAll(stocks);
            }

            if (tradeOrderRepository.count() == 0) {
                AppUser user = userRepository.findByUsername("user1").orElseThrow();
                Stock aapl = stockRepository.findBySymbol("AAPL").orElseThrow();
                BigDecimal qty = BigDecimal.valueOf(3);
                BigDecimal total = aapl.getCurrentPrice().multiply(qty);
                user.setCashBalance(user.getCashBalance().subtract(total));
                TradeOrder order = new TradeOrder();
                order.setUser(user);
                order.setStock(aapl);
                order.setSide(OrderSide.BUY);
                order.setQuantity(3);
                order.setExecutedPrice(aapl.getCurrentPrice());
                order.setTotalAmount(total);
                tradeOrderRepository.save(order);

                Position p = new Position();
                p.setUser(user);
                p.setStock(aapl);
                p.setQuantity(3);
                p.setAvgPrice(aapl.getCurrentPrice());
                positionRepository.save(p);
                userRepository.save(user);
            }
        };
    }

    private Stock create(String symbol, String name, String price) {
        Stock stock = new Stock();
        stock.setSymbol(symbol);
        stock.setName(name);
        stock.setCurrentPrice(new BigDecimal(price));
        stock.setActive(true);
        return stock;
    }
}
