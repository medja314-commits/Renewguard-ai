package fr.renewguard.controller;

import fr.renewguard.component.AlertItemController;

import fr.renewguard.model.dto.AlertDto;

import fr.renewguard.model.dto.HistoryPointDto;

import fr.renewguard.util.FxmlLoader;

import fr.renewguard.util.NumberFormatter;

import fr.renewguard.viewmodel.HistoryViewModel;

import javafx.collections.ListChangeListener;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.chart.*;

import javafx.scene.control.Button;

import javafx.scene.control.Label;

import javafx.scene.layout.VBox;

import java.net.URL;

import java.util.ResourceBundle;

public class HistoryController implements Initializable {

@FXML private Button chipDay;

@FXML private Button chipWeek;

@FXML private Button chipMonth;

@FXML private Button chipYear;

@FXML private Button exportCsvBtn;

@FXML private Button exportPdfBtn;

@FXML private Label kpiSaved;

@FXML private Label kpiCo2;

@FXML private Label kpiSolar;

@FXML private Label kpiNet;

@FXML private AreaChart<String, Number> productionChart;

@FXML private AreaChart<String, Number> consumptionChart;

@FXML private BarChart<String, Number> savingsChart;

@FXML private AreaChart<String, Number> co2Chart;

@FXML private VBox alertsList;

@FXML private Label alertsCountLabel;

private final HistoryViewModel vm = new HistoryViewModel();

private java.util.List<Button> allChips;

@Override

public void initialize(URL url, ResourceBundle rb) {

allChips = java.util.List.of(chipDay, chipWeek, chipMonth, chipYear);

vm.getProductionData().addListener((ListChangeListener<HistoryPointDto>) c -> refreshProduction());

vm.getConsumptionData().addListener((ListChangeListener<HistoryPointDto>) c -> refreshConsumption());

vm.getSavingsData().addListener((ListChangeListener<HistoryPointDto>) c -> refreshSavings());

vm.getCo2Data().addListener((ListChangeListener<HistoryPointDto>) c -> refreshCo2());

vm.getAlerts().addListener((ListChangeListener<AlertDto>) c -> renderAlerts());

vm.summaryProperty().addListener((o, old, s) -> {

if (s == null) return;

kpiSaved.setText(NumberFormatter.formatKwh(s.getTotalSavedKwh()));

kpiCo2.setText(NumberFormatter.formatCo2Kg(s.getTotalCo2Kg()));

kpiSolar.setText(NumberFormatter.formatPercentDouble(s.getSolarCoveragePercent()));

kpiNet.setText(NumberFormatter.formatCurrencyRounded(s.getNetBenefitEur()));

});

setActiveChip(chipWeek);

vm.refresh();

}

private void refreshProduction() {

XYChart.Series<String, Number> s = new XYChart.Series<>();

for (HistoryPointDto p : vm.getProductionData()) s.getData().add(new XYChart.Data<>(p.getLabel(), p.getProduction()));

productionChart.getData().setAll(s);

}

private void refreshConsumption() {

XYChart.Series<String, Number> prod = new XYChart.Series<>();

XYChart.Series<String, Number> cons = new XYChart.Series<>();

for (HistoryPointDto p : vm.getConsumptionData()) {

prod.getData().add(new XYChart.Data<>(p.getLabel(), p.getProduction()));

cons.getData().add(new XYChart.Data<>(p.getLabel(), p.getConsumption()));

}

consumptionChart.getData().setAll(prod, cons);

}

private void refreshSavings() {

XYChart.Series<String, Number> s = new XYChart.Series<>();

for (HistoryPointDto p : vm.getSavingsData()) s.getData().add(new XYChart.Data<>(p.getLabel(), p.getSaved()));

savingsChart.getData().setAll(s);

}

private void refreshCo2() {

XYChart.Series<String, Number> s = new XYChart.Series<>();

for (HistoryPointDto p : vm.getCo2Data()) s.getData().add(new XYChart.Data<>(p.getLabel(), p.getCo2()));

co2Chart.getData().setAll(s);

}

private void renderAlerts() {

alertsList.getChildren().clear();

alertsCountLabel.setText(vm.getAlerts().size() + " alertes");

for (AlertDto alert : vm.getAlerts()) {

FxmlLoader.Result<AlertItemController> result =

FxmlLoader.loadWithResult("/fr/renewguard/fxml/components/AlertItem.fxml");

result.controller().bind(alert, id -> { vm.resolveAlert(id); renderAlerts(); });

alertsList.getChildren().add(result.root());

}

}

@FXML private void onChipDay() { applyChip(chipDay, "day"); }

@FXML private void onChipWeek() { applyChip(chipWeek, "week"); }

@FXML private void onChipMonth() { applyChip(chipMonth, "month"); }

@FXML private void onChipYear() { applyChip(chipYear, "year"); }

private void applyChip(Button chip, String period) { vm.setPeriod(period); setActiveChip(chip); }

private void setActiveChip(Button active) {

for (Button b : allChips) { b.getStyleClass().removeAll("chip-active"); b.getStyleClass().add("chip"); }

active.getStyleClass().remove("chip");

active.getStyleClass().add("chip-active");

}

@FXML private void onExportCsv() { vm.exportReport("csv"); }

@FXML private void onExportPdf() { vm.exportReport("pdf"); }

}
