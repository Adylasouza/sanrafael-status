package dev.adylaoliveira.sanrafael.adapter.controller;

import dev.adylaoliveira.sanrafael.core.entity.Product;
import dev.adylaoliveira.sanrafael.core.port.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private final ProductRepository repository;

    public DashboardController(ProductRepository repository) {
        this.repository = repository;
    }

    // 1. ROTA PÚBLICA: Renderiza a página HTML do Dashboard
    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("products", repository.findAllProducts());
        return "dashboard"; // Retorna src/main/resources/templates/dashboard.html
    }

    // 2. ROTA PÚBLICA: Interface visual de login para o Administrador (AQUI ESTAVA O ERRO)
    @GetMapping("/auth/login")
    public String loginPage() {
        return "login"; // Retorna src/main/resources/templates/login.html (Retorna uma String!)
    }

    // 3. API PÚBLICA: Endpoint que o JavaScript consome em tempo real (Retorna um Map/JSON!)
    @GetMapping("/status")
    @ResponseBody
    public Map<String, Object> getRealTimeStatus() {
        List<Product> allProducts = repository.findAllProducts();

        dev.adylaoliveira.sanrafael.core.constant.Condition globalStatus =
                dev.adylaoliveira.sanrafael.core.constant.Condition.CONDITION_ACTIVE;

        List<Map<String, Object>> productsWithCalculatedStatus = new ArrayList<>();

        for (Product product : allProducts) {
            dev.adylaoliveira.sanrafael.core.constant.Condition productStatus = product.getCalculatedStatus();
            double availability = product.getAvailabilityPercentage();

            if (productStatus == dev.adylaoliveira.sanrafael.core.constant.Condition.CONDITION_ERROR) {
                globalStatus = dev.adylaoliveira.sanrafael.core.constant.Condition.CONDITION_ERROR;
            } else if (productStatus == dev.adylaoliveira.sanrafael.core.constant.Condition.CONDITION_WARNING
                    && globalStatus != dev.adylaoliveira.sanrafael.core.constant.Condition.CONDITION_ERROR) {
                globalStatus = dev.adylaoliveira.sanrafael.core.constant.Condition.CONDITION_WARNING;
            }

            Map<String, Object> pMap = new HashMap<>();
            pMap.put("id", product.id());
            pMap.put("name", product.name());
            pMap.put("reports", product.status());
            pMap.put("status", productStatus.toString());
            pMap.put("availability", String.format("%.2f%%", availability));

            productsWithCalculatedStatus.add(pMap);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("globalStatus", globalStatus.toString());
        response.put("products", productsWithCalculatedStatus);

        return response;
    }
}