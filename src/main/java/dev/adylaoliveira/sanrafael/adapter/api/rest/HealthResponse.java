package dev.adylaoliveira.sanrafael.adapter.api.rest;

import java.util.List;

public record HealthResponse(
        int diskPercentage,
        int ramPercentage,
        List<HealthEventResponse> events
) {
}