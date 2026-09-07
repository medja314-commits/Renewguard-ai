package fr.renewguard.component;

import fr.renewguard.util.DialogHost;
import fr.renewguard.util.FxmlLoader;
import fr.renewguard.viewmodel.PrioritiesViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import java.net.URL;
import java.util.ResourceBundle;

public class AddRuleDialogController implements Initializable {

    @FXML private ComboBox<String> conditionField;
    @FXML private Label conditionFieldError;
    @FXML private Button cancelBtn;
    @FXML private Button saveBtn;

    private DialogHost dialogHost;
    private PrioritiesViewModel viewModel;
    private ToastController toastController;
    private Region toastContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            FxmlLoader.Result<ToastController> toastResult = FxmlLoader.loadWithResult("/fr/renewguard/fxml/components/Toast.fxml");
            toastController = toastResult.controller();
            toastContainer = (Region) toastResult.root();
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du Toast: " + e.getMessage());
        }
    }

    public void setDialogHost(DialogHost host) {
        this.dialogHost = host;
    }

    public void setViewModel(PrioritiesViewModel vm) {
        this.viewModel = vm;
    }

    @FXML
    private void onCancel() {
        if (dialogHost != null) {
            dialogHost.close();
        }
        clearForm();
    }

    @FXML
    private void onSave() {
        String condition = conditionField.getValue();

        if (condition == null || condition.isEmpty()) {
            conditionFieldError.setText("Une condition doit être sélectionnée");
            conditionFieldError.setVisible(true);
            conditionFieldError.setManaged(true);
            return;
        }

        if (viewModel != null) {
            viewModel.addRule(condition);
            showToast("Règle créée avec succès", ToastController.Type.SUCCESS);
        }

        if (dialogHost != null) {
            dialogHost.close();
        }
        clearForm();
    }

    private void showToast(String message, ToastController.Type type) {
        if (toastController != null) {
            toastController.show(message, type);
            javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(3), e -> {
                    toastContainer.setVisible(false);
                    toastContainer.setManaged(false);
                })
            );
            timeline.play();
        }
    }

    private void clearForm() {
        conditionField.setValue(null);
        conditionFieldError.setVisible(false);
        conditionFieldError.setManaged(false);
    }
}
