package com.myProject.demo.DTO;

import com.myProject.demo.Enums.LedgerType;
import com.myProject.demo.Enums.ReferenceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LedgerEntryRequest {

    private Long walletId;
    private LedgerType type;

    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;

    private ReferenceType referenceType;
    private Long referenceId;

    private String description;
}