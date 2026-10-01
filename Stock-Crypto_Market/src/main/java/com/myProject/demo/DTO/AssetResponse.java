package com.myProject.demo.DTO;

import com.myProject.demo.Enums.AssetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
public class AssetResponse {

    private Long id;
    @NotBlank(message = "asset name is required")
    private String name;
    @NotBlank
    private String symbol;
    
    private AssetType type;
    @NotNull
    private BigDecimal currentPrice;
}
