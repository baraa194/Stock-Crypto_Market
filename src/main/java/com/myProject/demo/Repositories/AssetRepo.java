package com.myProject.demo.Repositories;

import com.myProject.demo.DTO.AssetDTO;
import com.myProject.demo.Models.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface AssetRepo extends JpaRepository<Asset, Long> {
    @Query("select new com.myProject.demo.DTO.AssetDTO(a.name,a.symbol,a.type,a.currentPrice)" +
            " from Asset a")
    List<AssetDTO> findAllAssets();

   // Optional<Asset> findAssetById(Long assetid);
    Optional<Asset> findAssetByName(String assetName);



}
