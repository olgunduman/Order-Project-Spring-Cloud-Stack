package com.example.OrderService.extarnal.request;

import com.example.OrderService.model.PaymentMode;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PaymentRequest {

    private long orderId;
    private long amount;
    private String referenceNumber;
    private PaymentMode paymentMode;

}
