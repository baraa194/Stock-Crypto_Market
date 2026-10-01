package com.myProject.demo.Services;

import com.myProject.demo.Enums.OrderStatus;
import com.myProject.demo.Models.Order;
import com.myProject.demo.Repositories.OrderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;


@Service
@RequiredArgsConstructor
public class OrderStatusService {
    private final OrderRepo orderRepo;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsFailed(Long orderId) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow();

        order.setStatus(OrderStatus.FAILED);
    }
}
