package com.example.tesisaoskaunto.productservice.application;

import com.example.tesisaoskaunto.categoryservice.infrastructure.repositories.CategoryRepository;
import com.example.tesisaoskaunto.productservice.domain.models.Product;
import com.example.tesisaoskaunto.productservice.domain.models.Size;
import com.example.tesisaoskaunto.productservice.service.ProductService;
import com.example.tesisaoskaunto.productservice.infrastructure.repository.ProductRepository;
import com.example.tesisaoskaunto.productservice.infrastructure.repository.SizeRepository;
import com.example.tesisaoskaunto.productservice.domain.dto.CreateProductRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productServiceAssistant;
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;

    public ProductController(ProductService productServiceAssistant, ProductRepository productRepository, CategoryRepository categoryRepository, SizeRepository sizeRepository) {
        this.productServiceAssistant = productServiceAssistant;
        this.productRepository = productRepository;
        this.sizeRepository = sizeRepository;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(BigDecimal.valueOf(request.getPrice()));
        product.setDiscount(BigDecimal.valueOf(request.getDiscount()));
        product.setImage(request.getImage());
        product.setCategoryId(request.getCategoryId());

        List<Size> sizes = request.getSizes().stream()
                .map(dto -> new Size(dto.getName(), dto.isActive()))
                .toList();

        Product response = productServiceAssistant.createProductWithSizes(product, sizes);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductByCategoryId(@PathVariable Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product updatedProduct) {
        Optional<Product> existingProductOpt = productRepository.findById(productId);

        if (existingProductOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Product existingProduct = existingProductOpt.get();

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setDiscount(updatedProduct.getDiscount());
        existingProduct.setImage(updatedProduct.getImage());
        existingProduct.setCategoryId(updatedProduct.getCategoryId());

        Map<String, Size> currentSizesMap = existingProduct.getSizes()
                .stream()
                .collect(Collectors.toMap(Size::getName, s -> s));

        Set<Size> newSizes = new HashSet<>();

        for (Size incomingSize : updatedProduct.getSizes()) {
            Size matched = currentSizesMap.get(incomingSize.getName());

            if (matched != null) {
                matched.setIsActive(incomingSize.getIsActive());
                newSizes.add(matched);
            } else {
                Size newSize = new Size(incomingSize.getName(), incomingSize.getIsActive());
                sizeRepository.save(newSize);
                newSizes.add(newSize);
            }
        }
        existingProduct.setSizes(newSizes);

        Product savedProduct = productRepository.save(existingProduct);

        return ResponseEntity.ok(savedProduct);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long productId) {
        Optional<Product> existingProductOpt = productRepository.findById(productId);
        if (existingProductOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Product existingProduct = existingProductOpt.get();
        productRepository.delete(existingProduct);
        return ResponseEntity.noContent().build();
    }
}
