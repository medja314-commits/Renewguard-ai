package fr.renewguard.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PredictionDto {

@JsonProperty("consumption_curve") private List<PredictionPoint> consumptionCurve;

@JsonProperty("production_curve") private List<PredictionPoint> productionCurve;

@JsonProperty("battery_forecast_label") private String batteryForecastLabel;

@JsonProperty("solar_forecast_label") private String solarForecastLabel;

@JsonProperty("autonomy_forecast_label") private String autonomyForecastLabel;

@JsonProperty("consumption_forecast_kw") private double consumptionForecastKw;

@JsonProperty("production_forecast_kw") private double productionForecastKw;

@JsonProperty("battery_forecast_percent") private int batteryForecastPercent;

public List<PredictionPoint> getConsumptionCurve() { return consumptionCurve; }

public List<PredictionPoint> getProductionCurve() { return productionCurve; }

public String getBatteryForecastLabel() { return batteryForecastLabel; }

public String getSolarForecastLabel() { return solarForecastLabel; }

public String getAutonomyForecastLabel() { return autonomyForecastLabel; }

public double getConsumptionForecastKw() { return consumptionForecastKw; }

public double getProductionForecastKw() { return productionForecastKw; }

public int getBatteryForecastPercent() { return batteryForecastPercent; }

public static class PredictionPoint {

@JsonProperty("label") private String label;

@JsonProperty("real") private Double real;

@JsonProperty("predicted") private Double predicted;

@JsonProperty("lower_bound") private Double lowerBound;

@JsonProperty("upper_bound") private Double upperBound;

public String getLabel() { return label; }

public Double getReal() { return real; }

public Double getPredicted() { return predicted; }

public Double getLowerBound() { return lowerBound; }

public Double getUpperBound() { return upperBound; }

public boolean hasReal() { return real != null; }

public boolean hasPredicted() { return predicted != null; }

}

}
