package com.example.ProductService.model;

import lombok.Data;

@Data
public class ProductResponse {

    private long productId;
    private String productName;
    private long price;
    private long quantity;
}
