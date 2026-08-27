package fr.renewguard.service;

import com.fasterxml.jackson.core.type.TypeReference;

import fr.renewguard.model.dto.EnergySnapshotDto;

import fr.renewguard.model.dto.HistoryPointDto;

import java.util.List;

import java.util.concurrent.CompletableFuture;

public final class EnergyService {

private static final EnergyService INSTANCE = new EnergyService();

private final ApiClient api = ApiClient.getInstance();

private EnergyService() {}

public static EnergyService getInstance() { return INSTANCE; }

public CompletableFuture<EnergySnapshotDto> getSnapshot() {

return api.get("/energy/snapshot", EnergySnapshotDto.class);

}

public CompletableFuture<List<HistoryPointDto>> getHistory(String period) {

return api.get("/energy/history?period=" + period, new TypeReference<List<HistoryPointDto>>() {});

}

public CompletableFuture<EnergySnapshotDto> refreshSnapshot() {

return api.post("/energy/snapshot/refresh", null, EnergySnapshotDto.class);

}

}
