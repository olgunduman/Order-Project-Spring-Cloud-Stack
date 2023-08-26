package com.example.OrderService.service;

import com.example.OrderService.entity.Order;
import com.example.OrderService.extarnal.client.ProductService;
import com.example.OrderService.model.OrderRequest;
import com.example.OrderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ProductService productService;
    @Override
    public long placeOrder(OrderRequest orderRequest) {

        // Order Entity - > Save the database with status Order Created
        // Produt Service -> Block product (Reduce the quantity)
        // Payment Service -> Payment success ->  Complete else Cancel
        log.info("Placing Order Request{}", orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());

        ResponseEntity<Long> productAmount = productService.getProductAmount(orderRequest.getProductId());

        long totalAmount = productAmount.getBody() * orderRequest.getQuantity();
        

        log.info("Creating Order With Status CREATED");
        var order =  new Order();
        order.setAmount(totalAmount);
        order.setProductId(orderRequest.getProductId());
        order.setQuantity(orderRequest.getQuantity());
        order.setOrderDate(Instant.now());
        order.setOrderStatus("CREATED");


        Order orderSave = orderRepository.save(order);
        log.info("Order Place Successfully with Order Id {}",orderSave);

        return orderSave.getId();

    }
}
