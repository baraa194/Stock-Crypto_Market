package com.myProject.demo.Services;

import com.myProject.demo.DTO.BuyTradeRequest;
import com.myProject.demo.DTO.SellTradeRequest;
import com.myProject.demo.Events.TradeExecutedEvent;
import com.myProject.demo.Exceptions.InsufficientFundsException;
import com.myProject.demo.Exceptions.InvalidTradeQuantityException;
import com.myProject.demo.Models.*;
import com.myProject.demo.Repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TradeServiceTest {
    @Mock
    private TradeRepo tradeRepo;

    @Mock
    private PortfolioRepo portfolioRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private AssetRepo assetRepo;

    @Mock
    private WalletRepo walletRepo;

    @Mock
    private PortfolioItemRepo portfolioItemRepo;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private TradeService tradeService;
    private User user;
    private Asset asset;
    private Wallet wallet;
    private Portfolio portfolio;
    private PortfolioItem portfolioItem;

    private BuyTradeRequest buyRequest;
    private SellTradeRequest sellRequest;
    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("baraa");

        asset = new Asset();
        asset.setId(1L);
        asset.setName("MICROSOFT");
        asset.setCurrentPrice(BigDecimal.valueOf(100));

        wallet = new Wallet();
        wallet.setBalance(BigDecimal.valueOf(10000));

        portfolio = new Portfolio();
        portfolio.setId(1L);
        portfolio.setTotalPNL(BigDecimal.ZERO);
        portfolio.setPortfolioItems(new ArrayList<>());

        portfolioItem = new PortfolioItem();
        portfolioItem.setAsset(asset);
        portfolioItem.setQuantity(BigDecimal.valueOf(10));
        portfolioItem.setAverage_buy_price(BigDecimal.valueOf(80));
        portfolioItem.setPortfolio(portfolio);

        buyRequest = new BuyTradeRequest();
        buyRequest.setUsername("baraa");
        buyRequest.setAssetName("MICROSOFT");
        buyRequest.setPortfolioId(1L);
        buyRequest.setQuantity(BigDecimal.valueOf(5));

        sellRequest = new SellTradeRequest();
        sellRequest.setUsername("baraa");
        sellRequest.setAssetName("MICROSOFT");
        sellRequest.setPortfolioId(1L);
        sellRequest.setQuantity(BigDecimal.valueOf(5));
        sellRequest.setPrice_at_trade(BigDecimal.valueOf(120));
    }
    @Test
    void createBuyingTrade_WhenValidRequest_ShouldSaveTradeAndUpdateWalletAndPortfolio() {
        when(userRepo.findByusername("baraa")).thenReturn(Optional.of(user));
        when(assetRepo.findAssetByName("MICROSOFT")).thenReturn(Optional.of(asset));
        when(portfolioRepo.findById(1L)).thenReturn(Optional.of(portfolio));
        when(walletRepo.findByUserUsername("baraa")).thenReturn(wallet);

        Trade savedTrade = new Trade();
        savedTrade.setId(1L);
        savedTrade.setPrice_at_trade(BigDecimal.valueOf(100));
        savedTrade.setQuantity(BigDecimal.valueOf(5));

        when(tradeRepo.save(any(Trade.class))).thenReturn(savedTrade);

        tradeService.CreateBuyingTrade(buyRequest);

        assertEquals(BigDecimal.valueOf(9500), wallet.getBalance());
        assertEquals(1, portfolio.getPortfolioItems().size());

        verify(walletRepo).save(wallet);
        verify(portfolioItemRepo).save(any(PortfolioItem.class));
        verify(portfolioRepo).save(portfolio);
        verify(tradeRepo).save(any(Trade.class));
        verify(publisher).publishEvent(any(TradeExecutedEvent.class));
    }
    @Test
    void createBuyingTrade_WhenInsufficientFunds_ShouldThrowException() {
        wallet.setBalance(BigDecimal.valueOf(100));

        when(userRepo.findByusername("baraa")).thenReturn(Optional.of(user));
        when(assetRepo.findAssetByName("MICROSOFT")).thenReturn(Optional.of(asset));
        when(portfolioRepo.findById(1L)).thenReturn(Optional.of(portfolio));
        when(walletRepo.findByUserUsername("baraa")).thenReturn(wallet);

        assertThrows(
                InsufficientFundsException.class,
                () -> tradeService.CreateBuyingTrade(buyRequest)
        );

        verify(tradeRepo, never()).save(any());
        verify(publisher, never()).publishEvent(any());
    }
    @Test
    void createSellingTrade_WhenValidRequest_ShouldSaveTradeAndUpdateWalletAndPortfolio() {
        portfolio.getPortfolioItems().add(portfolioItem);

        when(userRepo.findByusername("baraa")).thenReturn(Optional.of(user));
        when(assetRepo.findAssetByName("MICROSOFT")).thenReturn(Optional.of(asset));
        when(portfolioRepo.findById(1L)).thenReturn(Optional.of(portfolio));
        when(walletRepo.findByUserUsername("baraa")).thenReturn(wallet);

        Trade savedTrade = new Trade();
        savedTrade.setId(1L);
        savedTrade.setPrice_at_trade(BigDecimal.valueOf(120));
        savedTrade.setQuantity(BigDecimal.valueOf(5));

        when(tradeRepo.save(any(Trade.class))).thenReturn(savedTrade);

        tradeService.CreatesellingTrade(sellRequest);

        assertEquals(BigDecimal.valueOf(10500), wallet.getBalance());
        assertEquals(BigDecimal.valueOf(5), portfolioItem.getQuantity());

        verify(walletRepo).save(wallet);
        verify(portfolioRepo, atLeastOnce()).save(portfolio);
        verify(tradeRepo).save(any(Trade.class));
        verify(publisher).publishEvent(any(TradeExecutedEvent.class));
    }
    @Test
    void createSellingTrade_WhenQuantityExceedsAvailable_ShouldThrowException() {
        sellRequest.setQuantity(BigDecimal.valueOf(20));
        portfolio.getPortfolioItems().add(portfolioItem);

        when(userRepo.findByusername("baraa")).thenReturn(Optional.of(user));
        when(assetRepo.findAssetByName("MICROSOFT")).thenReturn(Optional.of(asset));
        when(portfolioRepo.findById(1L)).thenReturn(Optional.of(portfolio));

        assertThrows(
                InvalidTradeQuantityException.class,
                () -> tradeService.CreatesellingTrade(sellRequest)
        );

        verify(walletRepo, never()).save(any());
        verify(tradeRepo, never()).save(any());
        verify(publisher, never()).publishEvent(any());
    }
}
