package fr.renewguard.service;

import com.fasterxml.jackson.core.type.TypeReference;

import fr.renewguard.model.dto.PriorityLevelDto;

import fr.renewguard.model.dto.RuleDto;

import java.util.List;

import java.util.concurrent.CompletableFuture;

public final class PriorityService {

private static final PriorityService INSTANCE = new PriorityService();

private final ApiClient api = ApiClient.getInstance();

private PriorityService() {}

public static PriorityService getInstance() { return INSTANCE; }

public CompletableFuture<List<PriorityLevelDto>> getPriorities() {

return api.get("/priorities", new TypeReference<List<PriorityLevelDto>>() {});

}

public CompletableFuture<List<PriorityLevelDto>> savePriorities(List<PriorityLevelDto> levels) {

return api.put("/priorities", levels, new TypeReference<List<PriorityLevelDto>>() {});

}

public CompletableFuture<List<RuleDto>> getRules() {

return api.get("/rules", new TypeReference<List<RuleDto>>() {});

}

public CompletableFuture<List<RuleDto>> saveRules(List<RuleDto> rules) {

return api.put("/rules", rules, new TypeReference<List<RuleDto>>() {});

}

public CompletableFuture<RuleDto> toggleRule(long id, boolean active) {

return api.patch("/rules/" + id, new ToggleBody(active), RuleDto.class);

}

private record ToggleBody(boolean active) {}

}
