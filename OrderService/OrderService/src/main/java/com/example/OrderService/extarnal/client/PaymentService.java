package com.example.OrderService.extarnal.client;

import com.example.OrderService.extarnal.request.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "PAYMENT-SERVICE/payment")
public interface PaymentService {

    @PostMapping("/add")
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);


}
