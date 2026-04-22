package com.myProject.demo.Conrollers;

import com.myProject.demo.DTO.AssetDTO;
import com.myProject.demo.DTO.AssetUpdateDTO;
import com.myProject.demo.Exceptions.AssetNotFoundException;
import com.myProject.demo.Repositories.AssetRepo;
import com.myProject.demo.Services.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assets")
public class AssetController {

    @Autowired
    private AssetService assetService;

     @PostMapping("/add")
    public ResponseEntity<AssetDTO> AddAsset(@RequestBody AssetDTO assetdto)
     {
         assetService.AddAsset(assetdto);
         return ResponseEntity.ok().body(assetdto);

     }
     @PutMapping("/edit/{id}")
     public ResponseEntity<AssetDTO>updateAsset(@RequestBody AssetUpdateDTO assetdto,@PathVariable Long id)
     {
         AssetDTO asset=  assetService.updateAsset(assetdto,id);
         return ResponseEntity.ok().body(asset);

     }
     @GetMapping("/{id}")
     public ResponseEntity<AssetDTO> findAssetById(@PathVariable Long id)
     {
       AssetDTO asset = assetService.findbyId(id);
         return ResponseEntity.ok().body(asset);
     }
     @GetMapping("/getall")
    public List<AssetDTO> findAllAssets()
     {
         return assetService.findAll();
     }


}
