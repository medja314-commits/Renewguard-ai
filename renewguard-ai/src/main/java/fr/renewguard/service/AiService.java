package fr.renewguard.service;
 
import com.fasterxml.jackson.core.type.TypeReference;
import fr.renewguard.model.dto.AiDecisionDto;
import fr.renewguard.model.dto.ChatResponseDto;
import fr.renewguard.model.dto.PredictionDto;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
 
public final class AiService {
    private static final AiService INSTANCE = new AiService();
    private final ApiClient api = ApiClient.getInstance();
    private AiService() {}
    public static AiService getInstance() { return INSTANCE; }
 
    public CompletableFuture<List<AiDecisionDto>> getDecisions() {
        return api.get("/ai/decisions", new TypeReference<List<AiDecisionDto>>() {});
    }
    public CompletableFuture<List<AiDecisionDto>> getDecisions(int limit) {
        return api.get("/ai/decisions?limit=" + limit, new TypeReference<List<AiDecisionDto>>() {});
    }
    public CompletableFuture<PredictionDto> getPredictions() {
        return api.get("/ai/predictions", PredictionDto.class);
    }
    public CompletableFuture<ChatResponseDto> sendMessage(String message) {
        Map<String, String> body = Map.of("message", message);
        return api.post("/ai/chat", body, ChatResponseDto.class);
    }
    public CompletableFuture<Void> acknowledgeDecision(long id) {
        return api.patch("/ai/decisions/" + id + "/acknowledge", Map.of(), Void.class).thenApply(v -> null);
    }
}