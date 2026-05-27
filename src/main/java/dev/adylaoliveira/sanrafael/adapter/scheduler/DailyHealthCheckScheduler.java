package dev.adylaoliveira.sanrafael.adapter.scheduler;

import dev.adylaoliveira.sanrafael.core.entity.Product;
import dev.adylaoliveira.sanrafael.core.dto.ReportDTO;
import dev.adylaoliveira.sanrafael.core.port.HealthReportGateway;
import dev.adylaoliveira.sanrafael.core.port.ProductRepository;
import dev.adylaoliveira.sanrafael.service.ProductService;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class DailyHealthCheckScheduler {

    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    private final ProductRepository repository;
    private final HealthReportGateway gateway;
    private final ProductService service;

    public DailyHealthCheckScheduler(
            ProductRepository repository,
            HealthReportGateway gateway,
            ProductService service
    ) {
        this.repository = repository;
        this.gateway = gateway;
        this.service = service;
    }

    @PostConstruct
    public void startRoutine() {

        System.out.println("Scheduler iniciado.");

        final Runnable task = () -> {

            System.out.println("Iniciando verificación diaria de productos...");

            try {

                List<Product> products = repository.findAllProducts();

                for (Product product : products) {

                    try {

                        ReportDTO report = gateway.getHealthReport(
                                product.healthURL()
                        );

                        service.addAutomaticReport(
                                product.id(),
                                report
                        );

                        System.out.println(
                                "Report salvo para: "
                                        + product.name()
                        );

                    } catch (Exception e) {

                        System.out.println(
                                "Error verificando producto: "
                                        + product.name()
                        );
                    }
                }

                System.out.println(
                        "Finalizei coleta de status dos produtos."
                );

            } catch (Exception e) {

                System.out.println(
                        "Erro geral do scheduler."
                );
            }
        };

        // executa UMA vez quando inicia
        task.run();

        // depois executa 1 vez por dia
        scheduler.scheduleAtFixedRate(
                task,
                24,
                24,
                TimeUnit.HOURS
        );
    }
}