package fr.renewguard.model.dto;
 
import com.fasterxml.jackson.annotation.JsonProperty;
 
public class EnergySnapshotDto {
    @JsonProperty("solar_production") private double solarProduction;
    @JsonProperty("consumption") private double consumption;
    @JsonProperty("battery_percent") private int batteryPercent;
    @JsonProperty("battery_eta") private String batteryEta;
    @JsonProperty("battery_charging") private boolean batteryCharging;
    @JsonProperty("battery_charge_rate_kw") private double batteryChargeRateKw;
    @JsonProperty("grid_available") private boolean gridAvailable;
    @JsonProperty("grid_voltage") private double gridVoltage;
    @JsonProperty("grid_frequency") private double gridFrequency;
    @JsonProperty("grid_import_kw") private double gridImport;
    @JsonProperty("ai_score") private int aiScore;
    @JsonProperty("active_equipment_count") private int activeEquipmentCount;
    @JsonProperty("total_equipment_count") private int totalEquipmentCount;
    @JsonProperty("energy_mix") private EnergyMixDto energyMix;
    @JsonProperty("energy_saved_kwh") private double energySaved;
    @JsonProperty("co2_avoided_kg") private double co2Avoided;
    @JsonProperty("last_ai_decision") private AiDecisionDto lastAiDecision;
 
    public double getSolarProduction() { return solarProduction; }
    public double getConsumption() { return consumption; }
    public int getBatteryPercent() { return batteryPercent; }
    public String getBatteryEta() { return batteryEta; }
    public boolean isBatteryCharging() { return batteryCharging; }
    public double getBatteryChargeRateKw() { return batteryChargeRateKw; }
    public boolean isGridAvailable() { return gridAvailable; }
    public double getGridVoltage() { return gridVoltage; }
    public double getGridFrequency() { return gridFrequency; }
    public double getGridImport() { return gridImport; }
    public int getAiScore() { return aiScore; }
    public int getActiveEquipmentCount() { return activeEquipmentCount; }
    public int getTotalEquipmentCount() { return totalEquipmentCount; }
    public EnergyMixDto getEnergyMix() { return energyMix; }
    public double getEnergySaved() { return energySaved; }
    public double getCo2Avoided() { return co2Avoided; }
    public AiDecisionDto getLastAiDecision() { return lastAiDecision; }
 
    public static class EnergyMixDto {
        @JsonProperty("solar_percent") private double solarPercent;
        @JsonProperty("battery_percent") private double batteryPercent;
        @JsonProperty("grid_percent") private double gridPercent;
        public double getSolarPercent() { return solarPercent; }
        public double getBatteryPercent() { return batteryPercent; }
        public double getGridPercent() { return gridPercent; }
    }
}