package dev.adylaoliveira.sanrafael.service;

import dev.adylaoliveira.sanrafael.core.dto.ReportDTO;
import dev.adylaoliveira.sanrafael.core.entity.Event;
import dev.adylaoliveira.sanrafael.core.entity.Product;
import dev.adylaoliveira.sanrafael.core.entity.Report;
import dev.adylaoliveira.sanrafael.core.port.HealthReportGateway;
import dev.adylaoliveira.sanrafael.core.port.ProductRepository;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
    }

    /**
     * Método acionado pelo Scheduler para rodar a checagem com a URL dinâmica do Docker.
     */
    public void addAutomaticReport(String productId, String mockDockerUrl) {
        try {
            // CORREÇÃO: Transforma a String mockDockerUrl em um objeto java.net.URL exigido pela interface
            URL urlParam = new URI(mockDockerUrl).toURL();

            // Chama o gateway usando o método real e o tipo correto (URL)
            ReportDTO reportDTO = healthReportGateway.getHealthReport(urlParam);

            if (reportDTO == null) {
                return;
            }

            // Converte a lista de EventDTO para as Entidades reais de domínio (Event) com ID do tipo UUID
            List<Event> domainEvents = new ArrayList<>();
            if (reportDTO.events() != null) {
                reportDTO.events().forEach(eDto -> {
                    domainEvents.add(new Event(
                            UUID.randomUUID(), // Tipo UUID puro exigido pelo seu Record Event
                            eDto.condition(),
                            eDto.description()
                    ));
                });
            }

            // Converte DTO → Entidade do Domínio (Report)
            Report report = new Report(
                    generateReportId(),
                    new Date(),
                    domainEvents,
                    reportDTO.ramUsagePercentage(),
                    reportDTO.diskUsagePercentage()
            );

            // Persiste no repositório
            repository.addReportToProduct(productId, report);

        } catch (Exception e) {
            throw new RuntimeException("Falló el procesamiento del reporte automático para: " + mockDockerUrl, e);
        }
    }

    public void requestStatusOfAllProducts() {
        List<Product> products = repository.findAllProducts();
        if (products == null || products.isEmpty()) {
            return;
        }

        int portOffset = 0;
        for (Product product : products) {
            int targetPort = 8081 + (portOffset % 5);
            String mockDockerUrl = "http://localhost:" + targetPort + "/health";

            try {
                addAutomaticReport(product.id(), mockDockerUrl);
                System.out.println("Report salvo para: " + product.name());
            } catch (Exception e) {
                System.err.println("Erro ao coletar status de: " + product.name() + " -> " + e.getMessage());
            }
            portOffset++;
        }
        System.out.println("Finalizei coleta de status dos produtos.");
    }

    private String generateReportId() {
        return java.util.UUID.randomUUID().toString();
    }
}