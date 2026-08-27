package fr.renewguard.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RuleDto {

@JsonProperty("id") private long id;

@JsonProperty("condition") private String condition;

@JsonProperty("action") private String action;

@JsonProperty("active") private boolean active;

@JsonProperty("priority_level") private int priorityLevel;

@JsonProperty("threshold_value") private double thresholdValue;

@JsonProperty("threshold_unit") private String thresholdUnit;

public long getId() { return id; }

public String getCondition() { return condition; }

public String getAction() { return action; }

public boolean isActive() { return active; }

public int getPriorityLevel() { return priorityLevel; }

public double getThresholdValue() { return thresholdValue; }

public String getThresholdUnit() { return thresholdUnit; }

public void setActive(boolean active) { this.active = active; }

}
