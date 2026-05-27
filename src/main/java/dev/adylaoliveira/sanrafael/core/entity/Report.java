package dev.adylaoliveira.sanrafael.core.entity;

import dev.adylaoliveira.sanrafael.core.constant.Condition;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public record Report(
        String id,
        Date date,
        List<Event> events, // Atributo mapeado
        double ramPercentage,   // Exigência: Capacidade de RAM em porcentagem
        double discoPercentage  // Exigência: Capacidade de Disco Duro em porcentagem
) {
    public Condition calculateCondition() {
        if (isAnyEventWithCondition(Condition.CONDITION_ERROR)){
            return Condition.CONDITION_ERROR;
        }
        if (isAnyEventWithCondition(Condition.CONDITION_WARNING)){
            return Condition.CONDITION_WARNING;
        }

        return Condition.CONDITION_ACTIVE;
    }

    private boolean isAnyEventWithCondition(Condition condition) {
        return events.stream().anyMatch(event -> event.condition().equals(condition));
    }

}
