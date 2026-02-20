package com.myProject.demo.Conrollers;


import com.myProject.demo.DTO.AssetPriceRequest;
import com.myProject.demo.DTO.AssetPriceResponse;
import com.myProject.demo.Services.AssetPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assetprices")
public class AssetPriceController {
    @Autowired
    private AssetPriceService assetPriceService;

    @PostMapping("/add")
    public AssetPriceResponse AddAssetPrice(@RequestBody AssetPriceRequest assetPriceRequest)
    {
        return assetPriceService.AddAssetPrice(assetPriceRequest);
    }
    @GetMapping("/{assetname}")
    public List<AssetPriceResponse> findAssetPricesByAssetname(@PathVariable String assetname )
    {
       return assetPriceService.findAssetPricesByAssetname(assetname);
    }

}
