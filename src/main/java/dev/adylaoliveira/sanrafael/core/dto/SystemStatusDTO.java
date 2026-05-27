package dev.adylaoliveira.sanrafael.core.dto;

import dev.adylaoliveira.sanrafael.core.constant.Condition;

import java.util.List;

public record SystemStatusDTO(
        Condition globalStatus,
        List<ProductStatusDTO> products
) {}