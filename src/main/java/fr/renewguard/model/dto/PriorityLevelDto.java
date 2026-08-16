package fr.renewguard.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.renewguard.model.enums.PriorityLevel;

import java.util.List;

public class PriorityLevelDto {

@JsonProperty("level") private int level;

@JsonProperty("priority") private PriorityLevel priority;

@JsonProperty("label") private String label;

@JsonProperty("description") private String description;

@JsonProperty("equipment_ids") private List<Long> equipmentIds;

@JsonProperty("total_power_watts") private int totalPowerWatts;

public int getLevel() { return level; }

public PriorityLevel getPriority() { return priority; }

public String getLabel() { return label; }

public String getDescription() { return description; }

public List<Long> getEquipmentIds() { return equipmentIds; }

public int getTotalPowerWatts() { return totalPowerWatts; }

public void setLevel(int level) { this.level = level; }

public void setPriority(PriorityLevel priority) { this.priority = priority; }

public void setLabel(String label) { this.label = label; }

public void setDescription(String description) { this.description = description; }

public void setEquipmentIds(List<Long> ids) { this.equipmentIds = ids; }

public void setTotalPowerWatts(int watts) { this.totalPowerWatts = watts; }

}
