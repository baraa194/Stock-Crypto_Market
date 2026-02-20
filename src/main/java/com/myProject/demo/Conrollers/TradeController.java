package com.myProject.demo.Conrollers;

import com.myProject.demo.DTO.BuyTradeResponse;
import com.myProject.demo.DTO.SellTradeRequest;
import com.myProject.demo.DTO.BuyTradeRequest;
import com.myProject.demo.DTO.SellTradeResponse;
import com.myProject.demo.Enums.TradeType;
import com.myProject.demo.Repositories.TradeRepo;
import com.myProject.demo.Services.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trades")
public class TradeController {
    @Autowired
    private TradeService tradeService;
    @Autowired
    private TradeRepo tradeRepo;

    @PostMapping("/buy")
    public ResponseEntity<String> SaveBuying (@RequestBody BuyTradeRequest tradeRequest  )
    {
        tradeService.CreateBuyingTrade(tradeRequest);
        return ResponseEntity.ok("Trade added successfully");
    }

    @PostMapping("/sell")
    public ResponseEntity<String> Saveselling (@RequestBody SellTradeRequest tradeRequest  )
    {
        tradeService.CreatesellingTrade(tradeRequest);
        return ResponseEntity.ok("Trade added successfully");
    }
    @GetMapping("/getbuys/{portfolioid}")
    public ResponseEntity<List<BuyTradeResponse>> getBuys(@PathVariable Long portfolioid)
    {

        return ResponseEntity.ok(tradeService.getBuytradesbyportfolioId(portfolioid, TradeType.BUY));
    }

    @GetMapping("/getsells/{portfolioid}")
    public ResponseEntity<List<SellTradeResponse>> getSells(@PathVariable Long portfolioid)
    {
        return ResponseEntity.ok(tradeService.getSelltradesbyPrtfolioId(portfolioid, TradeType.SELL));
    }
}
