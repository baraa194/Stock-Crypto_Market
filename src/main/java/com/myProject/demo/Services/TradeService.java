package com.myProject.demo.Services;

import com.myProject.demo.DTO.*;
import com.myProject.demo.Enums.TradeType;
import com.myProject.demo.Events.TradeExecutedEvent;
import com.myProject.demo.Exceptions.*;
import com.myProject.demo.Models.*;
import com.myProject.demo.Repositories.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
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
    /* @CacheEvict(value = {
             "buytrades",
             "selltrades",
             "portfolios",
             "portfoliosList"
     }, allEntries = true)*/

     public void CreateBuyingTrade(BuyTradeRequest tradereq) {
        log.info("Starting BUY trade for user={} asset={} quantity={}",
                tradereq.getUsername(),
                tradereq.getAssetName(),
                tradereq.getQuantity());
         // get all objs from db
        User userfromdb= userRepo.findByusername(tradereq.getUsername())
                .orElseThrow(()->new UserNotFoundException("User not found"));
        Asset assetfromdb=assetRepo.findAssetByName(tradereq.getAssetName())
                .orElseThrow(()->new AssetNotFoundException("Asset not found"));
        Portfolio portfoliofromdb=portfolioRepo.findById(tradereq.getPortfolioId())
                .orElseThrow(()->new PortfolioNotFoundException("Portfolio not found"));


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
            log.info("Calculated total cost={} for quantity={} at price={}",
                    totalcost,
                    tradereq.getQuantity(),
                    assetcurrentprice);

            log.info("User wallet balance before trade={}", userwallet.getBalance());
            if(userwallet.getBalance().compareTo(totalcost) >=0)
            {

             userwallet.setBalance(userwallet.getBalance().subtract(totalcost));
             userwallet.setUpdatedAt(LocalDateTime.now());
                walletRepo.save(userwallet);
                log.info("Wallet updated. New balance={}", userwallet.getBalance());
            }
            else{
                throw new InsufficientFundsException("Insufficient Funds");

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
                log.info("Adding new asset to portfolio asset={} quantity={}",
                        assetfromdb.getName(),
                        tradereq.getQuantity());
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

                log.info("Updating existing portfolio item asset={} oldQty={} newQty={}",
                        assetfromdb.getName(),
                        oldQty,
                        totalQty);
                portfoliofromdb.setTotalPNL(
                        portfoliofromdb.getTotalPNL().add(pnl)
                );

                portfolioItemRepo.save(existingitem.get() );
            }

            portfolioRepo.save(portfoliofromdb);
            Trade trade2=tradeRepo.save(trade);
            log.info("\n=== 🔔 PUBLISHING TRADE EVENT ===");
            log.info("Trade ID:{} " , trade2.getId());
            log.info("Trade saved successfully in DB:${} " ,trade2.getId());
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
            throw new InvalidTradeException("Invalid Trade");
        }

    }



    @Transactional
   /*@CacheEvict(value = {
            "buytrades",
            "selltrades",
            "portfolios",
            "portfoliosList"}, allEntries = true)*/
public void CreatesellingTrade(SellTradeRequest tradereq) {
        log.info("Starting SELL trade: user={} asset={} portfolioId={} quantity={}",
                tradereq.getUsername(),
                tradereq.getAssetName(),
                tradereq.getPortfolioId(),
                tradereq.getQuantity());
        User userfromdb= userRepo.findByusername(tradereq.getUsername())
                .orElseThrow(()->new UserNotFoundException("User not found"));
        Asset assetfromdb=assetRepo.findAssetByName(tradereq.getAssetName())
                .orElseThrow(()->new AssetNotFoundException("Asset not found"));
        Portfolio portfoliofromdb=portfolioRepo.findById(tradereq.getPortfolioId())
                .orElseThrow(()->new PortfolioNotFoundException("Portfolio not found"));
    PortfolioItem portfolioitem=portfoliofromdb.getPortfolioItems().stream()
            .filter(i->i.getAsset().getName().equals(tradereq.getAssetName()))
            .findFirst().orElseThrow(() -> new AssetNotFoundException("Asset not found in portfolio"));

     if(tradereq.getQuantity().compareTo(BigDecimal.ZERO)>0 &&
     tradereq.getQuantity().compareTo(portfolioitem.getQuantity()) <=0)
     {
         Wallet userwallet= walletRepo.findByUserUsername(tradereq.getUsername());
         BigDecimal assetcurrentprice=assetfromdb.getCurrentPrice();
         BigDecimal totalcost=assetcurrentprice.multiply(tradereq.getQuantity());
         log.info("Calculated sell total amount={} using currentPrice={} for quantity={}",
                 totalcost,
                 assetcurrentprice,
                 tradereq.getQuantity());
         userwallet.setBalance(userwallet.getBalance().add(totalcost));
         userwallet.setUpdatedAt(LocalDateTime.now());
         walletRepo.save(userwallet);
         log.info("Wallet updated for user={}. New balance={}",
                 userfromdb.getUsername(),
                 userwallet.getBalance());
         BigDecimal realizedPNL=(tradereq.getPrice_at_trade().subtract(portfolioitem.getAverage_buy_price()))
                 .multiply(tradereq.getQuantity());
         log.info("Calculated realized PNL={} for user={} asset={}",
                 realizedPNL,
                 userfromdb.getUsername(),
                 assetfromdb.getName());
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
             log.info("Selling full quantity of asset={} from portfolioId={}. Removing portfolio item.",
                     assetfromdb.getName(),
                     portfoliofromdb.getId());

         }
         else {
             portfolioitem.setQuantity(portfolioitem.getQuantity().subtract(tradereq.getQuantity()));
             portfolioitem.setUpdated_at(LocalDateTime.now());
             log.info("Updating portfolio item for asset={} in portfolioId={}. Remaining quantity={}",
                     assetfromdb.getName(),
                     portfoliofromdb.getId(),
                     portfolioitem.getQuantity().subtract(tradereq.getQuantity()));
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
         log.info("SELL trade saved successfully with tradeId={}", trade2.getId());
         publisher.publishEvent(event);

     }
     else  {
         throw new InvalidTradeQuantityException ("Quantity exceeds available amount");

     }



}
//@Cacheable(value="buytrades",key="#portfolioid + '_' + #type")
public List<BuyTradeResponse> getBuytradesbyportfolioId(Long portfolioid,TradeType type) {
       return tradeRepo.findBuytradesByPortfolioId(portfolioid,type);

}
   // @Cacheable(value="selltrades",key="#portfolioid + '_' + #type")
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
