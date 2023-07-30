package com.example.OrderService.service;

import com.example.OrderService.entity.Order;
import com.example.OrderService.model.OrderRequest;
import com.example.OrderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    @Override
    public long placeOrder(OrderRequest orderRequest) {

        // Order Entity - > Save the database with status Order Created
        // Produt Service -> Block product (Reduce the quantity)
        // Payment Service -> Payment success ->  Complete else Cancel
        log.info("Placing Order Request{}", orderRequest);

        var order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .productId(orderRequest.getProductId())
                .orderStatus("CREATED")
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();

        Order orderSave = orderRepository.save(order);
        log.info("Order Place Successfully with Order Id {}",orderSave);

        return orderSave.getId();

    }
}
