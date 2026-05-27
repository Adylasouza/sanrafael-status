package dev.adylaoliveira.sanrafael.core.port;

import dev.adylaoliveira.sanrafael.core.entity.Event;
import dev.adylaoliveira.sanrafael.core.entity.Product;
import dev.adylaoliveira.sanrafael.core.entity.Report;

import java.util.List;


public interface ProductRepository {

    List<Product> findAllProducts();

    void saveProduct(Product product);

    Product findById(String productId);

    void addReportToProduct(String productId, Report report);

    void deleteReport(String reportId);

    void addEventToReport(String reportId, Event event);

    void deleteEvent(String eventId);
}