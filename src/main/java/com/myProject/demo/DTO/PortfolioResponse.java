package com.myProject.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioResponse {
    private Long id;
    private String username;
    private LocalDateTime createdAt;
    private BigDecimal totalPNL;
    private List<PortfolioItemResponse> portfolioItems;
}
