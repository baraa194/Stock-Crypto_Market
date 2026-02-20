package com.myProject.demo.DTO;

import com.myProject.demo.Enums.AssetType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetDTO {

    private String name;
    private String symbol;
    private AssetType type;
    private BigDecimal currentPrice;

}
