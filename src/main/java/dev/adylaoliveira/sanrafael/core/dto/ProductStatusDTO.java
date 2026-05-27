package dev.adylaoliveira.sanrafael.core.dto;

import dev.adylaoliveira.sanrafael.core.constant.Condition;

public record ProductStatusDTO(
        String id,
        String name,
        Condition status
) {}