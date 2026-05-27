package dev.adylaoliveira.sanrafael.core.dto;

import dev.adylaoliveira.sanrafael.core.entity.Event;

import java.util.Date;
import java.util.List;

public record ReportDTO(
        Date date,
        List<Event> events,
        int ramUsagePercentage,
        int diskUsagePercentage
) {
}
