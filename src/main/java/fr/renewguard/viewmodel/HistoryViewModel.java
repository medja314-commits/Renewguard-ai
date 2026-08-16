package fr.renewguard.viewmodel;

import fr.renewguard.model.dto.AlertDto;

import fr.renewguard.model.dto.HistoryPointDto;

import fr.renewguard.model.dto.HistorySummaryDto;

import fr.renewguard.service.HistoryService;

import javafx.application.Platform;

import javafx.beans.property.*;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;

public class HistoryViewModel {

private final HistoryService service = HistoryService.getInstance();

private final ObservableList<HistoryPointDto> productionData = FXCollections.observableArrayList();

private final ObservableList<HistoryPointDto> consumptionData = FXCollections.observableArrayList();

private final ObservableList<HistoryPointDto> savingsData = FXCollections.observableArrayList();

private final ObservableList<HistoryPointDto> co2Data = FXCollections.observableArrayList();

private final ObjectProperty<HistorySummaryDto> summary = new SimpleObjectProperty<>(null);

private final ObservableList<AlertDto> alerts = FXCollections.observableArrayList();

private final StringProperty selectedPeriod = new SimpleStringProperty("week");

public HistoryViewModel() { selectedPeriod.addListener((o, old, val) -> refresh()); }

public void refresh() {

String period = selectedPeriod.get();

service.getHistory(period).thenAcceptAsync(points -> {

productionData.setAll(points); consumptionData.setAll(points);

savingsData.setAll(points); co2Data.setAll(points);

}, Platform::runLater).exceptionally(ex -> null);

service.getSummary(period).thenAcceptAsync(summary::set, Platform::runLater).exceptionally(ex -> null);

service.getAlerts().thenAcceptAsync(alerts::setAll, Platform::runLater).exceptionally(ex -> null);

}

public void resolveAlert(long id) {

service.resolveAlert(id).thenRunAsync(() -> alerts.removeIf(a -> a.getId() == id), Platform::runLater)

.exceptionally(ex -> null);

}

public void setPeriod(String period) { selectedPeriod.set(period); }

public void exportReport(String format) { service.exportReport(selectedPeriod.get(), format).exceptionally(ex -> null); }

public ObservableList<HistoryPointDto> getProductionData() { return productionData; }

public ObservableList<HistoryPointDto> getConsumptionData() { return consumptionData; }

public ObservableList<HistoryPointDto> getSavingsData() { return savingsData; }

public ObservableList<HistoryPointDto> getCo2Data() { return co2Data; }

public ObservableList<AlertDto> getAlerts() { return alerts; }

public ObjectProperty<HistorySummaryDto> summaryProperty() { return summary; }

}
