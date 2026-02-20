package com.myProject.demo.Config;

import com.myProject.demo.DTO.*;
import com.myProject.demo.Models.*;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class modelMapperConfig {
    @Bean
    public ModelMapper modelmapper()
    {
     ModelMapper mapper = new ModelMapper();
      mapper.typeMap(AssetPrice.class, AssetPriceResponse.class)
              .addMapping(src->src.getAsset().getName(),
                      AssetPriceResponse::setAssetName);

//      mapper.typeMap(Wallet.class, WalletResponse.class)
//              .addMapping(src->src.getUser().getUsername(,WalletResponse::setUsername);
//
//      mapper.typeMap(Wallet.class, WalletRequest.class)
//              .addMapping(src->src.getUser().getUsername(),WalletRequest::setUsername);

      mapper.typeMap(Portfolio.class, PortfolioRequest.class)
              .addMapping(src->src.getUser().getUsername(),PortfolioRequest::setUsername);
        mapper.typeMap(Portfolio.class, PortfolioResponse.class)
                .addMapping(src->src.getUser().getUsername(),PortfolioResponse::setUsername);

      mapper.typeMap(PortfolioItem.class, PortfolioItemResponse.class)
              .addMapping(src->src.getAsset().getName(),PortfolioItemResponse::setAssetName);

      mapper.typeMap(Trade.class, BuyTradeRequest.class)
              .addMapping(src->src.getAsset().getName(), BuyTradeRequest::setAssetName)
              .addMapping(src->src.getUser().getUsername(), BuyTradeRequest::setUsername)
              .addMapping(src->src.getPortfolio().getId(), BuyTradeRequest::setPortfolioId);




      return mapper;
    }
}
