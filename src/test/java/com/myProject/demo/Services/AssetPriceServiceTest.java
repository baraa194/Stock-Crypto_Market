package com.myProject.demo.Services;

import com.myProject.demo.DTO.AssetPriceRequest;
import com.myProject.demo.DTO.AssetPriceResponse;
import com.myProject.demo.Exceptions.AssetNotFoundException;
import com.myProject.demo.Models.Asset;
import com.myProject.demo.Models.AssetPrice;
import com.myProject.demo.Repositories.AssetPriceRepo;
import com.myProject.demo.Repositories.AssetRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssetPriceServiceTest {
    @Mock
    AssetRepo assetRepo;
    @Mock
    ModelMapper modelMapper;
    @Mock
    AssetPriceRepo assetPriceRepo;
    @InjectMocks
    AssetPriceService assetPriceService;
    AssetPriceRequest assetPriceRequest;
    Asset asset;
    AssetPrice assetprice;
    AssetPriceResponse expectedResponse;

    @BeforeEach
    void setUp() {
        assetPriceRequest = new AssetPriceRequest();
        assetPriceRequest.setAssetName("MICROSOFT");
        assetPriceRequest.setPrice(BigDecimal.valueOf(5000.00));

        asset = new Asset();
        asset.setName("MICROSOFT");
        asset.setCurrentPrice(BigDecimal.valueOf(4000.00));

        assetprice = new AssetPrice();
        assetprice.setPrice(assetPriceRequest.getPrice());
        assetprice.setAsset(asset);

        expectedResponse = new AssetPriceResponse();
        expectedResponse.setPrice(BigDecimal.valueOf(5000.00));
    }

    @Test
    public void addAssetPrice_WhenAssetExists_ShouldSavePriceAndUpdateAsset() {

        when(assetRepo.findAssetByName("MICROSOFT"))
                .thenReturn(Optional.of(asset));

        when(assetPriceRepo.save(any(AssetPrice.class)))
                .thenReturn(assetprice);

        when(assetRepo.save(asset))
                .thenReturn(asset);

        when(modelMapper.map(any(AssetPrice.class), eq(AssetPriceResponse.class)))
                .thenReturn(expectedResponse);

        AssetPriceResponse result =
                assetPriceService.AddAssetPrice(assetPriceRequest);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(5000.00), result.getPrice());
        assertEquals(BigDecimal.valueOf(5000.00), asset.getCurrentPrice());

        verify(assetRepo).findAssetByName("MICROSOFT");
        verify(assetPriceRepo).save(any(AssetPrice.class));
        verify(assetRepo).save(asset);
        verify(modelMapper).map(any(AssetPrice.class), eq(AssetPriceResponse.class));
    }

    @Test
    void addAssetPrice_WhenAssetNotFound_ShouldThrowException() {

        when(assetRepo.findAssetByName("MICROSOFT"))
                .thenReturn(Optional.empty());

        assertThrows(
                AssetNotFoundException.class,
                () -> assetPriceService.AddAssetPrice(assetPriceRequest)
        );

        verify(assetRepo).findAssetByName("MICROSOFT");
        verify(assetPriceRepo, never()).save(any());
        verify(assetRepo, never()).save(any(Asset.class));
        verify(modelMapper, never()).map(any(), any());
    }
















}
