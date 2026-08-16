package fr.renewguard.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AlertSeverity {

@JsonProperty("CRITICAL") CRITICAL,

@JsonProperty("WARNING") WARNING,

@JsonProperty("INFO") INFO;

public String displayLabel() {

return switch (this) {

case CRITICAL -> "Critique";

case WARNING -> "Attention";

case INFO -> "Information";

};

}

public String dotColor() {

return switch (this) {

case CRITICAL -> "#FF4D4D";

case WARNING -> "#FFA53E";

case INFO -> "#3B9DFF";

};

}

public String cssClass() {

return switch (this) {

case CRITICAL -> "alert-critical";

case WARNING -> "alert-warning";

case INFO -> "alert-info";

};

}

}
