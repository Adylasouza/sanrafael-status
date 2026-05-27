package dev.adylaoliveira.sanrafael.adapter.repository.inmemory;

import dev.adylaoliveira.sanrafael.core.entity.Product;
import dev.adylaoliveira.sanrafael.core.entity.Report;
import dev.adylaoliveira.sanrafael.core.entity.Event;
import dev.adylaoliveira.sanrafael.core.port.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    // Banco de dados em memória Thread-Safe
    private final ConcurrentHashMap<String, Product> db = new ConcurrentHashMap<>();

    @Override
    public List<Product> findAllProducts() {
        return new ArrayList<>(db.values());
    }

    @Override
    public Product findById(String id) {
        return db.get(id);
    }

    @Override
    public void saveProduct(Product product) {
        db.put(product.id(), product);
    }

    // ==========================================
    // IMPLEMENTAÇÃO DOS NOVOS MÉTODOS DA INTERFACE
    // ==========================================

    @Override
    public void addReportToProduct(String productId, Report report) {
        Product product = db.get(productId);
        if (product != null) {
            // Como Records são imutáveis, criamos uma nova lista baseada na anterior
            List<Report> updatedReports = new ArrayList<>(product.status());
            updatedReports.add(report);

            // Criamos uma nova instância do produto com a lista atualizada
            Product updatedProduct = new Product(product.id(), product.name(), updatedReports);
            db.put(productId, updatedProduct);
        }
    }

    @Override
    public void deleteReport(String reportId) {
        // Varre os produtos para encontrar e remover o relatório correspondente
        for (String productId : db.keySet()) {
            Product product = db.get(productId);
            List<Report> updatedReports = new ArrayList<>(product.status());

            boolean removed = updatedReports.removeIf(report -> report.id().equals(reportId));

            if (removed) {
                Product updatedProduct = new Product(product.id(), product.name(), updatedReports);
                db.put(productId, updatedProduct);
                break; // Relatório encontrado e removido
            }
        }
    }

    @Override
    public void addEventToReport(String reportId, Event event) {
        // Procura o relatório que contém este ID para adicionar o evento
        for (String productId : db.keySet()) {
            Product product = db.get(productId);
            List<Report> updatedReports = new ArrayList<>();
            boolean reportFound = false;

            for (Report report : product.status()) {
                if (report.id().equals(reportId)) {
                    List<Event> updatedEvents = new ArrayList<>(report.events());
                    updatedEvents.add(event);

                    // Cria um novo record de relatório com o novo evento injetado
                    Report updatedReport = new Report(
                            report.id(),
                            report.date(),
                            updatedEvents,
                            report.ramPercentage(),
                            report.discoPercentage()
                    );
                    updatedReports.add(updatedReport);
                    reportFound = true;
                } else {
                    updatedReports.add(report);
                }
            }

            if (reportFound) {
                Product updatedProduct = new Product(product.id(), product.name(), updatedReports);
                db.put(productId, updatedProduct);
                break;
            }
        }
    }

    @Override
    public void deleteEvent(String eventId) {
        // Varre recursivamente os produtos e relatórios para remover o evento
        for (String productId : db.keySet()) {
            Product product = db.get(productId);
            List<Report> updatedReports = new ArrayList<>();
            boolean eventRemoved = false;

            for (Report report : product.status()) {
                List<Event> updatedEvents = new ArrayList<>(report.events());
                boolean removed = updatedEvents.removeIf(event -> event.id().equals(eventId));

                if (removed) {
                    Report updatedReport = new Report(
                            report.id(),
                            report.date(),
                            updatedEvents,
                            report.ramPercentage(),
                            report.discoPercentage()
                    );
                    updatedReports.add(updatedReport);
                    eventRemoved = true;
                } else {
                    updatedReports.add(report);
                }
            }

            if (eventRemoved) {
                Product updatedProduct = new Product(product.id(), product.name(), updatedReports);
                db.put(productId, updatedProduct);
                break;
            }
        }
    }
}