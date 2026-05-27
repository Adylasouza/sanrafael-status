package dev.adylaoliveira.sanrafael.adapter.repository.inmemory;

import dev.adylaoliveira.sanrafael.core.entity.Event;
import dev.adylaoliveira.sanrafael.core.entity.Product;
import dev.adylaoliveira.sanrafael.core.entity.Report;
import dev.adylaoliveira.sanrafael.core.port.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    // SOLUÇÃO: Trocamos List por ConcurrentHashMap.
    // O ID do produto será a chave (Key), impedindo duplicatas.
    private final Map<String, Product> products = new ConcurrentHashMap<>();

    @Override
    public List<Product> findAllProducts() {
        // Retorna uma cópia segura em formato de lista para a camada de serviço.
        // Isso evita erros de concorrência se alguém ler enquanto o Scheduler grava.
        return new ArrayList<>(products.values());
    }

    @Override
    public void saveProduct(Product product) {
        // O método .put() resolve o bug!
        // Se o produto com esse ID não existir, ele insere.
        // Se já existir (como no caso da atualização do Scheduler), ele apenas substitui o antigo pelo atualizado.
        products.put(product.id(), product);
    }

    @Override
    public Product findById(String productId) {
        // Busca direta via O(1) no mapa, muito mais rápido do que percorrer uma lista com Stream.
        Product product = products.get(productId);
        if (product == null) {
            throw new RuntimeException("Producto no encontrado: " + productId);
        }
        return product;
    }

    @Override
    public void addReportToProduct(String productId, Report report) {
        Product p = products.get(productId);
        if (p != null) {
            p.reports().add(report);
        }
    }

    @Override
    public void deleteReport(String reportId) {
        products.values().forEach(p ->
                p.reports().removeIf(r -> r.id().equals(reportId))
        );
    }

    @Override
    public void addEventToReport(String reportId, Event event) {
        products.values().stream()
                .flatMap(p -> p.reports().stream())
                .filter(r -> r.id().equals(reportId))
                .findFirst()
                .ifPresent(r -> r.events().add(event));
    }

    @Override
    public void deleteEvent(String eventId) {
        products.values().stream()
                .flatMap(p -> p.reports().stream())
                .forEach(r -> r.events().removeIf(e -> e.id().toString().equals(eventId)));
    }
}