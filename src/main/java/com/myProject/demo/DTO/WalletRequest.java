package com.myProject.demo.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletRequest {

    @NotNull
    private Long user_id;
    @PositiveOrZero(message = "Balance cannot be negative")
    private BigDecimal balance;
    @NotBlank(message="currency required")
    @Size(min = 2, max =4 )
    private String currency;   // USD, EUR, BTC, USDT...

}
