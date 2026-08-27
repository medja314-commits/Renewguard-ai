package fr.renewguard.component;

import fr.renewguard.model.dto.AiDecisionDto;

import fr.renewguard.util.NumberFormatter;

import javafx.animation.FadeTransition;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.control.Label;

import javafx.scene.control.ProgressBar;

import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;

import javafx.util.Duration;

import java.net.URL;

import java.util.ResourceBundle;

public class AiDecisionCardController implements Initializable {

@FXML private VBox cardRoot;

@FXML private Label titleLabel;

@FXML private Label actionLabel;

@FXML private Label reasonLabel;

@FXML private Label impactLabel;

@FXML private Label timestampLabel;

@FXML private Label statusBadge;

@FXML private ProgressBar confidenceBar;

@FXML private Label confidenceLabel;

@FXML private HBox detailsRow;

@Override

public void initialize(URL url, ResourceBundle rb) {}

public void apply(AiDecisionDto decision) {

actionLabel.setText(decision.getAction());

reasonLabel.setText(decision.getReason());

impactLabel.setText("Impact : " + decision.getImpactLabel());

timestampLabel.setText(NumberFormatter.formatTimeOnly(decision.getTimestamp()));

double confidence = decision.getConfidencePercent() / 100.0;

confidenceBar.setProgress(confidence);

confidenceLabel.setText(decision.getConfidencePercent() + "%");

applyStatus(decision);

fadeIn();

}

private void applyStatus(AiDecisionDto decision) {

statusBadge.getStyleClass().removeAll("badge-done", "badge-pending");

if (decision.isPending()) {

statusBadge.setText("EN ATTENTE");

statusBadge.getStyleClass().add("badge-pending");

} else {

statusBadge.setText("EFFECTUÉ");

statusBadge.getStyleClass().add("badge-done");

}

}

private void fadeIn() {

cardRoot.setOpacity(0);

FadeTransition ft = new FadeTransition(Duration.millis(400), cardRoot);

ft.setFromValue(0);

ft.setToValue(1);

ft.play();

}

}
