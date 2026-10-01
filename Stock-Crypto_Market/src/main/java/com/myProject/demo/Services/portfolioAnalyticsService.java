package com.myProject.demo.Services;

import com.myProject.demo.DTO.AssetAnalyticsResponse;
import com.myProject.demo.DTO.PortfolioAnalyticsResponse;
import com.myProject.demo.Exceptions.UserNotFoundException;
import com.myProject.demo.Models.Portfolio;
import com.myProject.demo.Models.PortfolioItem;
import com.myProject.demo.Repositories.PortfolioItemRepo;
import com.myProject.demo.Repositories.PortfolioRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@AllArgsConstructor
public class portfolioAnalyticsService {
    private final PortfolioRepo portfolioRepo;
    private final PortfolioItemRepo portfolioItemRepo;

    public PortfolioAnalyticsResponse calc (Long portfolioId) {
        Portfolio portfolio = portfolioRepo.findById(portfolioId)
                .orElseThrow(() -> new UserNotFoundException("portfolio not found"));
        List<AssetAnalyticsResponse> assets= portfolio.getPortfolioItems().stream()
                .map(item -> {

                    BigDecimal quantity = item.getQuantity();
                    BigDecimal averageBuyPrice = item.getAverage_buy_price();
                    BigDecimal currentPrice = item.getAsset().getCurrentPrice();
                    BigDecimal costBasis = quantity.multiply(averageBuyPrice);

                    BigDecimal marketValue = quantity.multiply(currentPrice);

                    BigDecimal unrealizedPNL = marketValue.subtract(costBasis);
                    BigDecimal returnPercentage =
                            costBasis.compareTo(BigDecimal.ZERO) == 0
                                    ? BigDecimal.ZERO
                                    : unrealizedPNL
                                    .divide(costBasis, 2, RoundingMode.HALF_UP)
                                    .multiply(BigDecimal.valueOf(100));
                    return new AssetAnalyticsResponse(
                            item.getAsset().getName(),
                            quantity,
                            averageBuyPrice,
                            currentPrice,
                            costBasis,
                            marketValue,
                            unrealizedPNL,
                            returnPercentage,
                            BigDecimal.ZERO
                    );
                }).toList();
        // calc totals
        BigDecimal totalCostBasis = assets.stream()
                .map(AssetAnalyticsResponse::getCostBasis)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalMarketValue = assets.stream()
                .map(AssetAnalyticsResponse::getMarketValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalUnrealizedPNL = assets.stream()
                .map(AssetAnalyticsResponse::getUnrealizedPNL)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal realizedPNL = portfolio.getTotalPNL();

        BigDecimal totalPNL = realizedPNL.add(totalUnrealizedPNL);
        BigDecimal returnPercentage =
                totalCostBasis.compareTo(BigDecimal.ZERO) == 0
                        ? BigDecimal.ZERO
                        : totalPNL
                        .divide(totalCostBasis, 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));



        List<AssetAnalyticsResponse> finalAssets = assets.stream()
                .map(asset -> {
                    BigDecimal allocationPercentage = totalMarketValue.compareTo(BigDecimal.ZERO) == 0
                            ? BigDecimal.ZERO
                            : asset.getMarketValue()
                            .divide(totalMarketValue, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .setScale(2, RoundingMode.HALF_UP);

                    return new AssetAnalyticsResponse(
                            asset.getAssetName(),
                            asset.getQuantity(),
                            asset.getAverageBuyPrice(),
                            asset.getCurrentPrice(),
                            asset.getCostBasis(),
                            asset.getMarketValue(),
                            asset.getUnrealizedPNL(),
                            asset.getReturnPercentage(),
                            allocationPercentage
                    );
                }).toList();
        return new PortfolioAnalyticsResponse(
                portfolio.getId(),
                portfolio.getUser().getUsername(),
                totalCostBasis,
                totalMarketValue,
                realizedPNL,
                totalUnrealizedPNL,
                totalPNL,
                returnPercentage,
                finalAssets
        );


    }

}
