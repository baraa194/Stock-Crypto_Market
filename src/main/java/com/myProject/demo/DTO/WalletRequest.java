package com.myProject.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletRequest {

    private Long user_id;
    private BigDecimal balance;
    private String currency;   // USD, EUR, BTC, USDT...

}
