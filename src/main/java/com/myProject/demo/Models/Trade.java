package com.myProject.demo.Models;

import com.myProject.demo.Enums.TradeType;
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
public class Trade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeType type;
    @Column(nullable = false)
    private BigDecimal price_at_trade;
    @Column(nullable = false)
    private BigDecimal  quantity;
    @Column(nullable = false)
    private BigDecimal  total_amount;
    private LocalDateTime created_at;

    private BigDecimal PNL;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="asset_id")
    private Asset asset;

    @ManyToOne
    @JoinColumn(name="portfolio_id")
    private Portfolio portfolio;

}
