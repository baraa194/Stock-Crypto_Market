package com.myProject.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioAnalyticsResponse {

    private Long portfolioId;
    private String userName;

    private BigDecimal totalCostBasis;
    private BigDecimal totalMarketValue;

    private BigDecimal realizedPNL;
    private BigDecimal unrealizedPNL;
    private BigDecimal totalPNL;

    private BigDecimal returnPercentage;

    private List<AssetAnalyticsResponse> assets;
}