package fr.renewguard.component;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ConfirmActionDialogController {

    @FXML private Label titleLabel;
    @FXML private Label messageLabel;
    @FXML private Button cancelBtn;
    @FXML private Button confirmBtn;

    @FXML
    private void onCancel() {
        // TODO: Close dialog
    }

    @FXML
    private void onConfirm() {
        // TODO: Execute action and close dialog
    }
}
