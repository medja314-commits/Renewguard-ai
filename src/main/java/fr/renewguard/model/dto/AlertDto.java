package fr.renewguard.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.renewguard.model.enums.AlertSeverity;

import java.time.LocalDateTime;

public class AlertDto {

@JsonProperty("id") private long id;

@JsonProperty("severity") private AlertSeverity severity;

@JsonProperty("title") private String title;

@JsonProperty("description") private String description;

@JsonProperty("timestamp") private LocalDateTime timestamp;

@JsonProperty("resolved") private boolean resolved;

@JsonProperty("resolved_at") private LocalDateTime resolvedAt;

@JsonProperty("suggested_action") private String suggestedAction;

public long getId() { return id; }

public AlertSeverity getSeverity() { return severity; }

public String getTitle() { return title; }

public String getDescription() { return description; }

public LocalDateTime getTimestamp() { return timestamp; }

public boolean isResolved() { return resolved; }

public LocalDateTime getResolvedAt() { return resolvedAt; }

public String getSuggestedAction() { return suggestedAction; }

public void setResolved(boolean resolved) { this.resolved = resolved; }

public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }

}
