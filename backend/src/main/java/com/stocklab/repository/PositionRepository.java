package com.stocklab.repository;

import com.stocklab.model.AppUser;
import com.stocklab.model.Position;
import com.stocklab.model.Stock;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
    List<Position> findByUser(AppUser user);
    Optional<Position> findByUserAndStock(AppUser user, Stock stock);
}
