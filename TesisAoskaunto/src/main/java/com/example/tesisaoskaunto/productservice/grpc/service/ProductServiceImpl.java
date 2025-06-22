package com.example.tesisaoskaunto.productservice.grpc.service;

import com.example.tesisaoskaunto.productservice.application.ProductServiceAssistant;
import com.example.tesisaoskaunto.productservice.domain.models.Product;
import com.example.tesisaoskaunto.productservice.domain.models.Size;
import com.example.tesisaoskaunto.projectservice.grpc.proto.*;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class ProductServiceImpl extends ProductServiceGrpc.ProductServiceImplBase {
    private final ProductServiceAssistant productService;

    public ProductServiceImpl(ProductServiceAssistant productService) {
        this.productService = productService;
    }

    @Override
    public void createProduct(CreateProductRequest request, StreamObserver<ProductResponse> responseObserver) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(BigDecimal.valueOf(request.getOriginalPrice()));
        product.setDiscount(BigDecimal.valueOf(request.getDiscount()));
        product.setImage(request.getImage());
        product.setCategory(request.getCategory());
        product.setDescription(request.getDescription());

        List<Size> sizes = request.getSizesList().stream().map(sizeInput -> {
            Size size = new Size();
            size.setName(sizeInput.getName());
            size.setIsActive(sizeInput.getIsActive());
            return size;
        }).collect(Collectors.toList());

        Product saved = productService.createProductWithSizes(product, sizes);

        ProductResponse response = ProductResponse.newBuilder()
                .setId(saved.getId())
                .setName(saved.getName())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
