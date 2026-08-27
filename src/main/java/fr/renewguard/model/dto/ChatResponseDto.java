package fr.renewguard.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ChatResponseDto {

@JsonProperty("message") private String message;

@JsonProperty("timestamp") private LocalDateTime timestamp;

@JsonProperty("model_version") private String modelVersion;

@JsonProperty("tokens_used") private int tokensUsed;

public String getMessage() { return message; }

public LocalDateTime getTimestamp() { return timestamp; }

public String getModelVersion() { return modelVersion; }

public int getTokensUsed() { return tokensUsed; }

}
