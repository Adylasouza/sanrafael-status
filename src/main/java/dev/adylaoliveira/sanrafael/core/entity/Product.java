package dev.adylaoliveira.sanrafael.core.entity;

import dev.adylaoliveira.sanrafael.core.constant.Condition;
import java.util.List;

public record Product(
        String id,
        String name,
        List<Report> status // Atributo que armazena a lista de reportes (Diagrama de clases)
) {
    /**
     * Consideração Técnica: A condição mais crítica entre os eventos determina o estado do produto.
     */
    public Condition getCalculatedStatus() {
        if (status == null || status.isEmpty()) {
            return Condition.CONDITION_ACTIVE;
        }

        Condition worstCondition = Condition.CONDITION_ACTIVE;

        for (Report report : status) {
            if (report.events() != null) {
                for (Event event : report.events()) {
                    // Se houver qualquer erro crítico, o sistema cai imediatamente para o pior estado
                    if (event.condition() == Condition.CONDITION_ERROR) {
                        return Condition.CONDITION_ERROR;
                    }
                    // Se encontrar uma advertência, salva para avaliar se não há erros piores nos próximos laços
                    if (event.condition() == Condition.CONDITION_WARNING) {
                        worstCondition = Condition.CONDITION_WARNING;
                    }
                }
            }
        }
        return worstCondition;
    }

    /**
     * Consideração Técnica (Pág 5): "El porcentaje de disponibilidad para un producto
     * se basa en la sumatoria de los estados actuales de sus reportes."
     */
    public double getAvailabilityPercentage() {
        if (status == null || status.isEmpty()) {
            return 100.0; // Sem incidentes crônicos significa 100% de disponibilidade
        }

        double totalScore = 0;
        int reportCount = status.size();

        for (Report report : status) {
            // Determina de forma isolada qual foi o estado deste relatório específico
            Condition reportCondition = Condition.CONDITION_ACTIVE;
            if (report.events() != null) {
                for (Event event : report.events()) {
                    if (event.condition() == Condition.CONDITION_ERROR) {
                        reportCondition = Condition.CONDITION_ERROR;
                        break;
                    } else if (event.condition() == Condition.CONDITION_WARNING) {
                        reportCondition = Condition.CONDITION_WARNING;
                    }
                }
            }

            // Atribui pesos matemáticos para realizar a sumatória de disponibilidade exigida
            switch (reportCondition) {
                case CONDITION_ACTIVE -> totalScore += 100.0;    // Totalmente disponível
                case CONDITION_WARNING -> totalScore += 50.0;   // Sistema degradado/lento conta metade
                case CONDITION_ERROR -> totalScore += 0.0;       // Sistema completamente fora de serviço
            }
        }

        // Retorna a média aritmética exata da somatória dos estados dos relatórios
        return totalScore / reportCount;
    }
}