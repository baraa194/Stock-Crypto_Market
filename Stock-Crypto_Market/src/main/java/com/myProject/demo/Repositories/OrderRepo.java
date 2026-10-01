package com.myProject.demo.Repositories;

import com.myProject.demo.Enums.OrderStatus;
import com.myProject.demo.Enums.OrderType;
import com.myProject.demo.Enums.TradeType;
import com.myProject.demo.Models.Order;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order,Long> {
    List<Order> findByPortfolioId(Long portfolioId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Optional<Order> findByIdForUpdate(@Param("id")Long orderId);


    List<Order> findByPortfolioIdAndStatus(Long portfolioId, OrderStatus status);

    List<Order> findByAssetNameAndTypeAndStatusAndOrderTypeAndTargetPriceGreaterThanEqual(
            String assetName,
            TradeType type,
            OrderStatus status,
            OrderType orderType,
              BigDecimal currentPrice
    );

    List<Order> findByAssetNameAndTypeAndStatusAndOrderTypeAndTargetPriceLessThanEqual(
            String assetName,
            TradeType type,
            OrderStatus status,
            OrderType orderType,
             BigDecimal currentPrice
    );


}
