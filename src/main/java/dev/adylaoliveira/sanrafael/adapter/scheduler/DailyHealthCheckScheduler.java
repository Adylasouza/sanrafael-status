package dev.adylaoliveira.sanrafael.adapter.scheduler;

import dev.adylaoliveira.sanrafael.core.entity.Product;
import dev.adylaoliveira.sanrafael.core.port.ProductRepository;
import dev.adylaoliveira.sanrafael.service.StatusRequestService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DailyHealthCheckScheduler {

    private final ProductRepository repository;
    private final StatusRequestService statusService;

    // Injeção de dependências via construtor (Boa prática da Arquitetura Hexagonal)
    public DailyHealthCheckScheduler(ProductRepository repository, StatusRequestService statusService) {
        this.repository = repository;
        this.statusService = statusService;
    }

    /**
     * Executa a checagem automática diária/periódica de saúde dos sistemas do hospital.
     * Cron configurado para rodar de forma automatizada (ex: todos os dias à meia-noite, ou periodicamente).
     */
    @Scheduled(cron = "0 0 0 * * ?") // Altere ou mantenha o tempo conforme o padrão do seu projeto
    public void executeDailyCheck() {
        List<Product> products = repository.findAllProducts();

        if (products == null || products.isEmpty()) {
            return;
        }

        // Contador para distribuir as requisições entre as portas dos containers Docker (8081 a 8085)
        int portOffset = 0;

        for (Product product : products) {
            // RESOLUÇÃO DO ERRO: Como o Record Product não tem URL fixa, geramos a URL dinâmica
            // apontando para os containers Docker configurados (portas 8081 até 8085)
            int targetPort = 8081 + (portOffset % 5);
            String mockDockerUrl = "http://localhost:" + targetPort + "/health";

            try {
                // Dispara o serviço que consome o Gateway REST e atualiza o estado do produto
                statusService.addAutomaticReport(product.id(), mockDockerUrl);
            } catch (Exception e) {
                // Impede que a falha de conexão de um container derrube a verificação dos outros sistemas
                System.err.println("Error consultando el estado para el sistema " + product.name() + " en " + mockDockerUrl + ": " + e.getMessage());
            }

            portOffset++;
        }
    }
}