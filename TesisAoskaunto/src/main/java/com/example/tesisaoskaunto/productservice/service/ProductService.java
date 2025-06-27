package com.example.tesisaoskaunto.productservice.service;

import com.example.tesisaoskaunto.productservice.domain.models.Product;
import com.example.tesisaoskaunto.productservice.domain.models.Size;
import com.example.tesisaoskaunto.productservice.infrastructure.repository.ProductRepository;
import com.example.tesisaoskaunto.productservice.infrastructure.repository.SizeRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
public class ProductService {
    private final SizeRepository sizeRepository;
    private final ProductRepository productRepository;

    public ProductService(SizeRepository sizeRepository, ProductRepository productRepository) {
        this.sizeRepository = sizeRepository;
        this.productRepository = productRepository;
    }

    public Product createProductWithSizes(Product product, List<Size> sizes) {
        List<Size> persistedSizes = sizeRepository.saveAll(sizes);

        product.setSizes(new HashSet<>(persistedSizes));

        return productRepository.save(product);
    }

}
