package fr.renewguard.component;

import fr.renewguard.model.dto.AlertDto;

import fr.renewguard.util.NumberFormatter;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.control.Button;

import javafx.scene.control.Label;

import javafx.scene.layout.HBox;

import javafx.scene.shape.Circle;

import java.net.URL;

import java.util.ResourceBundle;

import java.util.function.Consumer;

public class AlertItemController implements Initializable {

@FXML private HBox itemRoot;

@FXML private Circle severityDot;

@FXML private Label titleLabel;

@FXML private Label descLabel;

@FXML private Label timeLabel;

@FXML private Button resolveBtn;

private Consumer<Long> onResolve;

@Override

public void initialize(URL url, ResourceBundle rb) {}

public void bind(AlertDto alert, Consumer<Long> resolveCallback) {

this.onResolve = resolveCallback;

titleLabel.setText(alert.getTitle());

descLabel.setText(alert.getDescription());

timeLabel.setText(NumberFormatter.formatTimestamp(alert.getTimestamp()));

severityDot.setStyle("-fx-fill: " + alert.getSeverity().dotColor() + ";");

if (alert.isResolved()) {

resolveBtn.setText("Resolu");

resolveBtn.setDisable(true);

itemRoot.setOpacity(0.5);

} else {

resolveBtn.setOnAction(e -> { if (onResolve != null) onResolve.accept(alert.getId()); });

}

}

}
