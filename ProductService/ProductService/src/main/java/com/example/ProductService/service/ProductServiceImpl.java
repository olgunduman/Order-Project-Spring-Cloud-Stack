package com.example.ProductService.service;

import com.example.ProductService.entity.Product;
import com.example.ProductService.exception.ProductNotFoundException;
import com.example.ProductService.model.ProductRequest;
import com.example.ProductService.model.ProductResponse;
import com.example.ProductService.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new ProductNotFoundException("Product with given "+ productId +  " not found ","Product Not Found "));

        ProductResponse productResponse = modelMapper.map(product,ProductResponse.class);

        return productResponse;

    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce Quantity {} for Id: {}",productId,quantity);

        var product = productRepository.findById(productId).
                orElseThrow(() -> new ProductNotFoundException("Product with given" + productId + "not found","PRODUCT_NOT_FOUND"));
        if(product.getQuantity() < quantity){
            throw new ProductNotFoundException("Product does not have sufficent Quantity","INSUFFICENT_QUANTITY");
        }
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        log.info("Product Quantity uptated Succesfully");
    }

    @Override
    public long getProductPrice(long productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product with given" + productId + "not found","PRODUCT_NOT_FOUND"));

            return product.getPrice();
    }

    private boolean isExistProductName(String productName){
        var productList = productRepository.findAll();
       return productList.stream().anyMatch(product-> product.getProductName().equals(productName));
    }
}
