package com.myProject.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyTradeResponse {
    private BigDecimal quantity;
    private String assetName;
    private String username;
    private Long portfolioId;
    private BigDecimal PNL;
}
