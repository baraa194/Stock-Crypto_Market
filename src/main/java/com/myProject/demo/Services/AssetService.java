package com.myProject.demo.Services;

import com.myProject.demo.DTO.AssetDTO;
import com.myProject.demo.DTO.AssetUpdateDTO;
import com.myProject.demo.Models.Asset;
import com.myProject.demo.Repositories.AssetRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetService {

    @Autowired
    private AssetRepo assetRepo;
    @Autowired
    private  ModelMapper modelMapper;

    @Cacheable("assets")
    public List<AssetDTO> findAll() {
        return assetRepo.findAllAssets();
    }
    @Cacheable(value = "assetbyid", key = "#id")
      public AssetDTO findbyId(Long id)
      {
          Asset asset = assetRepo.findById(id).get();
          return modelMapper.map(asset, AssetDTO.class);
      }
      public void AddAsset(AssetDTO assetdto)
      {
        Asset asset=modelMapper.map(assetdto,Asset.class);
        asset.setName(asset.getName().trim().toUpperCase());
        assetRepo.save(asset);
          System.out.println("Asset added successfully");

      }
    @CacheEvict(value="assets", allEntries=true)
      public AssetDTO updateAsset(AssetUpdateDTO assetdto, Long id)
      {


          Asset assetfromdb= assetRepo.findById(id)
        .orElseThrow(() -> new RuntimeException("Asset not found"));
          modelMapper.map(assetdto, assetfromdb);
          assetfromdb.setName(assetfromdb.getName().trim().toUpperCase());
     assetRepo.save(assetfromdb);
     return modelMapper.map(assetfromdb, AssetDTO.class);

      }


}
