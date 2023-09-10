package com.example.OrderService.service;

import com.example.OrderService.entity.Order;
import com.example.OrderService.exception.CustomException;
import com.example.OrderService.extarnal.client.PaymentService;
import com.example.OrderService.extarnal.client.ProductService;
import com.example.OrderService.extarnal.request.PaymentRequest;
import com.example.OrderService.extarnal.response.PaymentResponse;
import com.example.OrderService.model.OrderRequest;
import com.example.OrderService.model.OrderResponse;
import com.example.OrderService.model.PaymentMode;
import com.example.OrderService.repository.OrderRepository;
import com.example.ProductService.model.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceImplTest {

    @Mock
    private  OrderRepository orderRepository;
    @Mock
    private  ProductService productService;
    @Mock
    private  PaymentService paymentService;
    @Mock
    private  RestTemplate restTemplate;

    @InjectMocks
    private OrderService orderService = new OrderServiceImpl();


    @DisplayName("Get Order - Success Scenario")
    @Test
     void test_when_Order_Service_GetOrderDetails_Success(){
        //MOCK
        Order order = getMockOrder();
        when(orderRepository.findById(anyLong()))
                .thenReturn(Optional.of(order));

        when(restTemplate.getForObject("http://PRODUCT-SERVICE/product/" + order.getProductId(),
                ProductResponse.class)).thenReturn(getMockProductResponse());

        when(restTemplate.getForObject("http://PAYMENT-SERVICE/payment/order/" + order.getId(),
                PaymentResponse.class)).thenReturn(getMockPaymentResponse());

        //ACTUAL
        OrderResponse orderResponse = orderService.getOrderDetails(1);

        //VERIFACTION
        verify(orderRepository,times(1)).findById(anyLong());
        verify(restTemplate,times(1)).getForObject("http://PRODUCT-SERVICE/product/" + order.getProductId(),
                ProductResponse.class);
        verify(restTemplate,times(1)).getForObject("http://PAYMENT-SERVICE/payment/order/" + order.getId(),
                PaymentResponse.class);


        //ASSERT
        assertNotNull(orderResponse);
        assertEquals(order.getId(),orderResponse.getOrderId());

    }

    @DisplayName("Get Order - Failure Scenario")
    @Test
    void test_when_Order_Service_GetOrderDetails_Failure_NotFound() {
        //MOCK
        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));

        //ACTUAL
        CustomException exception = assertThrows(CustomException.class,()-> orderService.getOrderDetails(1));

        //VERIFACTION
        verify(orderRepository,times(1)).findById(anyLong());

        //ASSERT
        assertEquals("NOT_FOUND",exception.getErrorCode());
        assertEquals(404,exception.getStatus());


    }

    @Test
    void test_when_Order_Service_PlaceOrder_Success() throws Exception {
        Order order = getMockOrder();
        OrderRequest orderRequest = getMockOrderRequest();
        //MOCK
        when(productService.reduceQuantity(anyLong(),anyLong())).
                thenReturn(new ResponseEntity<Void>(HttpStatus.OK));

        when(productService.getProductAmount(anyLong()))
                .thenReturn(new ResponseEntity<Long>(100L,HttpStatus.OK));

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        when(paymentService.doPayment(any(PaymentRequest.class))).
                thenReturn(new  ResponseEntity<Long>(1L,HttpStatus.OK));

        //ACTUAL
        long orderId = orderService.placeOrder(orderRequest);

        //VERIFACTION
        verify(orderRepository,times(2)).save(any());
        verify(productService,times(1)).reduceQuantity(anyLong(),anyLong());
        verify(productService,times(1)).getProductAmount(anyLong());
        verify(paymentService,times(1)).doPayment(any(PaymentRequest.class));

        //ASSERT
        assertEquals(order.getId(),orderId);

    }

    @DisplayName("Place Order - Payment Failed Scenario")
    @Test
    void test_when_Order_Service_Place_Order_Payment_Fails() throws Exception {

        Order order = getMockOrder();
        OrderRequest orderRequest = getMockOrderRequest();
        //MOCK
        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);
        when(productService.reduceQuantity(anyLong(),anyLong()))
                .thenReturn(new ResponseEntity<Void>(HttpStatus.OK));

        when(productService.getProductAmount(anyLong()))
                .thenReturn(new ResponseEntity<Long>(100L,HttpStatus.OK));
        when(paymentService.doPayment(any(PaymentRequest.class)))
                .thenThrow(new RuntimeException());


        //ACTUAL
        var exception = assertThrows(RuntimeException.class,()-> orderService.placeOrder(orderRequest));

        //VERIFACTION
        verify(orderRepository, times(2))
                .save(any());
        verify(productService, times(1))
                .reduceQuantity(anyLong(),anyLong());
        verify(productService,times(1)).getProductAmount(anyLong());
        verify(paymentService, times(1))
                .doPayment(any(PaymentRequest.class));

        //ASSERT
        assertEquals("java.lang.RuntimeException",exception.getClass().getName());


    }
    private OrderRequest getMockOrderRequest() {
        return OrderRequest.builder()
                .productId(1)
                .quantity(200)
                .paymentMode(PaymentMode.CASH)
                .totalAmount(1000)
                .build();
    }


    private PaymentResponse getMockPaymentResponse() {
        return PaymentResponse.builder()
                .paymentId(1)
                .paymentDate(Instant.now())
                .paymentMode(PaymentMode.CASH)
                .amount(200)
                .orderId(1)
                .status("ACCEPTED")
                .build();
    }

    private ProductResponse getMockProductResponse() {
        return ProductResponse.builder()
                .productId(2)
                .productName("iPhone")
                .price(100)
                .quantity(200)
                .build();
    }

    private Order getMockOrder() {
        return Order.builder()
                .orderStatus("PLACED")
                .orderDate(Instant.now())
                .id(1)
                .amount(100)
                .quantity(200)
                .productId(2)
                .build();
    }


}