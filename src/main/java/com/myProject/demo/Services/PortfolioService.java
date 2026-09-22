package com.myProject.demo.Services;

import com.myProject.demo.DTO.PortfolioItemRequest;
import com.myProject.demo.DTO.PortfolioItemResponse;
import com.myProject.demo.DTO.PortfolioRequest;
import com.myProject.demo.DTO.PortfolioResponse;
import com.myProject.demo.Exceptions.AssetNotFoundException;
import com.myProject.demo.Exceptions.PortfolioNotFoundException;
import com.myProject.demo.Exceptions.UserNotFoundException;
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
import java.util.Map;
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
               .orElseThrow(() -> new UserNotFoundException("user not found"));

       Portfolio portf=new Portfolio();
       portf.setUser(user);


    portfolioRepo.save(portf);
    PortfolioResponse response=new PortfolioResponse();
    response.setId(portf.getId());
    response.setCreated_at(LocalDateTime.now());
    response.setUsername(portfReq.getUsername());



     return response;

   }
//  @Caching(evict = {
//         @CacheEvict(value = "portfolios", key = "#id"),
//           @CacheEvict(value = "portfoliosList", allEntries = true)
//   })
  public PortfolioResponse updatePortfolio(String username, PortfolioRequest request) {
      Portfolio portfolio = portfolioRepo.findByUserUsername(username)
              .orElseThrow(() -> new PortfolioNotFoundException("Portfolio not found"));


      Map<Long, PortfolioItem> existingItems = portfolio.getPortfolioItems().stream()
              .collect(Collectors.toMap(item -> item.getAsset().getId(), item -> item));

      List<PortfolioItem> updatedItems = new ArrayList<>();

      for (PortfolioItemRequest itemReq : request.getItems()) {
          Asset asset = assetRepo.findAssetByName(itemReq.getAssetname())
                  .orElseThrow(() -> new AssetNotFoundException("Asset not found"));

          if (existingItems.containsKey(asset.getId())) {

              PortfolioItem existingItem = existingItems.get(asset.getId());
              existingItem.setQuantity(itemReq.getQuantity());
              existingItem.setAverage_buy_price(itemReq.getAverage_buy_price());
              existingItem.setUpdated_at(LocalDateTime.now());
              updatedItems.add(existingItem);
          } else {

              PortfolioItem newItem = new PortfolioItem();
              newItem.setPortfolio(portfolio);
              newItem.setAsset(asset);
              newItem.setQuantity(itemReq.getQuantity());
              newItem.setAverage_buy_price(itemReq.getAverage_buy_price());
              newItem.setUpdated_at(LocalDateTime.now());
              updatedItems.add(newItem);
          }
      }


      itemRepo.saveAll(updatedItems);
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
                .orElseThrow(() -> new PortfolioNotFoundException("Portfolio not found for user"));


        PortfolioResponse response = new PortfolioResponse();
        response.setId(portfolio.getId());
        response.setUsername(portfolio.getUser().getUsername());
        response.setCreated_at(portfolio.getCreated_at());
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
                .orElseThrow(() -> new PortfolioNotFoundException("Portfolio not found"));
        portfolioRepo.delete(portfolio);
    }


}
