package com.example.ProductService.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;

@Entity(name="product")
@Table(name="product", schema = "productdb")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long productId;
    @Column(name = "PRODUCT_NAME")

    private String productName;

    @Column(name="PRICE")

    private long price;

    @Column(name="QUANTITY")

    private long quantity;
}
