package com.example.tesisaoskaunto.productservice.application;

import com.example.tesisaoskaunto.categoryservice.infrastructure.repositories.CategoryRepository;
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

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productServiceAssistant;
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;

    public ProductController(ProductService productServiceAssistant, ProductRepository productRepository, SizeRepository sizeRepository, CategoryRepository categoryRepository) {
        this.productServiceAssistant = productServiceAssistant;
        this.productRepository = productRepository;
        this.sizeRepository = sizeRepository;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest request) {
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

        Product saved = productServiceAssistant.createProductWithSizes(product, sizes);

        ProductResponse response = new ProductResponse(saved.getId(), saved.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductByCategoryId(@PathVariable Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }
}
