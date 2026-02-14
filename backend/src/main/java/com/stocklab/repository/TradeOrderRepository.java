package com.stocklab.repository;

import com.stocklab.model.AppUser;
import com.stocklab.model.TradeOrder;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeOrderRepository extends JpaRepository<TradeOrder, Long> {
    List<TradeOrder> findByUserOrderByCreatedAtDesc(AppUser user);
}
