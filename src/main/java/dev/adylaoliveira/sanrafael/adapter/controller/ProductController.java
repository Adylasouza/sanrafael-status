package dev.adylaoliveira.sanrafael.adapter.controller;

import dev.adylaoliveira.sanrafael.core.dto.CreateProductDTO;
import dev.adylaoliveira.sanrafael.core.dto.ProductResponseDTO;
import dev.adylaoliveira.sanrafael.core.dto.SystemStatusDTO;
import dev.adylaoliveira.sanrafael.service.ProductService;
import org.springframework.web.bind.annotation.*;

import dev.adylaoliveira.sanrafael.core.dto.ProductDetailDTO;

import java.util.List;

@RestController
@RequestMapping("/products") // -> Prefixo base de todas as rotas deste controlador
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // HU-01 → STATUS GLOBAL DE INFRAESTRUTURA
    // Rota final acessível via Postman: GET http://localhost:8090/products/global-status
    @GetMapping("/global-status")
    public SystemStatusDTO getSystemStatus() {
        return productService.getSystemStatus();
    }

    // LISTAR TODOS OS PRODUTOS
    // Rota final acessível via Postman: GET http://localhost:8090/products
    @GetMapping
    public List<ProductResponseDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    // CRIAR UM NOVO PRODUTO
    // Rota final acessível via Postman: POST http://localhost:8090/products
    @PostMapping
    public void createProduct(@RequestBody CreateProductDTO dto) {
        productService.saveProduct(dto);
    }

    // OBTER DETALHES DE UM PRODUTO ESPECÍFICO
    // Rota final acessível via Postman: GET http://localhost:8090/products/{productId}
    @GetMapping("/{productId}")
    public ProductDetailDTO getProductDetails(@PathVariable String productId) {
        return productService.getProductDetails(productId);
    }
}