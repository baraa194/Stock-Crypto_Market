package com.myProject.demo.Services;

import com.myProject.demo.DTO.AssetDTO;
import com.myProject.demo.DTO.AssetUpdateDTO;
import com.myProject.demo.Models.Asset;
import com.myProject.demo.Repositories.AssetRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class AssetServiceTest {
    @Mock
    private AssetRepo assetRepo;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private AssetService assetService;

    private AssetDTO dto;
    private Asset asset;

    @BeforeEach
    public void setup(){
        dto = new AssetDTO();
        dto.setName("google");

        asset = new Asset();
        asset.setId(1L);
        asset.setName("google");
    }

    @Test
    public void addAssetShouldAddedSuccessfully() {


        Mockito.when(modelMapper.map(dto, Asset.class)).thenReturn(asset);
        Mockito.when(modelMapper.map(asset, AssetDTO.class))
                .thenReturn(dto);
        Mockito.when(assetRepo.save(asset)).thenReturn(asset);

        AssetDTO result=assetService.AddAsset(dto);

        //Assertions.assertNotNull(result);
        Assertions.assertEquals(dto.getName(),result.getName());

        Mockito.verify(modelMapper).map(dto, Asset.class);
        Mockito.verify(modelMapper).map(asset, AssetDTO.class);
        Mockito.verify(assetRepo).save(asset);

    }
    @Test
    public void deleteAssetShouldDeletedSuccessfully() {
        doNothing().when(assetRepo).deleteById(1L);
        assetService.deleteAsset(1L);
        Mockito.verify(assetRepo,times(1)).deleteById(1L);


    }
    @Test
    void addAssetShouldNotSaveWhenNameIsNull() {
        dto.setName(null);

        Assertions.assertThrows(NullPointerException.class, () -> {
            assetService.AddAsset(dto);
        });

        Mockito.verify(assetRepo, Mockito.never()).save(Mockito.any(Asset.class));
    }

    @Test
    void updateAssetShouldUpdatedSuccessfully() {
        AssetUpdateDTO updatedto = new AssetUpdateDTO();
        updatedto.setName("microsoft");
        AssetDTO resultDto = new AssetDTO();
        resultDto.setName("MICROSOFT");
        // mock find
        Mockito.when(assetRepo.findById(1L))
                .thenReturn(Optional.of(asset));
        Mockito.doNothing().when(modelMapper)
                .map(Mockito.any(AssetUpdateDTO.class), Mockito.any(Asset.class));

        Mockito.when(assetRepo.save(asset)).thenReturn(asset);
        Mockito.when(modelMapper.map(asset, AssetDTO.class))
                .thenReturn(resultDto);

        AssetDTO result = assetService.updateAsset(updatedto, 1L);
        Assertions.assertEquals("MICROSOFT", result.getName());

        Mockito.verify(modelMapper).map(updatedto, asset);
        Mockito.verify(assetRepo).save(asset);
        Mockito.verify(modelMapper).map(asset, AssetDTO.class);
      Mockito.verify(assetRepo).findById(1L);



    }




}
