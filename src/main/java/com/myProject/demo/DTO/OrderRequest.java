package com.myProject.demo.DTO;

import com.myProject.demo.Enums.TradeType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private String username;

    private Long portfolioId;

    private String assetName;

    private TradeType type;

    private BigDecimal quantity;

    private BigDecimal targetPrice;
}