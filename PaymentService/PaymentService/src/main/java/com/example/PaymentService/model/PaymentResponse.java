package com.example.PaymentService.model;

import lombok.*;

import java.time.Instant;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private long paymentId;
    private long orderId;
    private String status;
    private PaymentMode paymentMode;
    private long amount;
    private Instant paymentDate;
}
