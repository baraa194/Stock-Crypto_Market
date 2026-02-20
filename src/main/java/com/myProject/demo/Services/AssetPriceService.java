package com.myProject.demo.Services;

import com.myProject.demo.DTO.AssetPriceRequest;
import com.myProject.demo.DTO.AssetPriceResponse;
import com.myProject.demo.Models.Asset;
import com.myProject.demo.Models.AssetPrice;
import com.myProject.demo.Repositories.AssetPriceRepo;
import com.myProject.demo.Repositories.AssetRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssetPriceService {

    @Autowired
   private AssetPriceRepo assetPriceRepo;
    @Autowired
    private AssetRepo assetRepo;
    @Autowired
    private  ModelMapper modelMapper;

    @CacheEvict(value="assets", allEntries=true)
    public AssetPriceResponse AddAssetPrice(AssetPriceRequest assetPriceRequest) {

        Asset asset=assetRepo.findAssetByName(assetPriceRequest.getAssetName().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        AssetPrice assetprice=new AssetPrice();
        assetprice.setPrice(assetPriceRequest.getPrice());
        assetprice.setRecordedAt(LocalDateTime.now());
        assetprice.setAsset(asset);
        assetPriceRepo.save(assetprice);

        asset.setCurrentPrice(assetPriceRequest.getPrice());
            assetRepo.save(asset);

            return modelMapper.map(assetprice,AssetPriceResponse.class);
    }
  @Cacheable(value="assets",key="#assetname")
    public List<AssetPriceResponse> findAssetPricesByAssetname(String assetname ) {

        if (assetname == null || assetname.trim().isEmpty()) {
            throw new IllegalArgumentException("Asset name cannot be empty");
        }

        String normalizedName = assetname.trim().toUpperCase();
        return assetPriceRepo.findAllByAssetname(normalizedName);

    }





}
