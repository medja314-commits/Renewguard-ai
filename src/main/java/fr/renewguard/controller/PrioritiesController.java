package fr.renewguard.controller;

import fr.renewguard.component.PriorityKanbanCardController;

import fr.renewguard.model.dto.EquipmentDto;

import fr.renewguard.model.dto.RuleDto;

import fr.renewguard.util.FxmlLoader;

import fr.renewguard.viewmodel.PrioritiesViewModel;

import javafx.collections.ListChangeListener;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.Node;

import javafx.scene.control.Button;

import javafx.scene.control.Label;

import javafx.scene.control.ToggleButton;

import javafx.scene.input.DragEvent;

import javafx.scene.input.TransferMode;

import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;

import java.net.URL;

import java.util.ResourceBundle;

public class PrioritiesController implements Initializable {

@FXML private HBox modifiedBanner;

@FXML private HBox savedBanner;

@FXML private VBox col1Cards;

@FXML private VBox col2Cards;

@FXML private VBox col3Cards;

@FXML private Label col1Stats;

@FXML private Label col2Stats;

@FXML private Label col3Stats;

@FXML private VBox dropZone1;

@FXML private VBox dropZone2;

@FXML private VBox dropZone3;

@FXML private VBox rulesContainer;

@FXML private VBox rulesList;

private final PrioritiesViewModel vm = new PrioritiesViewModel();

private EquipmentDto dragging;

@Override

public void initialize(URL url, ResourceBundle rb) {

modifiedBanner.visibleProperty().bind(vm.modifiedProperty());

modifiedBanner.managedProperty().bind(vm.modifiedProperty());

savedBanner.visibleProperty().bind(vm.savedProperty());

savedBanner.managedProperty().bind(vm.savedProperty());

vm.getLevel1().addListener((ListChangeListener<EquipmentDto>) c -> renderColumn(1));

vm.getLevel2().addListener((ListChangeListener<EquipmentDto>) c -> renderColumn(2));

vm.getLevel3().addListener((ListChangeListener<EquipmentDto>) c -> renderColumn(3));

vm.getRules().addListener((ListChangeListener<RuleDto>) c -> renderRules());

setupDropZone(dropZone1, 1);

setupDropZone(dropZone2, 2);

setupDropZone(dropZone3, 3);

vm.refresh();

}

private void renderColumn(int level) {

VBox target = level == 1 ? col1Cards : level == 2 ? col2Cards : col3Cards;

Label stats = level == 1 ? col1Stats : level == 2 ? col2Stats : col3Stats;

var items = level == 1 ? vm.getLevel1() : level == 2 ? vm.getLevel2() : vm.getLevel3();

target.getChildren().clear();

for (EquipmentDto eq : items) {

FxmlLoader.Result<PriorityKanbanCardController> result =

FxmlLoader.loadWithResult("/fr/renewguard/fxml/components/PriorityKanbanCard.fxml");

result.controller().bind(eq, this::startDrag);

target.getChildren().add(result.root());

}

int totalW = items.stream().mapToInt(EquipmentDto::getPowerWatts).sum();

stats.setText(totalW + " W total - " + items.size() + " equipements");

}

private void renderRules() {

rulesList.getChildren().clear();

for (RuleDto rule : vm.getRules()) {

HBox row = new HBox(12);

row.getStyleClass().add("rule-row");

row.setStyle("-fx-padding: 10 14 10 14;");

VBox text = new VBox(3);

text.getChildren().addAll(

new Label("Si " + rule.getCondition()),

new Label("-> " + rule.getAction()));

ToggleButton toggle = new ToggleButton(rule.isActive() ? "ON" : "OFF");

toggle.setSelected(rule.isActive());

toggle.setOnAction(e -> vm.toggleRule(rule.getId()));

row.getChildren().addAll(text, toggle);

rulesList.getChildren().add(row);

}

}

private void startDrag(EquipmentDto equipment) { dragging = equipment; }

private void setupDropZone(VBox zone, int level) {

zone.setOnDragOver(e -> {

if (dragging != null && e.getGestureSource() != zone) e.acceptTransferModes(TransferMode.MOVE);

e.consume();

});

zone.setOnDragDropped((DragEvent e) -> {

if (dragging != null) {

int insertIdx = computeInsertIndex(zone, e.getY());

vm.moveEquipment(dragging, level, insertIdx);

dragging = null;

e.setDropCompleted(true);

}

e.consume();

});

}

private int computeInsertIndex(VBox zone, double dropY) {

double accumulated = 0;

int index = 0;

for (Node child : zone.getChildren()) {

if (accumulated + child.getBoundsInParent().getHeight() / 2 > dropY) break;

accumulated += child.getBoundsInParent().getHeight() + zone.getSpacing();

index++;

}

return index;

}

@FXML private void onSave() { vm.save(); }

@FXML private void onAddRule() { /* dialog d'ajout de regle, a implementer */ }

@FXML

private void onToggleRulesPanel() {

boolean visible = !rulesContainer.isVisible();

rulesContainer.setVisible(visible);

rulesContainer.setManaged(visible);

}

}
