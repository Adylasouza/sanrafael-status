package dev.adylaoliveira.sanrafael.core.entity;

import dev.adylaoliveira.sanrafael.core.constant.Condition;
import java.util.UUID;

public record Event(
        UUID id,
        Condition condition,
        String description
) {
}
