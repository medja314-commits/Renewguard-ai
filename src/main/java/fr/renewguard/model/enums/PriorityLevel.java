package fr.renewguard.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PriorityLevel {

@JsonProperty("CRITICAL") CRITICAL(1),

@JsonProperty("IMPORTANT") IMPORTANT(2),

@JsonProperty("LOW") LOW(3);

private final int rank;

PriorityLevel(int rank) { this.rank = rank; }

public int getRank() { return rank; }

public String displayLabel() {

return switch (this) {

case CRITICAL -> "Critique";

case IMPORTANT -> "Important";

case LOW -> "Non prioritaire";

};

}

public String badgeCssClass() {

return switch (this) {

case CRITICAL -> "priority-critical";

case IMPORTANT -> "priority-important";

case LOW -> "priority-low";

};

}

public String accentColor() {

return switch (this) {

case CRITICAL -> "#FF4D4D";

case IMPORTANT -> "#FFA53E";

case LOW -> "#8B93A7";

};

}

public static PriorityLevel fromRank(int rank) {

for (PriorityLevel p : values()) if (p.rank == rank) return p;

throw new IllegalArgumentException("No PriorityLevel for rank: " + rank);

}

}
