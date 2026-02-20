package com.myProject.demo.DTO;

import com.myProject.demo.Models.Asset;
import com.myProject.demo.Models.Portfolio;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioItemRequest {

    private BigDecimal quantity;
    private BigDecimal average_buy_price;
    private LocalDateTime updated_at;
    private String assetname;
}
