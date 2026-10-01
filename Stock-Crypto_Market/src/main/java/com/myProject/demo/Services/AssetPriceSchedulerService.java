package com.myProject.demo.Services;

import com.myProject.demo.DTO.AssetPriceRequest;
import com.myProject.demo.Models.AssetPrice;
import com.myProject.demo.Repositories.AssetPriceRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetPriceSchedulerService {
    @Autowired
    private AssetPriceService assetPriceService;
    @Autowired
    private AssetPriceRepo assetPriceRepo;

    Logger log = LoggerFactory.getLogger(AssetPriceSchedulerService.class);

    @Scheduled(fixedRate = 15000)
    public void updateAssetPricesAutomatically() {
        List<AssetPriceRequest> latestPrices = fetchPricesFromAPI();

        // لو القائمة فاضية نطبع للتحقق
        if (latestPrices.isEmpty()) {
            log.info("No prices fetched from API/DB.");
            return;
        }

        Collection<AssetPriceRequest> uniquePrices = latestPrices.stream()
                .collect(Collectors.toMap(
                        AssetPriceRequest::getAssetName,
                        req -> req,
                        (existing, replacement) -> replacement
                )).values();

        for (AssetPriceRequest req : uniquePrices) {
            try {

                AssetPrice latestRecorded = assetPriceRepo
                        .findTopByAssetNameOrderByRecordedAtDesc(req.getAssetName())
                        .orElse(null);


                if (latestRecorded == null || latestRecorded.getPrice().compareTo(req.getPrice()) != 0) {
                    assetPriceService.AddAssetPrice(req);
                    log.info("Updated price for {} to {}", req.getAssetName(), req.getPrice());
                } else {

                    log.info("Price for {} hasn't changed ({}), skipping save.", req.getAssetName(), req.getPrice());
                }

            } catch (Exception e) {
                log.error("Error updating price for {}: ", req.getAssetName(), e);
            }
        }
    }

    public List<AssetPriceRequest> fetchPricesFromAPI() {
        return assetPriceRepo.findAllPrices();
    }
}