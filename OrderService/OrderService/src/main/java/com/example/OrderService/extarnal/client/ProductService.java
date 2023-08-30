package com.example.OrderService.extarnal.client;

import com.example.OrderService.exception.CustomException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "PRODUCT-SERVICE/product")
public interface ProductService {


    Logger logger = LoggerFactory.getLogger(ProductService.class);
    @CircuitBreaker(name = "external", fallbackMethod = "fallbackReduceQuantity")
    @PutMapping("/reduceQuantity/{id}")
     ResponseEntity<Void> reduceQuantity
            (@PathVariable("id") long productId, @RequestParam long quantity);
    default ResponseEntity<Void> fallbackReduceQuantity(long productId, long quantity, Exception e) {
        logger.error("Error while reducing quantity of product {} with quantity {} ", productId, quantity);
        throw new CustomException("Product Service is avaliable",
                "UNAVAILABLE",500);

    }


    @GetMapping("/quantity/product/{id}")
     ResponseEntity<Long> getProductAmount(@PathVariable("id") long productId);


}
