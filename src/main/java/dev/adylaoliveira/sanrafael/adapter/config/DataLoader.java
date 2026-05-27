package dev.adylaoliveira.sanrafael.adapter.config;

import dev.adylaoliveira.sanrafael.core.dto.CreateProductDTO;
import dev.adylaoliveira.sanrafael.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductService service;

    public DataLoader(ProductService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) throws Exception {

        service.saveProduct(
                new CreateProductDTO(
                        "Historia Clínica Electrónica",
                        new URL("http://localhost:8081/health")
                )
        );

        service.saveProduct(
                new CreateProductDTO(
                        "Sistema de Laboratorio Clínico",
                        new URL("http://localhost:8082/health")
                )
        );

        service.saveProduct(
                new CreateProductDTO(
                        "Sistema PACS Radiológico",
                        new URL("http://localhost:8083/health")
                )
        );

        service.saveProduct(
                new CreateProductDTO(
                        "Portal de Pacientes",
                        new URL("http://localhost:8084/health")
                )
        );

        service.saveProduct(
                new CreateProductDTO(
                        "Sistema de Urgencias",
                        new URL("http://localhost:8081/health")
                )
        );

        service.saveProduct(
                new CreateProductDTO(
                        "Monitoreo UCI",
                        new URL("http://localhost:8085/health")
                )
        );

        service.saveProduct(
                new CreateProductDTO(
                        "Sistema de Farmacia Hospitalaria",
                        new URL("http://localhost:8085/health")

                )
        );

        service.saveProduct(
                new CreateProductDTO(
                        "Agenda Médica Inteligente",
                        new URL("http://localhost:8081/health")
                )
        );

        service.saveProduct(
                new CreateProductDTO(
                        "Infraestructura de Base de Datos",
                        new URL("http://localhost:8084/health")
                )
        );

        service.saveProduct(
                new CreateProductDTO(
                        "Servicios de Integración HL7",
                        new URL("http://localhost:8084/health")
                )
        );

        service.saveProduct(new CreateProductDTO(
                "Sistema de Vacunación",
                new URL("http://localhost:8082/health")
        ));

        service.saveProduct(new CreateProductDTO(
                "Sistema de Cirugía",
                new URL("http://localhost:8082/health")
        ));

        service.saveProduct(new CreateProductDTO(
                "Sistema de Neonatología",
                new URL("http://localhost:8083/health")
        ));

        service.saveProduct(new CreateProductDTO(
                "Sistema de Ambulancias",
                new URL("http://localhost:8084/health")
        ));

        service.saveProduct(new CreateProductDTO(
                "Sistema de Facturación",
                new URL("http://localhost:8083/health")
        ));

        service.saveProduct(new CreateProductDTO(
                "Sistema de Recursos Humanos",
                new URL("http://localhost:8083/health")
        ));

        service.saveProduct(new CreateProductDTO(
                "Sistema de Telemedicina",
                new URL("http://localhost:8083/health")
        ));

        service.saveProduct(new CreateProductDTO(
                "Sistema de Hemodiálisis",
                new URL("http://localhost:8083/health")
        ));

        service.saveProduct(new CreateProductDTO(
                "Sistema de Inventario",
                new URL("http://localhost:8084/health")
        ));

        service.saveProduct(new CreateProductDTO(
                "Sistema de Seguridad Hospitalaria",
                new URL("http://localhost:8085/health")
        ));

        service.saveProduct(new CreateProductDTO(
                "Sistema de Nutrición",
                new URL("http://localhost:8085/health")
        ));

        service.saveProduct(new CreateProductDTO(
                "Sistema de Gestión Quirúrgica",
                new URL("http://localhost:8084/health")
        ));

        System.out.println("Productos del Hospital San Rafael cargados correctamente.");
    }
}