package dev.adylaoliveira.sanrafael.core.port;

import dev.adylaoliveira.sanrafael.core.dto.ReportDTO;

import java.net.URL;

public interface HealthReportGateway {

    ReportDTO getHealthReport(URL healthEndpoint);

}