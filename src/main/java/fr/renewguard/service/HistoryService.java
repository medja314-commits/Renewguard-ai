package fr.renewguard.service;

import com.fasterxml.jackson.core.type.TypeReference;

import fr.renewguard.model.dto.AlertDto;

import fr.renewguard.model.dto.HistoryPointDto;

import fr.renewguard.model.dto.HistorySummaryDto;

import java.util.List;

import java.util.concurrent.CompletableFuture;

public final class HistoryService {

private static final HistoryService INSTANCE = new HistoryService();

private final ApiClient api = ApiClient.getInstance();

private HistoryService() {}

public static HistoryService getInstance() { return INSTANCE; }

public CompletableFuture<List<HistoryPointDto>> getHistory(String period) {

return api.get("/history?period=" + period, new TypeReference<List<HistoryPointDto>>() {});

}

public CompletableFuture<List<HistoryPointDto>> getHistory(String from, String to) {

return api.get("/history?from=" + from + "&to=" + to, new TypeReference<List<HistoryPointDto>>() {});

}

public CompletableFuture<HistorySummaryDto> getSummary(String period) {

return api.get("/history/summary?period=" + period, HistorySummaryDto.class);

}

public CompletableFuture<List<AlertDto>> getAlerts() {

return api.get("/alerts", new TypeReference<List<AlertDto>>() {});

}

public CompletableFuture<List<AlertDto>> getAlerts(String severity) {

return api.get("/alerts?severity=" + severity, new TypeReference<List<AlertDto>>() {});

}

public CompletableFuture<AlertDto> resolveAlert(long id) {

return api.post("/alerts/" + id + "/resolve", null, AlertDto.class);

}

public CompletableFuture<byte[]> exportReport(String period, String format) {

return api.get("/history/export?period=" + period + "&format=" + format, new TypeReference<byte[]>() {});

}

}
