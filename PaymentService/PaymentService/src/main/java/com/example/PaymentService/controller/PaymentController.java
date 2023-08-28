package com.example.PaymentService.controller;

import com.example.PaymentService.model.PaymentRequest;
import com.example.PaymentService.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/add")
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest){

        return new ResponseEntity<>(paymentService.doPayment(paymentRequest), HttpStatus.OK);
    }
}
