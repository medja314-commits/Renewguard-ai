package fr.renewguard.model.dto;
 
import com.fasterxml.jackson.annotation.JsonProperty;
 
public class HistorySummaryDto {
    @JsonProperty("total_saved_kwh") private double totalSavedKwh;
    @JsonProperty("total_co2_kg") private double totalCo2Kg;
    @JsonProperty("financial_savings_eur") private double financialSavingsEur;
    @JsonProperty("solar_coverage_percent") private double solarCoveragePercent;
    @JsonProperty("grid_export_eur") private double gridExportEur;
    @JsonProperty("grid_import_cost_eur") private double gridImportCostEur;
    @JsonProperty("net_benefit_eur") private double netBenefitEur;
    @JsonProperty("monthly_projection_eur") private double monthlyProjectionEur;
 
    public double getTotalSavedKwh() { return totalSavedKwh; }
    public double getTotalCo2Kg() { return totalCo2Kg; }
    public double getFinancialSavingsEur() { return financialSavingsEur; }
    public double getSolarCoveragePercent() { return solarCoveragePercent; }
    public double getGridExportEur() { return gridExportEur; }
    public double getGridImportCostEur() { return gridImportCostEur; }
    public double getNetBenefitEur() { return netBenefitEur; }
    public double getMonthlyProjectionEur() { return monthlyProjectionEur; }
}