package com.myProject.demo.DTO;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class WalletTransactionRequest {
    private Long walletId;
    private BigDecimal amount;
}
