package com.example.OrderService.extarnal.response;

import com.example.OrderService.model.PaymentMode;
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
