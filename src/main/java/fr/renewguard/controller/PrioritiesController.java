package fr.renewguard.controller;

import fr.renewguard.component.AddRuleDialogController;
import fr.renewguard.component.ToastController;
import fr.renewguard.util.DialogHost;
import fr.renewguard.util.FxmlLoader;
import fr.renewguard.viewmodel.PrioritiesViewModel;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
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
    @FXML private StackPane dialogOverlay;
    @FXML private StackPane prioritiesRoot;

    private DialogHost dialogHost;
    private ToastController toastController;
    private Region toastContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        kanbanWarningBanner.visibleProperty().bind(vm.warningVisibleProperty());
        kanbanWarningBanner.managedProperty().bind(vm.warningVisibleProperty());
        warningText.textProperty().bind(vm.warningMessageProperty());
        dialogHost = new DialogHost(dialogOverlay);

        try {
            FxmlLoader.Result<ToastController> toastResult = FxmlLoader.loadWithResult("/fr/renewguard/fxml/components/Toast.fxml");
            toastController = toastResult.controller();
            toastContainer = (Region) toastResult.root();
            toastContainer.setVisible(false);
            toastContainer.setManaged(false);
            prioritiesRoot.getChildren().add(toastContainer);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du Toast: " + e.getMessage());
        }
    }

    @FXML
    private void onAddRule() {
        FxmlLoader.Result<AddRuleDialogController> result =
            FxmlLoader.loadWithResult("/fr/renewguard/fxml/components/AddRuleDialog.fxml");
        AddRuleDialogController controller = result.controller();
        controller.setDialogHost(dialogHost);
        controller.setViewModel(vm);
        dialogHost.show(result.root());
    }

    @FXML
    private void onDismissWarning() {
        vm.dismissWarning();
        showToast("Priorités mises à jour", ToastController.Type.SUCCESS);
    }

    private void showToast(String message, ToastController.Type type) {
        if (toastController != null) {
            toastController.show(message, type);
            toastContainer.setVisible(true);
            toastContainer.setManaged(true);
            Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> {
                    toastContainer.setVisible(false);
                    toastContainer.setManaged(false);
                })
            );
            timeline.play();
        }
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
