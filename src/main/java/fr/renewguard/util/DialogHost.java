package fr.renewguard.util;

import javafx.scene.layout.StackPane;

public class DialogHost {

    private final StackPane overlay;

    public DialogHost(StackPane overlay) {
        this.overlay = overlay;
    }

    public void show(javafx.scene.Parent dialogRoot) {
        overlay.getChildren().clear();
        overlay.getChildren().add(dialogRoot);
        overlay.setVisible(true);
        overlay.setManaged(true);
    }

    public void close() {
        overlay.getChildren().clear();
        overlay.setVisible(false);
        overlay.setManaged(false);
    }
}
