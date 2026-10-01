package com.myProject.demo.Services;

import com.myProject.demo.DTO.OrderRequest;
import com.myProject.demo.DTO.OrderResponse;
import com.myProject.demo.Enums.OrderStatus;
import com.myProject.demo.Enums.OrderType;
import com.myProject.demo.Enums.TradeType;
import com.myProject.demo.Models.Asset;
import com.myProject.demo.Models.Order;
import com.myProject.demo.Models.Portfolio;
import com.myProject.demo.Models.User;
import com.myProject.demo.Repositories.AssetRepo;
import com.myProject.demo.Repositories.OrderRepo;
import com.myProject.demo.Repositories.PortfolioRepo;
import com.myProject.demo.Repositories.UserRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class OrderService {
    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final PortfolioRepo portfolioRepo;
    private final AssetRepo assetRepo;
    private final OrderExecutionService orderExecutionService;
    private final ReservationService reservationService;
    private final OrderStatusService orderStatusService;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        User user = userRepo.findByusername(request.getUsername()).orElseThrow(() ->
                        new RuntimeException("User not found"));

        Portfolio portfolio = portfolioRepo.findById(request.getPortfolioId()).orElseThrow(() ->
                        new RuntimeException("Portfolio not found"));

        Asset asset = assetRepo.findAssetByName(request.getAssetName()).orElseThrow(() ->
                        new RuntimeException("Asset not found"));

        if (request.getQuantity() == null ||
                request.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "Quantity must be greater than zero"
            );
        }

        if (request.getTargetPrice() == null ||
                request.getTargetPrice().compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "Target price must be greater than zero"
            );
        }


        Order order = new Order();

        order.setUser(user);
        order.setPortfolio(portfolio);
        order.setAsset(asset);

        order.setType(request.getType());
        order.setOrderType(
                request.getOrderType() != null
                        ? request.getOrderType()
                        : OrderType.LIMIT
        );
        order.setQuantity(request.getQuantity());
        order.setTargetPrice(request.getTargetPrice());

        order.setStatus(OrderStatus.PENDING);
        if (order.getType() == TradeType.BUY) {
            reservationService.reserveBuyFunds(order);
        } else if (order.getType() == TradeType.SELL) {

           if(order.getOrderType()==OrderType.TAKE_PROFIT)
            {
                if(order.getTargetPrice().compareTo(order.getAsset().getCurrentPrice())<=0 )
                {
                 throw new RuntimeException("Invalid Order");
                }

            } else if (order.getOrderType()==OrderType.STOP_LOSS) {

                if(order.getTargetPrice().compareTo(order.getAsset().getCurrentPrice())>=0)
                {
                  throw  new RuntimeException("Invalid Order");
                }

            }
            reservationService.reserveSellQuantity(order);


        }


        Order savedOrder = orderRepo.save(order);

        return mapToResponse(savedOrder);
    }
    public List<OrderResponse> getPortfolioOrders(Long portfolioId) {

        return orderRepo
                .findByPortfolioId(portfolioId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    public List<OrderResponse> getPendingOrders(Long portfolioId) {

        return orderRepo
                .findByPortfolioIdAndStatus(
                        portfolioId,
                        OrderStatus.PENDING
                )
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    public OrderResponse cancelOrder(Long orderId) {

        Order order = orderRepo
                .findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found")
                );

        if (order.getStatus() != OrderStatus.PENDING) {

            throw new RuntimeException(
                    "Only pending orders can be cancelled"
            );
        }

        order.setStatus(OrderStatus.CANCELLED);
        if (order.getType() == TradeType.BUY) {
            reservationService.releaseBuyReservation(order);
        } else {
            reservationService.releaseSellReservation(order);
        }

        Order savedOrder = orderRepo.save(order);

        return mapToResponse(savedOrder);
    }

    public List<Order> getMatchedOrders(String assetName, BigDecimal newPrice) {

        List<Order> buyOrders =
                orderRepo.findByAssetNameAndTypeAndStatusAndOrderTypeAndTargetPriceGreaterThanEqual(
                                assetName,
                                TradeType.BUY,
                                OrderStatus.PENDING,
                        OrderType.LIMIT , newPrice);

        List<Order> sellOrders = orderRepo.findByAssetNameAndTypeAndStatusAndOrderTypeAndTargetPriceLessThanEqual(
                                assetName,
                                TradeType.SELL,
                                OrderStatus.PENDING, OrderType.LIMIT, newPrice );

        List<Order> takeProfitOrders= orderRepo.findByAssetNameAndTypeAndStatusAndOrderTypeAndTargetPriceLessThanEqual(
                assetName,
                TradeType.SELL,
                OrderStatus.PENDING,
                OrderType.TAKE_PROFIT,
                newPrice)
               ;

        List<Order> stopLossOrders= orderRepo.findByAssetNameAndTypeAndStatusAndOrderTypeAndTargetPriceGreaterThanEqual(
                assetName,
                TradeType.SELL,
                OrderStatus.PENDING,
                OrderType.STOP_LOSS,
                newPrice);

        List<Order> matchedOrders = new ArrayList<>();

        matchedOrders.addAll(buyOrders);
        matchedOrders.addAll(sellOrders);
       matchedOrders.addAll(stopLossOrders);
       matchedOrders.addAll(takeProfitOrders);
        return matchedOrders;

    }

    public void passOrdersToTrade(List<Order> matchedOrders) {


            for (Order order : matchedOrders) {
                try{
                orderExecutionService.executeOrder(order.getId());

            } catch (Exception e) {
                    e.printStackTrace();

                    orderStatusService.markAsFailed(order.getId());
                }

            }

    }




    private OrderResponse mapToResponse(Order order) {

        OrderResponse response = new OrderResponse();

        response.setId(order.getId());

        response.setUsername(
                order.getUser().getUsername()
        );

        response.setPortfolioId(
                order.getPortfolio().getId()
        );

        response.setAssetName(
                order.getAsset().getName()
        );

        response.setType(order.getType());

        response.setQuantity(
                order.getQuantity()
        );

        response.setTargetPrice(
                order.getTargetPrice()
        );

        response.setStatus(
                order.getStatus()
        );

        response.setCreatedAt(
                order.getCreatedAt()
        );

        response.setExecutedAt(
                order.getExecutedAt()
        );
        response.setOrderType(order.getOrderType());

        return response;
    }







}
