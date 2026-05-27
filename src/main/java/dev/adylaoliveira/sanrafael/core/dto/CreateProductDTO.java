package dev.adylaoliveira.sanrafael.core.dto;

import java.net.URL;

public record CreateProductDTO(
        String name,
        URL healthURL
) {}