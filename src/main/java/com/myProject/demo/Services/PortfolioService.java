package com.myProject.demo.Services;

import com.myProject.demo.DTO.PortfolioItemResponse;
import com.myProject.demo.DTO.PortfolioRequest;
import com.myProject.demo.DTO.PortfolioResponse;
import com.myProject.demo.Models.Asset;
import com.myProject.demo.Models.Portfolio;
import com.myProject.demo.Models.PortfolioItem;
import com.myProject.demo.Models.User;
import com.myProject.demo.Repositories.AssetRepo;
import com.myProject.demo.Repositories.PortfolioItemRepo;
import com.myProject.demo.Repositories.PortfolioRepo;
import com.myProject.demo.Repositories.UserRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepo portfolioRepo;

    @Autowired
    private PortfolioItemRepo itemRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AssetRepo assetRepo;

    @Autowired
    private ModelMapper modelMapper;


    @CacheEvict(value="portfolios",allEntries = true)
   public PortfolioResponse CreatePortfolio(PortfolioRequest portfReq){
       User user=userRepo.findByusername(portfReq.getUsername())
               .orElseThrow(() -> new RuntimeException("user not found"));

       Portfolio portf=new Portfolio();
       portf.setUser(user);
       //portf.setCreated_at(LocalDateTime.now());

    portfolioRepo.save(portf);
    PortfolioResponse response=new PortfolioResponse();
    response.setId(portf.getId());
    response.setCreatedAt(LocalDateTime.now());
    response.setUsername(portfReq.getUsername());



     return response;

   }
    @Caching(evict = {
            @CacheEvict(value = "portfolios", key = "#id"),
            @CacheEvict(value = "portfoliosList", allEntries = true)
    })
    public PortfolioResponse updatePortfolio(Long id, PortfolioRequest request) {
        Portfolio portfolio = portfolioRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));


        itemRepo.deleteAll(portfolio.getPortfolioItems());

        List<PortfolioItem> items = request.getItems().stream().map(itemReq -> {
            Asset asset = assetRepo.findAssetByName(itemReq.getAssetname())
                    .orElseThrow(() -> new RuntimeException("Asset not found: " + itemReq.getAssetname()));
            PortfolioItem item = new PortfolioItem();
            item.setPortfolio(portfolio);
            item.setAsset(asset);
            item.setQuantity(itemReq.getQuantity());
            item.setAverage_buy_price(itemReq.getAverage_buy_price());
            item.setUpdated_at(LocalDateTime.now());
            return item;
        }).collect(Collectors.toList());

        itemRepo.saveAll(items);
        portfolio.setPortfolioItems(items);

        return modelMapper.map(portfolio, PortfolioResponse.class);
    }

    @Cacheable("portfoliosList")
    public List<PortfolioResponse> getAllPortfolios() {
        return portfolioRepo.findAll().stream()
                .map(p -> modelMapper.map(p, PortfolioResponse.class))
                .collect(Collectors.toList());
    }
    @Cacheable(value="portfolios",key="#username")
    public PortfolioResponse getPortfolioByUsername(String username) {
        Portfolio portfolio = portfolioRepo.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Portfolio not found for user"));


        PortfolioResponse response = new PortfolioResponse();
        response.setId(portfolio.getId());
        response.setUsername(portfolio.getUser().getUsername());
        response.setCreatedAt(portfolio.getCreated_at());
        response.setTotalPNL(portfolio.getTotalPNL());


        List<PortfolioItemResponse> items = new ArrayList<>();
        if (portfolio.getPortfolioItems() != null) {
            for (PortfolioItem item : portfolio.getPortfolioItems()) {
                PortfolioItemResponse itemResponse = new PortfolioItemResponse();
                itemResponse.setAssetName(item.getAsset().getName());
                itemResponse.setQuantity(item.getQuantity());
                itemResponse.setAverage_buy_price(item.getAverage_buy_price());
                itemResponse.setUpdated_at(item.getUpdated_at());
                items.add(itemResponse);
            }
        }
        response.setPortfolioItems(items);

        return response;
    }

    @Caching(evict = {
            @CacheEvict(value = "portfolios", key = "#id"),
            @CacheEvict(value = "portfoliosList", allEntries = true)
    })
    public void deletePortfolio(Long id) {
        Portfolio portfolio = portfolioRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        portfolioRepo.delete(portfolio);
    }


}
