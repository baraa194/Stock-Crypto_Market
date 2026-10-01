package com.myProject.demo.Services;

import com.myProject.demo.DTO.BuyTradeRequest;
import com.myProject.demo.DTO.SellTradeRequest;
import com.myProject.demo.Enums.OrderStatus;
import com.myProject.demo.Enums.TradeType;
import com.myProject.demo.Exceptions.InsufficientFundsException;
import com.myProject.demo.Models.Order;
import com.myProject.demo.Repositories.OrderRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderExecutionService {

    private final OrderRepo orderRepo;
    private final TradeService tradeService;
    private final ReservationService reservationService;

    @Transactional
    public void executeOrder(Long orderId) {

        Order order = orderRepo.findByIdForUpdate(orderId).orElseThrow(() ->
                        new RuntimeException("Order not found"));


        if (order.getStatus() != OrderStatus.PENDING) {
            return;}

        order.setStatus(OrderStatus.PROCESSING);


            if (order.getType() == TradeType.BUY) {

                BuyTradeRequest buyReq = new BuyTradeRequest();

                buyReq.setAssetName(order.getAsset().getName());
                buyReq.setPortfolioId(order.getPortfolio().getId());
                buyReq.setQuantity(order.getQuantity());
                buyReq.setUsername(order.getUser().getUsername());
                buyReq.setFromOrder(true);

                tradeService.CreateBuyingTrade(buyReq);

                reservationService.releaseBuyReservation(order);

            } else if (order.getType() == TradeType.SELL) {

                SellTradeRequest sellReq = new SellTradeRequest();

                sellReq.setAssetName(order.getAsset().getName());
                sellReq.setPortfolioId(order.getPortfolio().getId());
                sellReq.setPrice_at_trade(order.getTargetPrice());
                sellReq.setQuantity(order.getQuantity());
                sellReq.setUsername(order.getUser().getUsername());
                sellReq.setFromOrder(true);

                tradeService.CreatesellingTrade(sellReq);

                reservationService.releaseSellReservation(order);
            }

            order.setStatus(OrderStatus.EXECUTED);
            order.setExecutedAt(LocalDateTime.now());



        orderRepo.save(order);
    }
}