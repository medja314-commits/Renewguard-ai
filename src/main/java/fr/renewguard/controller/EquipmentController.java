package fr.renewguard.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class EquipmentController implements Initializable {

    @FXML private TableView equipmentTable;
    @FXML private TextField searchField;
    @FXML private Button addEquipmentBtn;
    @FXML private Button exportBtn;
    @FXML private Label equipmentCountLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize equipment controller
    }

    @FXML
    private void onAddEquipment() {
        // TODO: Show add equipment dialog
    }

    @FXML
    private void onFilter() {
        // TODO: Implement filter
    }

    @FXML
    private void onExport() {
        // TODO: Implement export
    }
}
