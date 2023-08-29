package com.example.OrderService.model;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductDetails {

    private long productId;
    private String productName;
    private long price;
    private long quantity;
}
