package fr.renewguard.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import fr.renewguard.model.enums.EquipmentStatus;

import fr.renewguard.model.enums.PriorityLevel;

public class EquipmentDto {

@JsonProperty("id") private long id;

@JsonProperty("name") private String name;

@JsonProperty("location") private String location;

@JsonProperty("icon") private String icon;

@JsonProperty("priority") private PriorityLevel priority;

@JsonProperty("status") private EquipmentStatus status;

@JsonProperty("power_watts") private int powerWatts;

@JsonProperty("last_activity") private String lastActivity;

@JsonProperty("ai_managed") private boolean aiManaged;

public long getId() { return id; }

public String getName() { return name; }

public String getLocation() { return location; }

public String getIcon() { return icon; }

public PriorityLevel getPriority() { return priority; }

public EquipmentStatus getStatus() { return status; }

public int getPowerWatts() { return powerWatts; }

public String getLastActivity() { return lastActivity; }

public boolean isAiManaged() { return aiManaged; }

public void setId(long id) { this.id = id; }

public void setName(String name) { this.name = name; }

public void setLocation(String location) { this.location = location; }

public void setIcon(String icon) { this.icon = icon; }

public void setPriority(PriorityLevel priority) { this.priority = priority; }

public void setStatus(EquipmentStatus status) { this.status = status; }

public void setPowerWatts(int watts) { this.powerWatts = watts; }

public void setLastActivity(String v) { this.lastActivity = v; }

public void setAiManaged(boolean v) { this.aiManaged = v; }

}
