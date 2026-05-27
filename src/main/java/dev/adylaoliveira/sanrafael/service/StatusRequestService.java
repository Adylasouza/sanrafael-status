package dev.adylaoliveira.sanrafael.service;

import dev.adylaoliveira.sanrafael.core.dto.ReportDTO;
import dev.adylaoliveira.sanrafael.core.entity.Product;
import dev.adylaoliveira.sanrafael.core.entity.Report;
import dev.adylaoliveira.sanrafael.core.port.HealthReportGateway;
import dev.adylaoliveira.sanrafael.core.port.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class StatusRequestService {

    public static final int REQUEST_FREQUENCY = 1;
    public static final TimeUnit REQUEST_TIME_UNIT = TimeUnit.SECONDS;

    private final ProductRepository repository;
    private final HealthReportGateway healthReportGateway;

    private final ScheduledExecutorService scheduler;

    public StatusRequestService(
            ProductRepository repository,
            HealthReportGateway healthReportGateway
    ) {
        this.repository = repository;
        this.healthReportGateway = healthReportGateway;

        this.scheduler = Executors.newScheduledThreadPool(1);

        //this.scheduler.scheduleAtFixedRate(
        //        this::requestStatusOfAllProducts,
        //        0,
        //        REQUEST_FREQUENCY,
        //        REQUEST_TIME_UNIT
        //);
    }

    public void requestStatusOfAllProducts() {

        List<Product> products = repository.findAllProducts();

        for (Product product : products) {

            // 1. chama gateway (HTTP externo)
            ReportDTO reportDTO = healthReportGateway.getHealthReport(
                    product.healthURL()
            );

            // 2. converte DTO → entidade do domínio
            Report report = new Report(
                    generateReportId(),
                    reportDTO.date(),
                    reportDTO.events(),
                    reportDTO.ramUsagePercentage(),
                    reportDTO.diskUsagePercentage()
            );

            // 3. persiste no sistema
            repository.addReportToProduct(product.id(), report);

            // 4. log (opcional)
            System.out.println("Report salvo para: " + product.name());
        }

        System.out.println("Finalizei coleta de status dos produtos.");
    }

    private String generateReportId() {
        return java.util.UUID.randomUUID().toString();
    }
}