package com.myProject.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellTradeResponse {
    private BigDecimal price_at_trade;
    private BigDecimal quantity;
    private String assetName;
    private String username;
    private Long portfolioId;
    private BigDecimal realizedPNL;
    private BigDecimal unrealizedPNL;
}
