package fr.renewguard.component;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class AddRuleDialogController {

    @FXML private ComboBox conditionField;
    @FXML private Label conditionFieldError;
    @FXML private Button cancelBtn;
    @FXML private Button saveBtn;

    @FXML
    private void onCancel() {
        // TODO: Close dialog
    }

    @FXML
    private void onSave() {
        // TODO: Save rule and close dialog
    }
}
