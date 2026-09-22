package com.myProject.demo.DTO;

import com.myProject.demo.Enums.OrderStatus;
import com.myProject.demo.Enums.TradeType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;

    private String username;

    private Long portfolioId;

    private String assetName;

    private TradeType type;

    private BigDecimal quantity;

    private BigDecimal targetPrice;

    private OrderStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime executedAt;
}