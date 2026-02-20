package com.myProject.demo.Services;

import com.myProject.demo.DTO.AssetPriceRequest;
import com.myProject.demo.DTO.AssetPriceResponse;
import com.myProject.demo.Repositories.AssetPriceRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssetPriceSchedulerService {
    @Autowired
    private AssetPriceService assetPriceService;
    @Autowired
    private AssetPriceRepo assetPriceRepo;

    Logger log = LoggerFactory.getLogger(AssetPriceSchedulerService.class);

//    boolean executed = false;
//    @Scheduled(fixedRate=86400000)
//   public void updateassetpricesAutomatically()
//   {
//       if(executed)
//           return;
//       executed=true;
//       List<AssetPriceRequest> latestprices=fetchPricesFromAPI();
//       for(AssetPriceRequest req:latestprices)
//       {
//           try {
//               assetPriceService.AddAssetPrice(req);
//               log.info("Updated {} at {}", req.getAssetName(), LocalDateTime.now());
//
//           }
//           catch (Exception e) {
//               System.out.println(e.getMessage());
//           }
//       }
//   }


    public List<AssetPriceRequest>fetchPricesFromAPI()
    {
        return assetPriceRepo.findAllPrices();

    }
}
