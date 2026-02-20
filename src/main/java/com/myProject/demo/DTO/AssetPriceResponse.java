package com.myProject.demo.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetPriceResponse {

    private BigDecimal price;
    private LocalDateTime recordedAt;
    @JsonProperty("assetName")
    private String AssetName;


}
