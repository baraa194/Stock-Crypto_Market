package com.myProject.demo.Models;

import com.myProject.demo.Enums.AssetType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String symbol;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AssetType type;
    private BigDecimal currentPrice;


    @OneToMany (mappedBy = "asset")
    private List<AssetPrice> asset_prices;

    @OneToMany(mappedBy="asset")
    private List<Trade> trades;

    @OneToMany(mappedBy = "asset")
    private List<PortfolioItem> portfolio_items;


}
