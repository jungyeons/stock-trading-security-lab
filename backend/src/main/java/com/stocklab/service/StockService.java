package com.stocklab.service;

import com.stocklab.model.Stock;
import com.stocklab.repository.StockRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<Stock> listActive() {
        return stockRepository.findByActiveTrueOrderBySymbolAsc();
    }

    public List<Stock> listAll() {
        return stockRepository.findAll().stream().sorted((a, b) -> a.getSymbol().compareTo(b.getSymbol())).toList();
    }

    public Stock getById(Long id) {
        return stockRepository.findById(id).orElseThrow(() -> new NotFoundException("stock not found"));
    }

    @Transactional
    public Stock save(Stock stock) {
        return stockRepository.save(stock);
    }
}
