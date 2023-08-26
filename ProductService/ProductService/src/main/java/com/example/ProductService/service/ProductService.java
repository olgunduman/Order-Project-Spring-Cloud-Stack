package com.example.ProductService.service;

import com.example.ProductService.model.ProductRequest;
import com.example.ProductService.model.ProductResponse;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);

    long getProductPrice(long productId);
}
