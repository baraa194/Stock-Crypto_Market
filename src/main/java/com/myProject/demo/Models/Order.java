package com.myProject.demo.Models;

import com.myProject.demo.Enums.OrderStatus;
import com.myProject.demo.Enums.TradeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeType type;


    @Column(nullable = false)
    private BigDecimal targetPrice;

    @Column(nullable = false)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime executedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        status = OrderStatus.PENDING;
    }




}
