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
public class BuyTradeRequest {

    @Positive(message = "Quantity must be greater than zero")
    private BigDecimal  quantity;
    @NotBlank(message="asset name is required")
    private String assetName;
    @NotBlank(message="username is required")
    private String username;
    @NotNull(message="portfolio id is required")
    private Long portfolioId;














}
