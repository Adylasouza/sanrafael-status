package dev.adylaoliveira.sanrafael.adapter.controller;

import dev.adylaoliveira.sanrafael.core.dto.CreateProductDTO;
import dev.adylaoliveira.sanrafael.core.dto.ProductResponseDTO;
import dev.adylaoliveira.sanrafael.core.dto.SystemStatusDTO;
import dev.adylaoliveira.sanrafael.service.ProductService;
import org.springframework.web.bind.annotation.*;

import dev.adylaoliveira.sanrafael.core.dto.ProductDetailDTO;

import java.util.List;

@RestController
@RequestMapping
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // HU-01 → STATUS GLOBAL
    @GetMapping("/status")
    public SystemStatusDTO getSystemStatus() {
        return productService.getSystemStatus();
    }

    @GetMapping("/products/{productId}")
    public ProductDetailDTO getProductDetails(@PathVariable String productId) {
        return productService.getProductDetails(productId);
    }

    // PRODUCTS

    @GetMapping("/products")
    public List<ProductResponseDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/products")
    public void createProduct(@RequestBody CreateProductDTO dto) {
        productService.saveProduct(dto);
    }



    // REPORTS
    // EVENTS

}