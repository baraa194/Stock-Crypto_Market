package com.myProject.demo.Services;

import com.myProject.demo.DTO.*;
import com.myProject.demo.Enums.TradeType;
import com.myProject.demo.Events.TradeExecutedEvent;
import com.myProject.demo.Models.*;
import com.myProject.demo.Repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class TradeService {
    @Autowired
    private TradeRepo tradeRepo;
    @Autowired
    private PortfolioRepo portfolioRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AssetRepo assetRepo;
    @Autowired
    private WalletRepo walletRepo;
    @Autowired
    private PortfolioItemRepo portfolioItemRepo;

    private final ApplicationEventPublisher publisher;

    public TradeService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Transactional
     @CacheEvict(value = {
             "buytrades",
             "selltrades",
             "portfolios",
             "portfoliosList"
     }, allEntries = true)

     public void CreateBuyingTrade(BuyTradeRequest tradereq) {
         // get all objs from db
        User userfromdb= userRepo.findByusername(tradereq.getUsername())
                .orElseThrow(()->new RuntimeException("Username not found"));
        Asset assetfromdb=assetRepo.findAssetByName(tradereq.getAssetName())
                .orElseThrow(()->new RuntimeException("Asset name not found"));
        Portfolio portfoliofromdb=portfolioRepo.findById(tradereq.getPortfolioId())
                .orElseThrow(()->new RuntimeException("Portfolio id not found"));

        // update wallet ,trade
        if(tradereq.getQuantity().compareTo(BigDecimal.ZERO)>0)
        {
            Wallet userwallet= walletRepo.findByUserUsername(tradereq.getUsername());
            BigDecimal assetcurrentprice=assetfromdb.getCurrentPrice();

            BigDecimal totalcost=assetcurrentprice.multiply(tradereq.getQuantity());
            Trade trade=new Trade();
            trade.setPortfolio(portfoliofromdb);
            trade.setAsset(assetfromdb);
            trade.setCreated_at(LocalDateTime.now());
            trade.setPrice_at_trade(assetcurrentprice);
            trade.setQuantity(tradereq.getQuantity());
            trade.setUser(userfromdb);
            trade.setTotal_amount(totalcost);
            trade.setType(TradeType.BUY);





            if(userwallet.getBalance().compareTo(totalcost) >=0)
            {
             userwallet.setBalance(userwallet.getBalance().subtract(totalcost));
             userwallet.setUpdatedAt(LocalDateTime.now());
                walletRepo.save(userwallet);
            }
            else{
                throw new RuntimeException("Insufficient balance");
            }
            // update portfolio items
            Optional<PortfolioItem> existingitem= portfoliofromdb.getPortfolioItems().stream()
                    .filter(i->i.getAsset().getName().equals(tradereq.getAssetName()))
                    .findFirst();
                // update data
                // check if the item exist or new one
            if(!existingitem.isPresent())
            {
                PortfolioItem portfolioitem=new PortfolioItem();
                portfolioitem.setQuantity(tradereq.getQuantity());
                portfolioitem.setAverage_buy_price(assetcurrentprice);
                portfolioitem.setAsset(assetfromdb);
                portfolioitem.setPortfolio(portfoliofromdb);
                portfolioitem.setUpdated_at(LocalDateTime.now());
                portfoliofromdb.getPortfolioItems().add(portfolioitem);
                trade.setPNL(BigDecimal.ZERO);
                portfoliofromdb.setTotalPNL(BigDecimal.ZERO);
                //tradeRepo.save(trade);

                portfolioItemRepo.save(portfolioitem);
            }
            else{
                BigDecimal oldQty   = existingitem.get().getQuantity();
                BigDecimal oldAvg   = existingitem.get().getAverage_buy_price();
                BigDecimal newQty   = tradereq.getQuantity();
                BigDecimal newPrice = assetcurrentprice;
                PortfolioItem item = existingitem.get();
                BigDecimal totalCost = oldQty.multiply(oldAvg).add(newQty.multiply(newPrice));
                BigDecimal totalQty = oldQty.add(newQty);
                BigDecimal newAvgPrice = totalCost.divide(totalQty, 2, RoundingMode.HALF_EVEN);
                BigDecimal pnl=(assetcurrentprice.subtract(newAvgPrice).multiply(oldQty));
                item.setQuantity(totalQty);
                item.setAverage_buy_price(newAvgPrice);
                item.setUpdated_at(LocalDateTime.now());
                trade.setPNL(pnl);
               // tradeRepo.save(trade);
                // publish the event


                portfoliofromdb.setTotalPNL(
                        portfoliofromdb.getTotalPNL().add(pnl)
                );

                portfolioItemRepo.save(existingitem.get() );
            }

            portfolioRepo.save(portfoliofromdb);
            Trade trade2=tradeRepo.save(trade);
            System.out.println("\n=== 🔔 PUBLISHING TRADE EVENT ===");
            System.out.println("Trade ID: " + trade2.getId());
            System.out.println("Trade saved successfully in DB: " + trade2.getId());
            TradeExecutedEvent event = new TradeExecutedEvent(
                    trade2.getId(),
                    trade2.getPrice_at_trade(),
                    trade2.getQuantity(),
                    assetfromdb.getName(),
                    userfromdb.getUsername(),
                    portfoliofromdb.getId(),
                    TradeType.BUY,
                    LocalDateTime.now()

            );

            publisher.publishEvent(event);
        }
        else {
            throw new RuntimeException("Invalid request");
        }





         System.out.println("Trade is Success");
    }



    @Transactional
    @CacheEvict(value = {
            "buytrades",
            "selltrades",
            "portfolios",
            "portfoliosList"
    }, allEntries = true)
public void CreatesellingTrade(SellTradeRequest tradereq) {
    User userfromdb= userRepo.findByusername(tradereq.getUsername())
            .orElseThrow(()->new RuntimeException("Username not found"));
    Asset assetfromdb=assetRepo.findAssetByName(tradereq.getAssetName())
            .orElseThrow(()->new RuntimeException("Asset name not found"));
    Portfolio portfoliofromdb=portfolioRepo.findById(tradereq.getPortfolioId())
            .orElseThrow(()->new RuntimeException("Portfolio id not found"));
    PortfolioItem portfolioitem=portfoliofromdb.getPortfolioItems().stream()
            .filter(i->i.getAsset().getName().equals(tradereq.getAssetName()))
            .findFirst().orElseThrow(() -> new RuntimeException("Asset not found in portfolio"));

     if(tradereq.getQuantity().compareTo(BigDecimal.ZERO)>0 &&
     tradereq.getQuantity().compareTo(portfolioitem.getQuantity()) <=0)
     {
         Wallet userwallet= walletRepo.findByUserUsername(tradereq.getUsername());
         BigDecimal assetcurrentprice=assetfromdb.getCurrentPrice();
         BigDecimal totalcost=assetcurrentprice.multiply(tradereq.getQuantity());
         userwallet.setBalance(userwallet.getBalance().add(totalcost));
         userwallet.setUpdatedAt(LocalDateTime.now());
         walletRepo.save(userwallet);

         BigDecimal realizedPNL=(tradereq.getPrice_at_trade().subtract(portfolioitem.getAverage_buy_price()))
                 .multiply(tradereq.getQuantity());
         // add to total pnl
         portfoliofromdb.setTotalPNL(portfoliofromdb.getTotalPNL().add(realizedPNL));
         Trade trade=new Trade();
         trade.setPortfolio(portfoliofromdb);
         trade.setAsset(assetfromdb);
         trade.setUser(userfromdb);
         trade.setCreated_at(LocalDateTime.now());
         trade.setPrice_at_trade(tradereq.getPrice_at_trade());
         trade.setQuantity(tradereq.getQuantity());
         trade.setTotal_amount(totalcost);
         trade.setType(TradeType.SELL);
         trade.setPNL(realizedPNL);
         //tradeRepo.save(trade);


         //sell all quantity
         if(tradereq.getQuantity().compareTo(portfolioitem.getQuantity())==0)
         {
           portfoliofromdb.getPortfolioItems().remove(portfolioitem);

         }
         else {
             portfolioitem.setQuantity(portfolioitem.getQuantity().subtract(tradereq.getQuantity()));
             portfolioitem.setUpdated_at(LocalDateTime.now());

             portfolioRepo.save(portfoliofromdb);
         }
         portfolioRepo.save(portfoliofromdb);

         Trade trade2=tradeRepo.save(trade);
         TradeExecutedEvent event = new TradeExecutedEvent(
                 trade2.getId(),
                 trade2.getPrice_at_trade(),
                 trade2.getQuantity(),
                 assetfromdb.getName(),
                 userfromdb.getUsername(),
                 portfoliofromdb.getId(),
                 TradeType.SELL,
                 LocalDateTime.now()

         );

         publisher.publishEvent(event);

     }


     else  {
         throw new RuntimeException("Invalid quantity");
     }

    System.out.println("Trade Successfully created");


}
@Cacheable(value="buytrades",key="#portfolioid + '_' + #type")
public List<BuyTradeResponse> getBuytradesbyportfolioId(Long portfolioid,TradeType type) {
       return tradeRepo.findBuytradesByPortfolioId(portfolioid,type);

}
    @Cacheable(value="selltrades",key="#portfolioid + '_' + #type")
public List<SellTradeResponse> getSelltradesbyPrtfolioId(Long portfolioid,TradeType type) {

List<Trade> trades=tradeRepo.findTradesByPortfolioAndType(portfolioid,type);

List<SellTradeResponse> selltraderesponse=trades.stream().map(trade->

        {
            SellTradeResponse s=new SellTradeResponse();
            s.setQuantity(trade.getQuantity());
            s.setPrice_at_trade(trade.getPrice_at_trade());
            s.setPortfolioId(trade.getPortfolio().getId());
            s.setRealizedPNL(trade.getPNL());
            s.setAssetName(trade.getAsset().getName());
            s.setUsername(trade.getUser().getUsername());

            PortfolioItem item = trade.getPortfolio().getPortfolioItems().stream()
                    .filter(i -> i.getAsset().getId().equals(trade.getAsset().getId()))
                    .findFirst()
                    .orElse(null);

            if(item != null){
                BigDecimal unrealized = item.getAsset().getCurrentPrice()
                        .subtract(item.getAverage_buy_price())
                        .multiply(item.getQuantity());
                s.setUnrealizedPNL(unrealized);
            } else {
                s.setUnrealizedPNL(BigDecimal.ZERO);
            }
            return s;

        }).collect(Collectors.toList());

return selltraderesponse;

}






}
