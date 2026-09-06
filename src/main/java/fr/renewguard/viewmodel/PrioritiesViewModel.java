package fr.renewguard.viewmodel;

import fr.renewguard.model.dto.EquipmentDto;

import fr.renewguard.model.dto.PriorityLevelDto;

import fr.renewguard.model.dto.RuleDto;

import fr.renewguard.model.enums.PriorityLevel;

import fr.renewguard.service.EquipmentService;

import fr.renewguard.service.PriorityService;

import javafx.application.Platform;

import javafx.beans.property.*;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;

import java.util.ArrayList;

import java.util.List;

public class PrioritiesViewModel {

private record ValidationResult(boolean blocked, boolean hasWarning, String message) {}

private final PriorityService priorityService = PriorityService.getInstance();

private final EquipmentService equipmentService = EquipmentService.getInstance();

private final ObservableList<EquipmentDto> level1 = FXCollections.observableArrayList();

private final ObservableList<EquipmentDto> level2 = FXCollections.observableArrayList();

private final ObservableList<EquipmentDto> level3 = FXCollections.observableArrayList();

private final ObservableList<RuleDto> rules = FXCollections.observableArrayList();

private final BooleanProperty modified = new SimpleBooleanProperty(false);

private final BooleanProperty loading = new SimpleBooleanProperty(false);

private final BooleanProperty saved = new SimpleBooleanProperty(false);

private final StringProperty errorMsg = new SimpleStringProperty(null);

private final StringProperty warningMessage = new SimpleStringProperty("");

private final BooleanProperty warningVisible = new SimpleBooleanProperty(false);

public void refresh() {

loading.set(true);

equipmentService.getAll().thenAcceptAsync(items -> {

level1.clear(); level2.clear(); level3.clear();

for (EquipmentDto e : items) {

if (e.getPriority() == PriorityLevel.CRITICAL) level1.add(e);

else if (e.getPriority() == PriorityLevel.IMPORTANT) level2.add(e);

else level3.add(e);

}

loading.set(false);

}, Platform::runLater).exceptionally(ex -> { Platform.runLater(() -> { errorMsg.set(ex.getMessage()); loading.set(false); }); return null; });

priorityService.getRules().thenAcceptAsync(rules::setAll, Platform::runLater).exceptionally(ex -> null);

}

public void moveEquipment(EquipmentDto equipment, int targetLevel, int insertIndex) {

int fromLevel = detectCurrentLevel(equipment);

ValidationResult validation = validateMove(equipment, fromLevel, targetLevel);

if (validation.blocked()) {

warningMessage.set(validation.message());

warningVisible.set(true);

return;

}

removeFromAll(equipment);

PriorityLevel newPriority = switch (targetLevel) {

case 1 -> PriorityLevel.CRITICAL;

case 2 -> PriorityLevel.IMPORTANT;

default -> PriorityLevel.LOW;

};

equipment.setPriority(newPriority);

ObservableList<EquipmentDto> target = listForLevel(targetLevel);

int idx = Math.min(insertIndex, target.size());

target.add(idx, equipment);

modified.set(true); saved.set(false);

if (validation.hasWarning()) {

warningMessage.set(validation.message());

warningVisible.set(true);

}

}

private int detectCurrentLevel(EquipmentDto equipment) {

if (level1.contains(equipment)) return 1;

if (level2.contains(equipment)) return 2;

if (level3.contains(equipment)) return 3;

return -1;

}

private ValidationResult validateMove(EquipmentDto equipment, int fromLevel, int targetLevel) {

if (fromLevel == targetLevel) return new ValidationResult(false, false, "");

int level1Count = level1.size();

if (fromLevel == 1 && level1Count == 1) {

return new ValidationResult(true, false, "Le Niveau 1 (Critique) ne peut pas être vidé complètement. Au moins un équipement critique est obligatoire.");

}

int totalPower = (int) level1.stream().mapToInt(EquipmentDto::getPowerWatts).sum()

+ (int) level2.stream().mapToInt(EquipmentDto::getPowerWatts).sum()

+ (int) level3.stream().mapToInt(EquipmentDto::getPowerWatts).sum();

if (totalPower == 0) return new ValidationResult(false, false, "");

int level1PowerAfter = (int) level1.stream()

.filter(e -> e.getId() != equipment.getId())

.mapToInt(EquipmentDto::getPowerWatts).sum();

if (targetLevel == 1) {

level1PowerAfter += equipment.getPowerWatts();

double percentageLevel1 = (double) level1PowerAfter / totalPower * 100;

if (percentageLevel1 > 80) {

return new ValidationResult(false, true, "La majorité de vos équipements sont classés critiques — la priorisation perd son utilité en cas de pénurie réelle.");

}

}

if (fromLevel == 1 && targetLevel == 3) {

return new ValidationResult(false, true, "Vous retirez " + equipment.getName() + " de la protection critique directement vers non-prioritaire.");

}

return new ValidationResult(false, false, "");

}

private void removeFromAll(EquipmentDto equipment) {

level1.removeIf(e -> e.getId() == equipment.getId());

level2.removeIf(e -> e.getId() == equipment.getId());

level3.removeIf(e -> e.getId() == equipment.getId());

}

private ObservableList<EquipmentDto> listForLevel(int level) {

return switch (level) { case 1 -> level1; case 2 -> level2; default -> level3; };

}

public void save() {

List<PriorityLevelDto> payload = new ArrayList<>();

payload.add(buildLevelDto(1, PriorityLevel.CRITICAL, level1));

payload.add(buildLevelDto(2, PriorityLevel.IMPORTANT, level2));

payload.add(buildLevelDto(3, PriorityLevel.LOW, level3));

loading.set(true);

priorityService.savePriorities(payload).thenAcceptAsync(result -> {

modified.set(false); saved.set(true); loading.set(false);

}, Platform::runLater).exceptionally(ex -> { Platform.runLater(() -> { errorMsg.set("Erreur lors de la sauvegarde"); loading.set(false); }); return null; });

}

public void toggleRule(long id) {

rules.stream().filter(r -> r.getId() == id).findFirst().ifPresent(rule -> {

rule.setActive(!rule.isActive());

int idx = rules.indexOf(rule);

rules.set(idx, rule);

priorityService.toggleRule(id, rule.isActive()).exceptionally(ex -> null);

});

}

private PriorityLevelDto buildLevelDto(int level, PriorityLevel priority, List<EquipmentDto> items) {

PriorityLevelDto dto = new PriorityLevelDto();

dto.setLevel(level); dto.setPriority(priority);

dto.setEquipmentIds(items.stream().map(EquipmentDto::getId).toList());

dto.setTotalPowerWatts(items.stream().mapToInt(EquipmentDto::getPowerWatts).sum());

return dto;

}

public void dismissWarning() {

warningVisible.set(false);

}

public ObservableList<EquipmentDto> getLevel1() { return level1; }

public ObservableList<EquipmentDto> getLevel2() { return level2; }

public ObservableList<EquipmentDto> getLevel3() { return level3; }

public ObservableList<RuleDto> getRules() { return rules; }

public BooleanProperty modifiedProperty() { return modified; }

public BooleanProperty savedProperty() { return saved; }

public StringProperty warningMessageProperty() { return warningMessage; }

public BooleanProperty warningVisibleProperty() { return warningVisible; }

}
