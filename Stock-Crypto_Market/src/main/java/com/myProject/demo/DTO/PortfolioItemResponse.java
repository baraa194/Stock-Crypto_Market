package com.myProject.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioItemResponse {
    private String assetName;
    private BigDecimal quantity;
    private BigDecimal average_buy_price;
    private LocalDateTime updated_at;


}
