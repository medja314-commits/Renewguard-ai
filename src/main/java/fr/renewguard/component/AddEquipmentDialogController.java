package fr.renewguard.component;

import fr.renewguard.util.DialogHost;
import fr.renewguard.util.FxmlLoader;
import fr.renewguard.viewmodel.EquipmentViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import java.net.URL;
import java.util.ResourceBundle;

public class AddEquipmentDialogController implements Initializable {

    @FXML private TextField nameField;
    @FXML private Label nameFieldError;
    @FXML private Button cancelBtn;
    @FXML private Button createBtn;

    private DialogHost dialogHost;
    private EquipmentViewModel viewModel;
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

    public void setViewModel(EquipmentViewModel vm) {
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
    private void onCreate() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            nameFieldError.setText("Le nom de l'equipement est obligatoire");
            nameFieldError.setVisible(true);
            nameFieldError.setManaged(true);
            return;
        }

        if (viewModel != null) {
            viewModel.addEquipment(name);
            showToast("Équipement '" + name + "' créé avec succès", ToastController.Type.SUCCESS);
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
        nameField.clear();
        nameFieldError.setVisible(false);
        nameFieldError.setManaged(false);
    }
}
