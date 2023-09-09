package com.example.ProductService.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {

    private long productId;
    private String productName;
    private long price;
    private long quantity;
}
