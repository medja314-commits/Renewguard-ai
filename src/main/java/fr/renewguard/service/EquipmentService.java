package fr.renewguard.service;

import com.fasterxml.jackson.core.type.TypeReference;

import fr.renewguard.model.dto.EquipmentDto;

import fr.renewguard.model.enums.EquipmentStatus;

import java.util.List;

import java.util.Map;

import java.util.concurrent.CompletableFuture;

public final class EquipmentService {

private static final EquipmentService INSTANCE = new EquipmentService();

private final ApiClient api = ApiClient.getInstance();

private EquipmentService() {}

public static EquipmentService getInstance() { return INSTANCE; }

public CompletableFuture<List<EquipmentDto>> getAll() {

return api.get("/equipment", new TypeReference<List<EquipmentDto>>() {});

}

public CompletableFuture<EquipmentDto> getById(long id) {

return api.get("/equipment/" + id, EquipmentDto.class);

}

public CompletableFuture<EquipmentDto> setStatus(long id, EquipmentStatus status) {

Map<String, String> body = Map.of("status", status.name());

return api.patch("/equipment/" + id + "/status", body, EquipmentDto.class);

}

public CompletableFuture<EquipmentDto> create(EquipmentDto dto) {

return api.post("/equipment", dto, EquipmentDto.class);

}

public CompletableFuture<EquipmentDto> update(long id, EquipmentDto dto) {

return api.put("/equipment/" + id, dto, EquipmentDto.class);

}

public CompletableFuture<Void> delete(long id) {

return api.delete("/equipment/" + id);

}

}
