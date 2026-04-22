package com.myProject.demo.Services;

import com.myProject.demo.DTO.AssetDTO;
import com.myProject.demo.DTO.AssetUpdateDTO;
import com.myProject.demo.Exceptions.AssetNotFoundException;
import com.myProject.demo.Models.Asset;
import com.myProject.demo.Repositories.AssetRepo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AssetService {

    @Autowired
    private AssetRepo assetRepo;
    @Autowired
    private  ModelMapper modelMapper;

   // @Cacheable("assets")
    public List<AssetDTO> findAll() {
        List<AssetDTO> result = assetRepo.findAllAssets();
        log.info("Assets count: " + result.size());
        return result;
    }
   // @CacheEvict(value = {"assets", "assetbyid"}, allEntries = true)
      public AssetDTO findbyId(Long id)
      {
          Asset asset = assetRepo.findById(id)
                  .orElseThrow(() -> new AssetNotFoundException("Asset not found with id: " + id));
          return modelMapper.map(asset, AssetDTO.class);
      }

    //@CacheEvict(value = "assets", allEntries = true)
    public AssetDTO AddAsset(AssetDTO assetdto) {
        Asset asset = modelMapper.map(assetdto, Asset.class);
        asset.setName(asset.getName().trim().toUpperCase());
        Asset saved = assetRepo.save(asset);
        return modelMapper.map(saved, AssetDTO.class);
    }
    //@CacheEvict(value="assets", allEntries=true)
      public AssetDTO updateAsset(AssetUpdateDTO assetdto, Long id)
      {
          Asset assetfromdb = assetRepo.findById(id)
                  .orElseThrow(() -> new AssetNotFoundException("Asset not found with id: " + id));
          modelMapper.map(assetdto, assetfromdb);
          assetfromdb.setName(assetfromdb.getName().trim().toUpperCase());
     assetRepo.save(assetfromdb);
     return modelMapper.map(assetfromdb, AssetDTO.class);

      }


}
