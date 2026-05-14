package com.myProject.demo.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellTradeRequest {

    @NotNull
    @Positive
    private BigDecimal price_at_trade;
    @Positive(message="quantity must be greater than 0")
    private BigDecimal quantity;
    @NotBlank(message = "asset name is required")
    private String assetName;
    @NotBlank(message = "username is required")
    private String username;
    @NotNull(message = "portfolio id is required")
    private Long portfolioId;




}
