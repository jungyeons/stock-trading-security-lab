package com.stocklab.repository;

import com.stocklab.model.Stock;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByActiveTrueOrderBySymbolAsc();
    Optional<Stock> findBySymbol(String symbol);
}
