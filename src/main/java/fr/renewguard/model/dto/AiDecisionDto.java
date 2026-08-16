package fr.renewguard.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class AiDecisionDto {

@JsonProperty("id") private long id;

@JsonProperty("action") private String action;

@JsonProperty("equipment_name") private String equipmentName;

@JsonProperty("equipment_id") private Long equipmentId;

@JsonProperty("reason") private String reason;

@JsonProperty("impact_label") private String impactLabel;

@JsonProperty("confidence_percent") private int confidencePercent;

@JsonProperty("status") private String status;

@JsonProperty("timestamp") private LocalDateTime timestamp;

@JsonProperty("acknowledged") private boolean acknowledged;

public long getId() { return id; }

public String getAction() { return action; }

public String getEquipmentName() { return equipmentName; }

public Long getEquipmentId() { return equipmentId; }

public String getReason() { return reason; }

public String getImpactLabel() { return impactLabel; }

public int getConfidencePercent() { return confidencePercent; }

public String getStatus() { return status; }

public LocalDateTime getTimestamp() { return timestamp; }

public boolean isAcknowledged() { return acknowledged; }

public void setAcknowledged(boolean v) { this.acknowledged = v; }

public boolean isPending() { return "PENDING".equalsIgnoreCase(status); }

public boolean isDone() { return "DONE".equalsIgnoreCase(status); }

}
