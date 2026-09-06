package fr.renewguard.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

public class PrioritiesController implements Initializable {

    @FXML private Button addRuleBtn;
    @FXML private Label warningText;
    @FXML private VBox colNiveau1;
    @FXML private VBox colNiveau2;
    @FXML private VBox colNiveau3;
    @FXML private VBox colNonPrioritaire;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize priorities controller
    }

    @FXML
    private void onAddRule() {
        // TODO: Show add rule dialog
    }

    @FXML
    private void onDismissWarning() {
        // TODO: Dismiss warning banner
    }

    @FXML
    private void onAddToNiveau1() {
        // TODO: Add to niveau 1
    }

    @FXML
    private void onAddToNiveau2() {
        // TODO: Add to niveau 2
    }

    @FXML
    private void onAddToNiveau3() {
        // TODO: Add to niveau 3
    }

    @FXML
    private void onAddToNonPrioritaire() {
        // TODO: Add to non prioritaire
    }
}
