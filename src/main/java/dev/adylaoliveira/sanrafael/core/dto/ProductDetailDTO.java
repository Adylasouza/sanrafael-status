package dev.adylaoliveira.sanrafael.core.dto;

import dev.adylaoliveira.sanrafael.core.entity.Report;

import java.util.List;

public record ProductDetailDTO(
        String id,
        String name,
        List<Report> reports
) {
}