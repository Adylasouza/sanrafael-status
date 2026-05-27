package dev.adylaoliveira.sanrafael.service;

import dev.adylaoliveira.sanrafael.core.constant.Condition;
import dev.adylaoliveira.sanrafael.core.dto.*;
import dev.adylaoliveira.sanrafael.core.entity.Event;
import dev.adylaoliveira.sanrafael.core.entity.Product;
import dev.adylaoliveira.sanrafael.core.entity.Report;
import dev.adylaoliveira.sanrafael.core.exception.NotFoundException;
import dev.adylaoliveira.sanrafael.core.port.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    // BASIC CRUD
    public List<ProductResponseDTO> getAllProducts() {
        return repository.findAllProducts()
                .stream()
                .map(p -> new ProductResponseDTO(
                        p.id(),
                        p.name(),
                        p.getCalculatedStatus() // Utiliza o método robusto que criado dentro do Record
                ))
                .toList();
    }

    public void saveProduct(CreateProductDTO dto) {
        // Removido o quarto parâmetro (healthURL) que não existe no diagrama de classes oficial
        Product product = new Product(
                java.util.UUID.randomUUID().toString(),
                dto.name(),
                new ArrayList<>() // Mapeia diretamente para a lista 'status' de relatórios
        );

        repository.saveProduct(product);
    }

    // REPORTS
    public void addReportToProduct(String productId, Report report) {
        Product product = repository.findAllProducts()
                .stream()
                .filter(p -> p.id().equals(productId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Product not found"));

        repository.addReportToProduct(product.id(), report);
    }

    // EVENTS
    public void addEventToReport(String reportId, Event event) {
        repository.addEventToReport(reportId, event);
    }

    // HU-02 - PRODUCT DETAIL
    public ProductDetailDTO getProductDetails(String productId) {
        Product product = repository.findAllProducts()
                .stream()
                .filter(p -> p.id().equals(productId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Product not found"));

        List<Report> sortedReports = product.status()
                .stream()
                .sorted((r1, r2) -> r2.date().compareTo(r1.date()))
                .toList();

        return new ProductDetailDTO(
                product.id(),
                product.name(),
                sortedReports
        );
    }

    // STATUS LOGIC
    public Condition getProductStatus(Product product) {
        // Reaproveita diretamente a lógica que colocamos dentro da entidade para manter o código limpo
        return product.getCalculatedStatus();
    }

    // HU-01 - SYSTEM STATUS
    public SystemStatusDTO getSystemStatus() {
        List<Product> products = repository.findAllProducts();

        List<ProductStatusDTO> productStatuses = products.stream()
                .map(p -> new ProductStatusDTO(
                        p.id(),
                        p.name(),
                        p.getCalculatedStatus() //Substituído chamada depreciada
                ))
                .toList();

        Condition globalStatus = productStatuses.stream()
                .map(ProductStatusDTO::status)
                .max(Condition::compareTo)
                .orElse(Condition.CONDITION_ACTIVE);

        return new SystemStatusDTO(globalStatus, productStatuses);
    }

    // ATUALIZAÇÃO DIARIA
    public void addAutomaticReport(
            String productId,
            ReportDTO reportDTO
    ) {
        Product product = repository.findById(productId);

        if (product == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        // Instancia o relatório amarrando a capacidade de RAM e Disco em porcentagens
        Report report = new Report(
                java.util.UUID.randomUUID().toString(),
                reportDTO.date(),
                reportDTO.events(),
                reportDTO.ramUsagePercentage(),
                reportDTO.diskUsagePercentage()
        );

        // CORREÇÃO: Alterado de .reports() para .status()
        product.status().add(report);

        // Persiste as alterações calculadas no repositório de dados
        repository.saveProduct(product);
    }
}