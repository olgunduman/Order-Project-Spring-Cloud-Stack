package com.example.ProductService.service;

import com.example.ProductService.entity.Product;
import com.example.ProductService.exception.ProductServiceCustomException;
import com.example.ProductService.model.ProductRequest;
import com.example.ProductService.model.ProductResponse;
import com.example.ProductService.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding Product");
        if(isExistProductName(productRequest.getProductName())){
            throw new RuntimeException("Product Name Already Exist");
        }
        Product product = modelMapper.map(productRequest, Product.class);

        productRepository.save(product);
        log.info("Product Added");
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("Get the product for productId: {}", productId);
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException("Product with given id not found ","Product Not Found "));

        ProductResponse productResponse = modelMapper.map(product,ProductResponse.class);

        return productResponse;

    }

    private boolean isExistProductName(String productName){
        var productList = productRepository.findAll();
       return productList.stream().anyMatch(product-> product.getProductName().equals(productName));
    }
}
