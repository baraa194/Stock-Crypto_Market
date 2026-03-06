package com.myProject.demo.Conrollers;

import com.myProject.demo.DTO.AssetDTO;
import com.myProject.demo.DTO.AssetUpdateDTO;
import com.myProject.demo.Repositories.AssetRepo;
import com.myProject.demo.Services.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assets")
public class AssetController {

    @Autowired
    private AssetService assetService;

     @PostMapping("/add")
    public AssetDTO AddAsset(@RequestBody AssetDTO assetdto)
     {
         System.out.println("Add Asset first");
         assetService.AddAsset(assetdto);
         System.out.println("Add Asset Success");
         return assetdto;

     }
     @PutMapping("/edit/{id}")
     public AssetDTO updateAsset(@RequestBody AssetUpdateDTO assetdto,@PathVariable Long id)
     {
         return  assetService.updateAsset(assetdto,id);
     }
     @GetMapping("/{id}")
     public AssetDTO findAssetById(@PathVariable Long id)
     {
       return assetService.findbyId(id);
     }
     @GetMapping("/getall")
    public List<AssetDTO> findAllAssets()
     {
         return assetService.findAll();
     }


}
