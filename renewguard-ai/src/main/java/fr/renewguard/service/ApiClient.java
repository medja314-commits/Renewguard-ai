package fr.renewguard.service;
 
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.renewguard.util.TokenStore;
import okhttp3.*;
 
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
 
public final class ApiClient {
 
    private static final ApiClient INSTANCE = new ApiClient();
 
    private final OkHttpClient http;
    private final ObjectMapper mapper;
    private String baseUrl;
 
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
 
    private ApiClient() {
        this.baseUrl = System.getProperty("api.base-url", "http://localhost:8000");
        this.mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.http = new OkHttpClient.Builder()
            .connectTimeout(5, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
            .addInterceptor(new AuthInterceptor())
            .build();
    }
 
    public static ApiClient getInstance() { return INSTANCE; }
    public void setBaseUrl(String url) { this.baseUrl = url; }
 
    public <T> CompletableFuture<T> get(String path, Class<T> type) {
        Request req = new Request.Builder().url(baseUrl + path).get().build();
        return execute(req, body -> mapper.readValue(body, type));
    }
 
    public <T> CompletableFuture<T> get(String path, TypeReference<T> typeRef) {
        Request req = new Request.Builder().url(baseUrl + path).get().build();
        return execute(req, body -> mapper.readValue(body, typeRef));
    }
 
    public <T> CompletableFuture<T> post(String path, Object body, Class<T> type) {
        Request req = new Request.Builder().url(baseUrl + path).post(toRequestBody(body)).build();
        return execute(req, raw -> mapper.readValue(raw, type));
    }
 
    public <T> CompletableFuture<T> patch(String path, Object body, Class<T> type) {
        Request req = new Request.Builder().url(baseUrl + path).patch(toRequestBody(body)).build();
        return execute(req, raw -> mapper.readValue(raw, type));
    }
 
    public <T> CompletableFuture<T> put(String path, Object body, Class<T> type) {
        Request req = new Request.Builder().url(baseUrl + path).put(toRequestBody(body)).build();
        return execute(req, raw -> mapper.readValue(raw, type));
    }
 
    public <T> CompletableFuture<T> put(String path, Object body, TypeReference<T> typeRef) {
        Request req = new Request.Builder().url(baseUrl + path).put(toRequestBody(body)).build();
        return execute(req, raw -> mapper.readValue(raw, typeRef));
    }
 
    public CompletableFuture<Void> delete(String path) {
        Request req = new Request.Builder().url(baseUrl + path).delete().build();
        return execute(req, raw -> null);
    }
 
    private <T> CompletableFuture<T> execute(Request request, ResponseMapper<T> mapper) {
        CompletableFuture<T> future = new CompletableFuture<>();
        http.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                future.completeExceptionally(e);
            }
            @Override
            public void onResponse(Call call, Response response) {
                try (ResponseBody rb = response.body()) {
                    handleHttpError(response.code(), request.url().encodedPath());
                    String rawBody = rb != null ? rb.string() : "{}";
                    future.complete(mapper.map(rawBody));
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }
        });
        return future;
    }
 
    private void handleHttpError(int code, String path) {
        if (code == 401) {
            TokenStore.getInstance().clear();
            javafx.application.Platform.runLater(
                () -> fr.renewguard.navigation.SceneManager.navigate("auth")
            );
            throw new ApiException(401, "Unauthorized — session expiree");
        }
        if (code == 403) throw new ApiException(403, "Acces refuse : " + path);
        if (code == 404) throw new ApiException(404, "Ressource introuvable : " + path);
        if (code == 422) throw new ApiException(422, "Donnees invalides envoyees a : " + path);
        if (code >= 500) throw new ApiException(code, "Erreur serveur sur : " + path);
    }
 
    private RequestBody toRequestBody(Object body) {
        try {
            return RequestBody.create(mapper.writeValueAsString(body), JSON);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new RuntimeException("Serialization error", e);
        }
    }
 
    @FunctionalInterface
    private interface ResponseMapper<T> { T map(String body) throws IOException; }
 
    public static class ApiException extends RuntimeException {
        private final int statusCode;
        public ApiException(int code, String message) { super(message); this.statusCode = code; }
        public int getStatusCode() { return statusCode; }
    }
}