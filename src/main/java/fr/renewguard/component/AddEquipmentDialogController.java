package fr.renewguard.component;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class AddEquipmentDialogController {

    @FXML private TextField nameField;
    @FXML private Label nameFieldError;
    @FXML private Button cancelBtn;
    @FXML private Button createBtn;

    @FXML
    private void onCancel() {
        // TODO: Close dialog
    }

    @FXML
    private void onCreate() {
        // TODO: Create equipment and close dialog
    }
}
