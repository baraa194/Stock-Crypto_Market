package com.myProject.demo.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletResponse {

    private Long user_id;
    private BigDecimal balance;
    private String currency;   // USD, EUR, BTC, USDT...
     private LocalDateTime updatedAt;

}
