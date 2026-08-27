package fr.renewguard.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HistoryPointDto {

@JsonProperty("label") private String label;

@JsonProperty("production_kwh") private double production;

@JsonProperty("consumption_kwh") private double consumption;

@JsonProperty("saved_kwh") private double saved;

@JsonProperty("co2_kg") private double co2;

@JsonProperty("grid_import_kwh") private double gridImport;

@JsonProperty("grid_export_kwh") private double gridExport;

public String getLabel() { return label; }

public double getProduction() { return production; }

public double getConsumption() { return consumption; }

public double getSaved() { return saved; }

public double getCo2() { return co2; }

public double getGridImport() { return gridImport; }

public double getGridExport() { return gridExport; }

}
