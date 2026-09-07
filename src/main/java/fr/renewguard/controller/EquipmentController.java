package fr.renewguard.controller;

import fr.renewguard.component.AddEquipmentDialogController;
import fr.renewguard.component.EquipmentRowController;
import fr.renewguard.component.ExportDialogController;
import fr.renewguard.model.dto.EquipmentDto;
import fr.renewguard.model.enums.EquipmentStatus;
import fr.renewguard.model.enums.PriorityLevel;
import fr.renewguard.util.DialogHost;
import fr.renewguard.util.FxmlLoader;
import fr.renewguard.util.NumberFormatter;
import fr.renewguard.viewmodel.EquipmentViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EquipmentController implements Initializable {

    @FXML private Label statsLabel;
    @FXML private TextField searchField;
    @FXML private Button addBtn;
    @FXML private Button chipAll;
    @FXML private Button chipCritical;
    @FXML private Button chipImportant;
    @FXML private Button chipLow;
    @FXML private Button chipActive;
    @FXML private Button chipInactive;
    @FXML private Button demoSectorBtn;
    @FXML private TableView<EquipmentDto> table;
    @FXML private TableColumn<EquipmentDto, String> colName;
    @FXML private TableColumn<EquipmentDto, String> colLocation;
    @FXML private TableColumn<EquipmentDto, PriorityLevel> colPriority;
    @FXML private TableColumn<EquipmentDto, Integer> colPower;
    @FXML private TableColumn<EquipmentDto, EquipmentStatus> colStatus;
    @FXML private TableColumn<EquipmentDto, String> colActivity;
    @FXML private TableColumn<EquipmentDto, Void> colToggle;
    @FXML private Button addEquipmentBtn;
    @FXML private Button exportBtn;
    @FXML private Label equipmentCountLabel;
    @FXML private StackPane dialogOverlay;

    private final EquipmentViewModel vm = new EquipmentViewModel();
    private List<Button> allChips;
    private DialogHost dialogHost;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        allChips = List.of(chipAll, chipCritical, chipImportant, chipLow, chipActive, chipInactive);
        searchField.textProperty().bindBidirectional(vm.searchQueryProperty());
        configureColumns();
        table.setItems(vm.getFilteredEquipments());
        table.setPlaceholder(new Label("Aucun equipement trouve."));
        vm.totalActiveProperty().addListener((o, old, val) -> refreshStats());
        vm.totalPowerWProperty().addListener((o, old, val) -> refreshStats());
        vm.sectorCutoffProperty().addListener((o, old, val) -> updateSectorButtonText());
        setActiveChip(chipAll);
        dialogHost = new DialogHost(dialogOverlay);
        vm.refresh();
        refreshStats();
        updateSectorButtonText();
    }

    private void refreshStats() {
        statsLabel.setText(vm.getTotalCount() + " equipements - " + vm.getTotalActive()
            + " actifs - " + NumberFormatter.formatWatts(vm.getTotalPowerW()) + " consommes");
        if (equipmentCountLabel != null) {
            equipmentCountLabel.setText(String.valueOf(vm.getTotalCount()));
        }
    }

    private void configureColumns() {
        colName.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getName()));
        colLocation.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getLocation()));

        colPriority.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getPriority()));
        colPriority.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(PriorityLevel item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); return; }
                Label badge = new Label(item.displayLabel());
                badge.getStyleClass().add(item.badgeCssClass());
                setGraphic(badge);
            }
        });

        colPower.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getPowerWatts()));
        colPower.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : NumberFormatter.formatWatts(item));
            }
        });

        colStatus.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getStatus()));
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(EquipmentStatus item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.displayLabel());
            }
        });

        colActivity.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getLastActivity()));

        colToggle.setCellFactory(col -> new TableCell<>() {
            private final FxmlLoader.Result<EquipmentRowController> result =
                FxmlLoader.loadWithResult("/fr/renewguard/fxml/components/EquipmentRow.fxml");
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) { setGraphic(null); return; }
                EquipmentDto eq = getTableRow().getItem();
                result.controller().bind(eq, id -> { vm.toggleStatus(id); table.refresh(); });
                setGraphic(result.root());
            }
        });
    }

    @FXML private void onChipAll() { applyChip(chipAll, "ALL"); }
    @FXML private void onChipCritical() { applyChip(chipCritical, "CRITICAL"); }
    @FXML private void onChipImportant() { applyChip(chipImportant, "IMPORTANT"); }
    @FXML private void onChipLow() { applyChip(chipLow, "LOW"); }
    @FXML private void onChipActive() { applyChip(chipActive, "ACTIVE"); }
    @FXML private void onChipInactive() { applyChip(chipInactive, "INACTIVE"); }

    private void applyChip(Button chip, String filter) {
        vm.setFilter(filter);
        setActiveChip(chip);
    }

    private void setActiveChip(Button active) {
        for (Button b : allChips) {
            b.getStyleClass().removeAll("chip-active");
            b.getStyleClass().add("chip");
        }
        active.getStyleClass().remove("chip");
        active.getStyleClass().add("chip-active");
    }

    @FXML
    private void onAddEquipment() {
        FxmlLoader.Result<AddEquipmentDialogController> result =
            FxmlLoader.loadWithResult("/fr/renewguard/fxml/components/AddEquipmentDialog.fxml");
        AddEquipmentDialogController controller = result.controller();
        controller.setDialogHost(dialogHost);
        controller.setViewModel(vm);
        dialogHost.show(result.root());
    }

    @FXML
    private void onFilter(javafx.event.ActionEvent event) {
        if (event.getSource() instanceof Button button) {
            String filterValue = (String) button.getUserData();
            if (filterValue != null && !filterValue.isEmpty()) {
                String filterType = filterValue.toUpperCase();
                vm.setFilter(filterType);
                setActiveChip(button);
            }
        }
    }

    @FXML
    private void onExport() {
        FxmlLoader.Result<ExportDialogController> result =
            FxmlLoader.loadWithResult("/fr/renewguard/fxml/components/ExportDialog.fxml");
        ExportDialogController controller = result.controller();
        controller.setDialogHost(dialogHost);
        dialogHost.show(result.root());
    }

    @FXML private void onAdd() { /* dialog d'ajout, a implementer */ }
    @FXML private void onRefresh() { vm.refresh(); }
    @FXML private void onToggleSectorCutoff() { vm.toggleSectorCutoff(); }

    private void updateSectorButtonText() {
        if (demoSectorBtn != null) {
            demoSectorBtn.setText(vm.isSectorCutoff() ? "Rétablir le secteur" : "Simuler coupure réseau");
        }
    }
}
