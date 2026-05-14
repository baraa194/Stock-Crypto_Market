package com.myProject.demo.DTO;

import com.myProject.demo.Enums.AssetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetDTO {

    @NotBlank(message = "asset name is required")
    private String name;
    @NotBlank
    private String symbol;
    @NotBlank
    private AssetType type;
    @NotNull
    private BigDecimal currentPrice;

}
