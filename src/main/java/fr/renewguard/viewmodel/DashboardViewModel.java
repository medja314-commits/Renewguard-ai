package fr.renewguard.viewmodel;

import fr.renewguard.model.dto.AiDecisionDto;

import fr.renewguard.model.dto.EnergySnapshotDto;

import fr.renewguard.model.dto.HistoryPointDto;

import fr.renewguard.service.AiService;

import fr.renewguard.service.EnergyService;

import fr.renewguard.util.PollingScheduler;

import javafx.application.Platform;

import javafx.beans.property.*;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;

import java.util.List;

public class DashboardViewModel {

// ── Services ─────────────────────────────────────────────────────

private final EnergyService energyService;

private final AiService aiService;

// ── Snapshot properties ──────────────────────────────────────────

private final DoubleProperty solarProduction = new SimpleDoubleProperty(0);

private final DoubleProperty consumption = new SimpleDoubleProperty(0);

private final IntegerProperty aiScore = new SimpleIntegerProperty(0);

private final StringProperty equipmentCount = new SimpleStringProperty("---");

private final IntegerProperty batteryPercent = new SimpleIntegerProperty(0);

private final StringProperty batteryEta = new SimpleStringProperty("---");

private final BooleanProperty batteryCharging = new SimpleBooleanProperty(false);

private final DoubleProperty batteryChargeRate = new SimpleDoubleProperty(0);

private final BooleanProperty gridAvailable = new SimpleBooleanProperty(true);

private final DoubleProperty gridVoltage = new SimpleDoubleProperty(0);

private final DoubleProperty gridFrequency = new SimpleDoubleProperty(0);

private final DoubleProperty gridImport = new SimpleDoubleProperty(0);

private final DoubleProperty energySaved = new SimpleDoubleProperty(0);

private final DoubleProperty co2Avoided = new SimpleDoubleProperty(0);

private final DoubleProperty solarMix = new SimpleDoubleProperty(0);

private final DoubleProperty batteryMix = new SimpleDoubleProperty(0);

private final DoubleProperty gridMix = new SimpleDoubleProperty(0);

// ── AI decision ──────────────────────────────────────────────────

private final ObjectProperty<AiDecisionDto> lastDecision =

new SimpleObjectProperty<>(null);

// ── Chart data ───────────────────────────────────────────────────

private final ObservableList<HistoryPointDto> chartData =

FXCollections.observableArrayList();

private final StringProperty selectedPeriod = new SimpleStringProperty("day");

// ── System view ──────────────────────────────────────────────────

private final BooleanProperty systemViewVisible = new SimpleBooleanProperty(false);

// ── State ────────────────────────────────────────────────────────

private final BooleanProperty loading = new SimpleBooleanProperty(false);

private final StringProperty error = new SimpleStringProperty(null);

private PollingScheduler.Handle snapshotHandle;

private PollingScheduler.Handle aiHandle;

// ── Constructors ─────────────────────────────────────────────────

DashboardViewModel(EnergyService energyService, AiService aiService) {

this.energyService = energyService;

this.aiService = aiService;

}

public DashboardViewModel() {

this(EnergyService.getInstance(), AiService.getInstance());

}

// ── Init / dispose ───────────────────────────────────────────────

public void startPolling() {

refreshSnapshot();

refreshChart();

refreshLastDecision();

snapshotHandle = PollingScheduler.getInstance()

.schedule(this::refreshSnapshot, 30);

aiHandle = PollingScheduler.getInstance()

.schedule(this::refreshLastDecision, 60);

}

public void stopPolling() {

if (snapshotHandle != null) snapshotHandle.cancel();

if (aiHandle != null) aiHandle.cancel();

}

// ── Refresh methods ──────────────────────────────────────────────

public void refreshSnapshot() {

energyService.getSnapshot()

.thenAcceptAsync(this::applySnapshot, Platform::runLater)

.exceptionally(ex -> {

Platform.runLater(() -> error.set(ex.getMessage()));

return null;

});

}

public void refreshChart() {

energyService.getHistory(selectedPeriod.get())

.thenAcceptAsync(points -> {

chartData.setAll(points);

}, Platform::runLater)

.exceptionally(ex -> {

Platform.runLater(() -> error.set(ex.getMessage()));

return null;

});

}

public void refreshLastDecision() {

aiService.getDecisions(1)

.thenAcceptAsync(list -> {

if (list != null && !list.isEmpty()) lastDecision.set(list.get(0));

}, Platform::runLater)

.exceptionally(ex -> null);

}

public void changePeriod(String period) {

selectedPeriod.set(period);

refreshChart();

}

public void toggleSystemView() {

systemViewVisible.set(!systemViewVisible.get());

}

// ── Private applier ──────────────────────────────────────────────

private void applySnapshot(EnergySnapshotDto s) {

solarProduction.set(s.getSolarProduction());

consumption.set(s.getConsumption());

aiScore.set(s.getAiScore());

equipmentCount.set(s.getActiveEquipmentCount() + " / " + s.getTotalEquipmentCount());

batteryPercent.set(s.getBatteryPercent());

batteryEta.set(s.getBatteryEta() != null ? s.getBatteryEta() : "---");

batteryCharging.set(s.isBatteryCharging());

batteryChargeRate.set(s.getBatteryChargeRateKw());

gridAvailable.set(s.isGridAvailable());

gridVoltage.set(s.getGridVoltage());

gridFrequency.set(s.getGridFrequency());

gridImport.set(s.getGridImport());

energySaved.set(s.getEnergySaved());

co2Avoided.set(s.getCo2Avoided());

if (s.getEnergyMix() != null) {

solarMix.set(s.getEnergyMix().getSolarPercent());

batteryMix.set(s.getEnergyMix().getBatteryPercent());

gridMix.set(s.getEnergyMix().getGridPercent());

}

if (s.getLastAiDecision() != null) lastDecision.set(s.getLastAiDecision());

}

// ── Property accessors ───────────────────────────────────────────

public DoubleProperty solarProductionProperty() { return solarProduction; }

public DoubleProperty consumptionProperty() { return consumption; }

public IntegerProperty aiScoreProperty() { return aiScore; }

public StringProperty equipmentCountProperty() { return equipmentCount; }

public IntegerProperty batteryPercentProperty() { return batteryPercent; }

public StringProperty batteryEtaProperty() { return batteryEta; }

public BooleanProperty batteryChargingProperty() { return batteryCharging; }

public DoubleProperty batteryChargeRateProperty() { return batteryChargeRate; }

public BooleanProperty gridAvailableProperty() { return gridAvailable; }

public DoubleProperty gridVoltageProperty() { return gridVoltage; }

public DoubleProperty gridFrequencyProperty() { return gridFrequency; }

public DoubleProperty gridImportProperty() { return gridImport; }

public DoubleProperty energySavedProperty() { return energySaved; }

public DoubleProperty co2AvoidedProperty() { return co2Avoided; }

public DoubleProperty solarMixProperty() { return solarMix; }

public DoubleProperty batteryMixProperty() { return batteryMix; }

public DoubleProperty gridMixProperty() { return gridMix; }

public ObjectProperty<AiDecisionDto> lastDecisionProperty() { return lastDecision; }

public ObservableList<HistoryPointDto> getChartData() { return chartData; }

public StringProperty selectedPeriodProperty() { return selectedPeriod; }

public BooleanProperty systemViewVisibleProperty() { return systemViewVisible; }

public BooleanProperty loadingProperty() { return loading; }

public StringProperty errorProperty() { return error; }

public double getSolarProduction() { return solarProduction.get(); }

public double getConsumption() { return consumption.get(); }

public int getAiScore() { return aiScore.get(); }

public int getBatteryPercent() { return batteryPercent.get(); }

public boolean isGridAvailable() { return gridAvailable.get(); }

public double getSolarMix() { return solarMix.get(); }

public double getBatteryMix() { return batteryMix.get(); }

public double getGridMix() { return gridMix.get(); }

}
