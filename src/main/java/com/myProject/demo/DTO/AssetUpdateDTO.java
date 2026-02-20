package com.myProject.demo.DTO;

import com.myProject.demo.Enums.AssetType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetUpdateDTO {
    //private Long id;
    private String name;
    private String symbol;
    private AssetType type;
   // private BigDecimal currentPrice;
}
