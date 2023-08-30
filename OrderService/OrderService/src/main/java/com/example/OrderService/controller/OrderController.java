package com.example.OrderService.controller;

import com.example.OrderService.model.OrderRequest;
import com.example.OrderService.model.OrderResponse;
import com.example.OrderService.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Log4j2
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest) throws Exception{
        long orderId = orderService.placeOrder(orderRequest);
        log.info("Order Id {}", orderId);
        return new ResponseEntity<>(orderId,HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable long orderId){

        OrderResponse orderResponse = orderService.getOrderDetails(orderId);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);

    }

}
