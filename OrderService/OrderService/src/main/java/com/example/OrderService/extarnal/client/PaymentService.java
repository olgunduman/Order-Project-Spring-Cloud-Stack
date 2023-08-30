package com.example.OrderService.extarnal.client;

import com.example.OrderService.exception.CustomException;
import com.example.OrderService.extarnal.request.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
//@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "PAYMENT-SERVICE/payment")
public interface PaymentService {
    Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @CircuitBreaker(name = "external", fallbackMethod = "fallbackDoPayment")
    @PostMapping("/add")
     ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest);

    default ResponseEntity<Long> fallbackDoPayment(PaymentRequest paymentRequest, Exception e) {
        logger.error("Error while doing payment {} ", paymentRequest);
        throw new CustomException("Payment Service is avaliable",
                "UNAVAILABLE", 500);
    }
}



