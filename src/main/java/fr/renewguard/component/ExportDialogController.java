package fr.renewguard.component;

import fr.renewguard.service.HistoryService;
import fr.renewguard.util.DialogHost;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExportDialogController {

    @FXML private ComboBox<String> formatCombo;
    @FXML private ComboBox<String> periodCombo;
    @FXML private Label formatError;
    @FXML private Label periodError;
    @FXML private Button cancelBtn;
    @FXML private Button exportBtn;

    private DialogHost dialogHost;
    private HistoryService historyService = HistoryService.getInstance();

    public void setDialogHost(DialogHost host) {
        this.dialogHost = host;
    }

    @FXML
    public void initialize() {
        formatCombo.getItems().addAll("CSV", "PDF", "XLSX");
        periodCombo.getItems().addAll("Aujourd'hui", "Cette semaine", "Ce mois", "Les 3 derniers mois", "L'année");
    }

    @FXML
    private void onCancel() {
        if (dialogHost != null) {
            dialogHost.close();
        }
        clearForm();
    }

    @FXML
    private void onExport() {
        String format = formatCombo.getValue();
        String period = periodCombo.getValue();

        if (format == null || format.isEmpty()) {
            formatError.setText("Un format doit être sélectionné");
            formatError.setVisible(true);
            formatError.setManaged(true);
            return;
        }

        if (period == null || period.isEmpty()) {
            periodError.setText("Une période doit être sélectionnée");
            periodError.setVisible(true);
            periodError.setManaged(true);
            return;
        }

        formatError.setVisible(false);
        formatError.setManaged(false);
        periodError.setVisible(false);
        periodError.setManaged(false);

        String periodCode = mapPeriodToCode(period);

        historyService.exportReport(periodCode, format.toLowerCase())
            .thenAcceptAsync(data -> {
                saveFile(data, format);
            }, javafx.application.Platform::runLater)
            .exceptionally(ex -> {
                periodError.setText("Erreur lors de l'export: " + ex.getMessage());
                periodError.setVisible(true);
                periodError.setManaged(true);
                return null;
            });

        if (dialogHost != null) {
            dialogHost.close();
        }
        clearForm();
    }

    private String mapPeriodToCode(String period) {
        return switch (period) {
            case "Aujourd'hui" -> "today";
            case "Cette semaine" -> "week";
            case "Ce mois" -> "month";
            case "Les 3 derniers mois" -> "quarter";
            case "L'année" -> "year";
            default -> "month";
        };
    }

    private void saveFile(byte[] data, String format) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer l'export");
        fileChooser.setInitialFileName("equipements_export." + format.toLowerCase());
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter(format + " files", "*." + format.toLowerCase())
        );

        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            try (FileOutputStream fos = new FileOutputStream(selectedFile)) {
                fos.write(data);
                showSuccessMessage("Export sauvegardé avec succès");
            } catch (IOException e) {
                showErrorMessage("Erreur lors de la sauvegarde: " + e.getMessage());
            }
        }
    }

    private void showSuccessMessage(String message) {
        System.out.println("✓ " + message);
    }

    private void showErrorMessage(String message) {
        System.err.println("✗ " + message);
    }

    private void clearForm() {
        formatCombo.setValue(null);
        periodCombo.setValue(null);
        formatError.setVisible(false);
        formatError.setManaged(false);
        periodError.setVisible(false);
        periodError.setManaged(false);
    }
}
