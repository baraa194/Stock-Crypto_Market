package com.myProject.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssetAnalyticsResponse {

    private String assetName;

    private BigDecimal quantity;

    private BigDecimal averageBuyPrice;
    private BigDecimal currentPrice;

    private BigDecimal costBasis;
    private BigDecimal marketValue;

    private BigDecimal unrealizedPNL;
    private BigDecimal returnPercentage;

    private BigDecimal allocationPercentage;
}
