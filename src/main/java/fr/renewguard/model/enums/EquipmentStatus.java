package fr.renewguard.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EquipmentStatus {

@JsonProperty("ON") ON,

@JsonProperty("OFF") OFF,

@JsonProperty("AI_OFF") AI_OFF,

@JsonProperty("OFFLINE") OFFLINE;

public boolean isOperational() { return this == ON; }

public String displayLabel() {

return switch (this) {

case ON -> "Actif";

case OFF -> "Inactif";

case AI_OFF -> "Off (IA)";

case OFFLINE -> "Hors ligne";

};

}

public String cssClass() {

return switch (this) {

case ON -> "status-on";

case OFF -> "status-off";

case AI_OFF -> "status-ai-off";

case OFFLINE -> "status-offline";

};

}

public String dotColor() {

return switch (this) {

case ON -> "#22D3A5";

case OFF -> "#8B93A7";

case AI_OFF -> "#3B9DFF";

case OFFLINE -> "#FF4D4D";

};

}

}
