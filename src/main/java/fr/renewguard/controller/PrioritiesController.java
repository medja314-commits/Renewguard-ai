package fr.renewguard.controller;

import fr.renewguard.viewmodel.PrioritiesViewModel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

public class PrioritiesController implements Initializable {

    private final PrioritiesViewModel vm = new PrioritiesViewModel();

    @FXML private HBox kanbanWarningBanner;
    @FXML private Button dismissWarningBtn;
    @FXML private Label warningText;
    @FXML private Button addRuleBtn;
    @FXML private VBox colNiveau1;
    @FXML private VBox colNiveau2;
    @FXML private VBox colNiveau3;
    @FXML private VBox colNonPrioritaire;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        kanbanWarningBanner.visibleProperty().bind(vm.warningVisibleProperty());
        kanbanWarningBanner.managedProperty().bind(vm.warningVisibleProperty());
        warningText.textProperty().bind(vm.warningMessageProperty());
    }

    @FXML
    private void onAddRule() {
        // TODO: Show add rule dialog
    }

    @FXML
    private void onDismissWarning() {
        vm.dismissWarning();
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
