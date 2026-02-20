package com.myProject.demo.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private BigDecimal quantity;
    @Column(nullable = false)
    private BigDecimal average_buy_price;
    private LocalDateTime updated_at;


    @ManyToOne
    @JoinColumn(name="portfolio_id")
    private Portfolio portfolio;
    @ManyToOne
    @JoinColumn(name="asset_id")
    private Asset asset;
}
