package com.example.OrderService.service;

import com.example.OrderService.entity.Order;
import com.example.OrderService.extarnal.client.PaymentService;
import com.example.OrderService.extarnal.client.ProductService;
import com.example.OrderService.extarnal.request.PaymentRequest;
import com.example.OrderService.model.OrderRequest;
import com.example.OrderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final ProductService productService;

    private final PaymentService paymentService;

    @Override
    @Transactional
    public long placeOrder(OrderRequest orderRequest) {

        // Order Entity - > Save the database with status Order Created
        // Produt Service -> Block product (Reduce the quantity)
        // Payment Service -> Payment success ->  Complete else Cancel
        log.info("Placing Order Request{}", orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());

        ResponseEntity<Long> productAmount = productService.getProductAmount(orderRequest.getProductId());

        long totalAmount = productAmount.getBody() * orderRequest.getQuantity();


        log.info("Creating Order With Status CREATED");
        Order order =  new Order();
        order.setAmount(totalAmount);
        order.setProductId(orderRequest.getProductId());
        order.setQuantity(orderRequest.getQuantity());
        order.setOrderDate(Instant.now());
        order.setOrderStatus("CREATED");

        order = orderRepository.save(order);
        log.info("Calling Payment Service to completed the payment");
        PaymentRequest paymentRequest =
                PaymentRequest.builder()
                   .orderId(order.getId())
                   .paymentMode(orderRequest.getPaymentMode())
                   .amount(totalAmount)
                   .build();

        String orderStatus =null;
        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment done successfully Changing the Order Status PLACED");
            orderStatus ="PLACED";
        }catch(Exception e) {
            log.info("Error occured in paymnet. Order Status FAILED");
            orderStatus ="PAYMENT_FAILED";
        }
        order.setOrderStatus(orderStatus);

        orderRepository.save(order);

        log.info("Order Place Successfully with Order Id {}",order);

        return order.getId();

    }
}
