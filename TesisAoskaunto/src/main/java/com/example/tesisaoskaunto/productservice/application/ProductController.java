package com.example.tesisaoskaunto.productservice.application;

import com.example.tesisaoskaunto.productservice.domain.models.Product;
import com.example.tesisaoskaunto.productservice.domain.models.Size;
import com.example.tesisaoskaunto.productservice.service.ProductService;
import com.example.tesisaoskaunto.productservice.infrastructure.repository.ProductRepository;
import com.example.tesisaoskaunto.productservice.infrastructure.repository.SizeRepository;
import com.example.tesisaoskaunto.productservice.domain.dto.CreateProductRequest;
import com.example.tesisaoskaunto.productservice.domain.dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productServiceAssistant;
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;

    public ProductController(ProductService productServiceAssistant, ProductRepository productRepository, SizeRepository sizeRepository) {
        this.productServiceAssistant = productServiceAssistant;
        this.productRepository = productRepository;
        this.sizeRepository = sizeRepository;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request) {
        Set<Size> sizes = sizeRepository.findByIdIn(request.getSizeIds());

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(BigDecimal.valueOf(request.getPrice()));
        product.setDiscount(BigDecimal.valueOf(request.getDiscount()));
        product.setImage(request.getImage());
        product.setCategory(request.getCategory());
        product.setSizes(sizes);

        Product savedProduct = productRepository.save(product);

        ProductResponse response = new ProductResponse(savedProduct.getId(), savedProduct.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> ResponseEntity.ok(new ProductResponse(product.getId(), product.getName())))
                .orElse(ResponseEntity.notFound().build());
    }
}
