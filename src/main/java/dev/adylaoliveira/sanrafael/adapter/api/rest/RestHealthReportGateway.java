package dev.adylaoliveira.sanrafael.adapter.api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.adylaoliveira.sanrafael.core.constant.Condition;
import dev.adylaoliveira.sanrafael.core.dto.ReportDTO;
import dev.adylaoliveira.sanrafael.core.entity.Event;
import dev.adylaoliveira.sanrafael.core.port.HealthReportGateway;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestHealthReportGateway implements HealthReportGateway {

    private final HttpClient client;
    private final ObjectMapper mapper;

    public RestHealthReportGateway() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    @Override
    public ReportDTO getHealthReport(URL healthEndpoint) {

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(healthEndpoint.toString()))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            HealthResponse healthResponse = mapper.readValue(
                    response.body(),
                    HealthResponse.class
            );

            // Importe: java.util.stream.Collectors;

            List<Event> events = healthResponse.events()
                    .stream()
                    .map(event -> new Event(
                            UUID.randomUUID(),
                            mapCondition(event.condition()),
                            event.description()
                    ))
                    .collect(Collectors.toList()); // <--- Retorna um ArrayList mutável padrão!

            return new ReportDTO(
                    new Date(),
                    events,
                    healthResponse.ramPercentage(),
                    healthResponse.diskPercentage()
            );

        } catch (Exception e) {

            Event errorEvent = new Event(
                    UUID.randomUUID(),
                    Condition.CONDITION_ERROR,
                    "Servicio fuera de línea"
            );

            return new ReportDTO(
                    new Date(),
                    List.of(errorEvent),
                    0,
                    0
            );
        }
    }

    private Condition mapCondition(String condition) {

        return switch (condition.toUpperCase()) {

            case "ERROR" -> Condition.CONDITION_ERROR;

            case "WARNING" -> Condition.CONDITION_WARNING;

            default -> Condition.CONDITION_ACTIVE;
        };
    }
}