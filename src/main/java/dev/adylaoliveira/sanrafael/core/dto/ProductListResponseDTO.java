package dev.adylaoliveira.sanrafael.core.dto;

import java.util.List;

public record ProductListResponseDTO(
        List<ProductResponseDTO> products
) {}