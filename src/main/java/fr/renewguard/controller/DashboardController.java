package fr.renewguard.controller;

import fr.renewguard.component.AiDecisionCardController;

import fr.renewguard.component.BatteryGaugeController;

import fr.renewguard.component.KpiCardController;

import fr.renewguard.model.dto.HistoryPointDto;

import fr.renewguard.util.FxmlLoader;

import fr.renewguard.util.NumberFormatter;

import fr.renewguard.viewmodel.DashboardViewModel;

import javafx.collections.FXCollections;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.chart.*;

import javafx.scene.control.Button;

import javafx.scene.control.Label;

import javafx.scene.layout.*;

import javafx.scene.shape.Circle;

import java.net.URL;

import java.util.List;

import java.util.ResourceBundle;

public class DashboardController implements Initializable {

// ── Status bar ───────────────────────────────────────────────────

@FXML private Circle statusDot;

@FXML private Label statusBadge;

@FXML private Button systemViewBtn;

@FXML private VBox systemViewPanel;

// ── KPI placeholders (GridPane cells) ───────────────────────────

@FXML private StackPane kpiSolarSlot;

@FXML private StackPane kpiConsumptionSlot;

@FXML private StackPane kpiScoreSlot;

@FXML private StackPane kpiEquipmentSlot;

// ── Battery / Network / AI decision slots ────────────────────────

@FXML private StackPane batterySlot;

@FXML private StackPane aiDecisionSlot;

// ── Network card (inline, no sub-component) ──────────────────────

@FXML private Circle networkDot;

@FXML private Label networkStatusLabel;

@FXML private Label networkVoltageLabel;

@FXML private Label networkFrequencyLabel;

@FXML private Label networkImportLabel;

// ── Chart ────────────────────────────────────────────────────────

@FXML private AreaChart<String, Number> mainChart;

@FXML private Button btnDay;

@FXML private Button btnWeek;

@FXML private Button btnMonth;

// ── Donut ────────────────────────────────────────────────────────

@FXML private PieChart pieChart;

@FXML private Label energySavedLabel;

@FXML private Label co2AvoidedLabel;

// ── Sub-controllers (injected manually) ──────────────────────────

private KpiCardController kpiSolarCtrl;

private KpiCardController kpiConsumptionCtrl;

private KpiCardController kpiScoreCtrl;

private KpiCardController kpiEquipmentCtrl;

private BatteryGaugeController batteryCtrl;

private AiDecisionCardController aiDecisionCtrl;

// ── ViewModel ────────────────────────────────────────────────────

private final DashboardViewModel vm = new DashboardViewModel();

// ── Init ─────────────────────────────────────────────────────────

@Override

public void initialize(URL url, ResourceBundle rb) {

injectComponents();

bindViewModel();

configureChart();

configurePeriodButtons(btnDay);

vm.startPolling();

}

public void dispose() {

vm.stopPolling();

}

// ── Component injection ──────────────────────────────────────────

private void injectComponents() {

var solarResult = FxmlLoader.<KpiCardController>loadWithResult(

"/fr/renewguard/fxml/components/KpiCard.fxml");

kpiSolarCtrl = solarResult.controller();

kpiSolarCtrl.configure("Production solaire", "☀", "GREEN");

kpiSolarSlot.getChildren().setAll(solarResult.root());

var consoResult = FxmlLoader.<KpiCardController>loadWithResult(

"/fr/renewguard/fxml/components/KpiCard.fxml");

kpiConsumptionCtrl = consoResult.controller();

kpiConsumptionCtrl.configure("Consommation", "⚡", "BLUE");

kpiConsumptionSlot.getChildren().setAll(consoResult.root());

var scoreResult = FxmlLoader.<KpiCardController>loadWithResult(

"/fr/renewguard/fxml/components/KpiCard.fxml");

kpiScoreCtrl = scoreResult.controller();

kpiScoreCtrl.configure("Score IA", "🧠", "GREEN");

kpiScoreSlot.getChildren().setAll(scoreResult.root());

var equipResult = FxmlLoader.<KpiCardController>loadWithResult(

"/fr/renewguard/fxml/components/KpiCard.fxml");

kpiEquipmentCtrl = equipResult.controller();

kpiEquipmentCtrl.configure("Équipements actifs", "🔌", "AMBER");

kpiEquipmentSlot.getChildren().setAll(equipResult.root());

var batteryResult = FxmlLoader.<BatteryGaugeController>loadWithResult(

"/fr/renewguard/fxml/components/BatteryGauge.fxml");

batteryCtrl = batteryResult.controller();

batterySlot.getChildren().setAll(batteryResult.root());

var aiResult = FxmlLoader.<AiDecisionCardController>loadWithResult(

"/fr/renewguard/fxml/components/AiDecisionCard.fxml");

aiDecisionCtrl = aiResult.controller();

aiDecisionSlot.getChildren().setAll(aiResult.root());

}

// ── Bindings ─────────────────────────────────────────────────────

private void bindViewModel() {

vm.solarProductionProperty().addListener((o, old, val) ->

kpiSolarCtrl.setValue(

NumberFormatter.formatKw(val.doubleValue()), "kW",

"+12% vs hier", true));

vm.consumptionProperty().addListener((o, old, val) ->

kpiConsumptionCtrl.setValue(

NumberFormatter.formatKw(val.doubleValue()), "kW",

"--8% vs moy.", false));

vm.aiScoreProperty().addListener((o, old, val) ->

kpiScoreCtrl.setValue(

String.valueOf(val.intValue()), "/ 100",

"Optimisation active", null));

vm.equipmentCountProperty().addListener((o, old, val) ->

kpiEquipmentCtrl.setValue(val, "", "2 OFF par IA", null));

vm.batteryPercentProperty().addListener((o, old, val) ->

batteryCtrl.setPercent(val.intValue()));

vm.batteryEtaProperty().addListener((o, old, val) ->

batteryCtrl.setEta(val));

vm.batteryChargingProperty().addListener((o, old, val) ->

batteryCtrl.setCharging(val, vm.batteryChargeRateProperty().get()));

vm.gridAvailableProperty().addListener((o, old, available) ->

updateNetworkCard(available));

vm.gridVoltageProperty().addListener((o, old, val) ->

networkVoltageLabel.setText(NumberFormatter.formatVoltage(val.doubleValue())));

vm.gridFrequencyProperty().addListener((o, old, val) ->

networkFrequencyLabel.setText(NumberFormatter.formatHz(val.doubleValue())));

vm.gridImportProperty().addListener((o, old, val) ->

networkImportLabel.setText(NumberFormatter.formatKw(val.doubleValue()) + " kW"));

vm.lastDecisionProperty().addListener((o, old, decision) -> {

if (decision != null) aiDecisionCtrl.apply(decision);

});

vm.getChartData().addListener(

(javafx.collections.ListChangeListener<HistoryPointDto>) c ->

refreshChart(vm.getChartData())

);

vm.energySavedProperty().addListener((o, old, val) ->

energySavedLabel.setText(NumberFormatter.formatKwh(val.doubleValue())));

vm.co2AvoidedProperty().addListener((o, old, val) ->

co2AvoidedLabel.setText(NumberFormatter.formatCo2Kg(val.doubleValue())));

vm.solarMixProperty().addListener((o, old, v) -> refreshDonut());

vm.batteryMixProperty().addListener((o, old, v) -> refreshDonut());

vm.gridMixProperty().addListener((o, old, v) -> refreshDonut());

vm.systemViewVisibleProperty().addListener((o, old, visible) -> {

systemViewPanel.setVisible(visible);

systemViewPanel.setManaged(visible);

systemViewBtn.setText(visible ? "Masquer le système" : "Vue système");

});

}

// ── Network card ─────────────────────────────────────────────────

private void updateNetworkCard(boolean available) {

String color = available ? "#22D3A5" : "#FF4D4D";

networkDot.setStyle("-fx-fill: " + color + ";");

networkStatusLabel.setText(available ? "Réseau disponible" : "Coupure détectée");

networkStatusLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 15px; -fx-font-weight: 700;");

}

// ── Chart ────────────────────────────────────────────────────────

private void configureChart() {

mainChart.setAnimated(false);

mainChart.setLegendVisible(true);

mainChart.getStyleClass().add("chart-dark");

}

private void refreshChart(List<HistoryPointDto> points) {

XYChart.Series<String, Number> production = new XYChart.Series<>();

XYChart.Series<String, Number> consumption = new XYChart.Series<>();

production.setName("Production solaire");

consumption.setName("Consommation");

for (HistoryPointDto p : points) {

production.getData().add(

new XYChart.Data<>(p.getLabel(), p.getProduction()));

consumption.getData().add(

new XYChart.Data<>(p.getLabel(), p.getConsumption()));

}

mainChart.getData().clear();

mainChart.getData().addAll(production, consumption);

javafx.application.Platform.runLater(() -> {

if (production.getNode() != null)

production.getNode().setStyle(

"-fx-stroke: #22D3A5; -fx-stroke-width: 2;");

if (consumption.getNode() != null)

consumption.getNode().setStyle(

"-fx-stroke: #3B9DFF; -fx-stroke-width: 2;");

for (XYChart.Data<?, ?> d : production.getData())

if (d.getNode() != null)

d.getNode().setStyle("-fx-background-color: #22D3A5, white;");

for (XYChart.Data<?, ?> d : consumption.getData())

if (d.getNode() != null)

d.getNode().setStyle("-fx-background-color: #3B9DFF, white;");

});

}

// ── Donut ────────────────────────────────────────────────────────

private void refreshDonut() {

double solar = vm.getSolarMix();

double battery = vm.getBatteryMix();

double grid = vm.getGridMix();

pieChart.setData(FXCollections.observableArrayList(

new PieChart.Data("Solaire", solar),

new PieChart.Data("Batterie", battery),

new PieChart.Data("Réseau", grid)

));

javafx.application.Platform.runLater(() -> {

var data = pieChart.getData();

if (data.size() >= 3) {

data.get(0).getNode().setStyle("-fx-pie-color: #22D3A5;");

data.get(1).getNode().setStyle("-fx-pie-color: #3B9DFF;");

data.get(2).getNode().setStyle("-fx-pie-color: #FFA53E;");

}

});

}

// ── Period buttons ───────────────────────────────────────────────

@FXML private void onPeriodDay() { switchPeriod("day", btnDay); }

@FXML private void onPeriodWeek() { switchPeriod("week", btnWeek); }

@FXML private void onPeriodMonth() { switchPeriod("month", btnMonth); }

private void switchPeriod(String period, Button active) {

for (Button b : List.of(btnDay, btnWeek, btnMonth)) {

b.getStyleClass().removeAll("period-btn-active");

b.getStyleClass().add("period-btn");

}

active.getStyleClass().remove("period-btn");

active.getStyleClass().add("period-btn-active");

vm.changePeriod(period);

}

private void configurePeriodButtons(Button initial) {

for (Button b : List.of(btnDay, btnWeek, btnMonth))

b.getStyleClass().add("period-btn");

initial.getStyleClass().remove("period-btn");

initial.getStyleClass().add("period-btn-active");

}

// ── System view ──────────────────────────────────────────────────

@FXML

private void onSystemViewToggle() {

vm.toggleSystemView();

}

}
