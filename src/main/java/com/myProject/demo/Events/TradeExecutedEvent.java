package com.myProject.demo.Events;

import com.myProject.demo.Enums.TradeType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TradeExecutedEvent {
    private Long tradeId;
    private BigDecimal price;
    private BigDecimal quantity;
    private String assetName;
    private String username;
    private Long portfolioId;
    private  TradeType type;
    private  LocalDateTime executedAt;
}
