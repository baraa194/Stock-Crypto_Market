package com.myProject.demo.DTO;

import com.myProject.demo.Models.Asset;
import com.myProject.demo.Models.Portfolio;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioItemRequest {

    @Positive(message="quantity must be greater than 0")
    private BigDecimal quantity;
    @NotNull
    private BigDecimal average_buy_price;
    private LocalDateTime updated_at;
    @NotBlank(message="assetname is required")
    private String assetname;
}
