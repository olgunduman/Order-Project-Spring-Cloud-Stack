package com.example.PaymentService.service;

import com.example.PaymentService.entity.TransactionDetails;
import com.example.PaymentService.model.PaymentMode;
import com.example.PaymentService.model.PaymentRequest;
import com.example.PaymentService.repository.TransactionDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentServiceImpl implements PaymentService{

    private final TransactionDetailsRepository transactionDetailsRepository;
    @Override
    public long doPayment(PaymentRequest paymentRequest) {
        log.info("recording Payment Details: {}",paymentRequest);

        TransactionDetails transactionDetails =
                TransactionDetails.builder()
                        .orderId(paymentRequest.getOrderId())
                        .paymentDate(Instant.now())
                        .paymentMode(paymentRequest.getPaymentMode().name())
                        .referenceNumber(paymentRequest.getReferenceNumber())
                         .paymentStatus("SUCCESS")
                         .amount(paymentRequest.getAmount())
                        .build();

        transactionDetailsRepository.save(transactionDetails);
        log.info("Transaction Completed with Id: {}",transactionDetails.getId());
        return transactionDetails.getId();
    }
}
