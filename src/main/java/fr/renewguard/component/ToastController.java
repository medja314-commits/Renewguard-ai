package fr.renewguard.component;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ToastController {

    @FXML private Label toastIcon;
    @FXML private Label toastMessage;

    public enum Type { SUCCESS, ERROR, WARNING, INFO }

    public void show(String message, Type type) {
        toastMessage.setText(message);
        switch(type) {
            case SUCCESS:
                toastIcon.setText("✓");
                toastIcon.setStyle("-fx-text-fill: #22D3A5;");
                break;
            case ERROR:
                toastIcon.setText("✗");
                toastIcon.setStyle("-fx-text-fill: #FF4D4D;");
                break;
            case WARNING:
                toastIcon.setText("⚠");
                toastIcon.setStyle("-fx-text-fill: #FFA53E;");
                break;
            case INFO:
                toastIcon.setText("ℹ");
                toastIcon.setStyle("-fx-text-fill: #3B9DFF;");
                break;
        }
    }
}
