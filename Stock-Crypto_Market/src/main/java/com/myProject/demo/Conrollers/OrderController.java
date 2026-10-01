package com.myProject.demo.Conrollers;

import com.myProject.demo.DTO.OrderRequest;
import com.myProject.demo.DTO.OrderResponse;
import com.myProject.demo.Services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {

        OrderResponse response = orderService.createOrder(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }



    @GetMapping("/portfolio/{portfolioId}")
    public ResponseEntity<List<OrderResponse>>
    getPortfolioOrders(
            @PathVariable Long portfolioId
    ) {

        return ResponseEntity.ok(
                orderService.getPortfolioOrders(portfolioId)
        );
    }



    @GetMapping("/portfolio/{portfolioId}/pending")
    public ResponseEntity<List<OrderResponse>>
    getPendingOrders(
            @PathVariable Long portfolioId
    ) {

        return ResponseEntity.ok(
                orderService.getPendingOrders(portfolioId)
        );
    }



    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long orderId
    ) {

        return ResponseEntity.ok(
                orderService.cancelOrder(orderId)
        );
    }
}