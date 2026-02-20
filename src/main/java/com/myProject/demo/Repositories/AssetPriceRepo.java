package com.myProject.demo.Repositories;

import com.myProject.demo.DTO.AssetPriceRequest;
import com.myProject.demo.DTO.AssetPriceResponse;
import com.myProject.demo.Models.Asset;
import com.myProject.demo.Models.AssetPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AssetPriceRepo extends JpaRepository<AssetPrice, Long> {


    @Query("select new com.myProject.demo.DTO.AssetPriceResponse (ap.price,ap.recordedAt,ap.asset.name)" +
            " from AssetPrice ap where ap.asset.name =:assetname ")
    List<AssetPriceResponse> findAllByAssetname(@Param("assetname") String assetname);


    @Query("select new com.myProject.demo.DTO.AssetPriceRequest (ap.price,ap.asset.name)" +
            " from AssetPrice ap ")
    List<AssetPriceRequest> findAllPrices();




}
