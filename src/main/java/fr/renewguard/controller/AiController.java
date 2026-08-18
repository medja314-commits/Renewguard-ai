package fr.renewguard.controller;

import fr.renewguard.component.ChatBubbleController;

import fr.renewguard.model.dto.AiDecisionDto;

import fr.renewguard.model.dto.PredictionDto;

import fr.renewguard.util.FxmlLoader;

import fr.renewguard.util.NumberFormatter;

import fr.renewguard.viewmodel.AiViewModel;

import fr.renewguard.viewmodel.AiViewModel.ChatMessage;

import javafx.collections.ListChangeListener;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.chart.*;

import javafx.scene.control.Button;

import javafx.scene.control.Label;

import javafx.scene.control.TextField;

import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;

import java.net.URL;

import java.util.ResourceBundle;

public class AiController implements Initializable {

@FXML private Label totalDecisionsLabel;

@FXML private Label pendingDecisionsLabel;

@FXML private LineChart<String, Number> predictionChart;

@FXML private VBox timelineContainer;

@FXML private VBox chatMessages;

@FXML private TextField chatInputField;

@FXML private Button sendBtn;

@FXML private Button demoChatBtn;

@FXML private HBox typingIndicator;

private final AiViewModel vm = new AiViewModel();

@Override

public void initialize(URL url, ResourceBundle rb) {

predictionChart.setAnimated(false);

predictionChart.setCreateSymbols(false);

vm.getPredictions().addListener((ListChangeListener<PredictionDto>) c -> refreshChart());

vm.getDecisions().addListener((ListChangeListener<AiDecisionDto>) c -> renderTimeline());

vm.totalDecisionsProperty().addListener((o, old, val) -> totalDecisionsLabel.setText(val + " decisions"));

vm.pendingDecisionsProperty().addListener((o, old, val) -> pendingDecisionsLabel.setText(val + " en attente"));

chatInputField.textProperty().bindBidirectional(vm.chatInputProperty());

chatInputField.setOnAction(e -> vm.sendMessage());

vm.chatLoadingProperty().addListener((o, old, loading) -> {

sendBtn.setDisable(loading);

typingIndicator.setVisible(loading);

typingIndicator.setManaged(loading);

});

vm.setOnNewMessage(this::onNewMessage);

vm.refresh();

}

private void refreshChart() {

XYChart.Series<String, Number> real = new XYChart.Series<>();

real.setName("Reel");

XYChart.Series<String, Number> predicted = new XYChart.Series<>();

predicted.setName("Prediction IA");

for (PredictionDto p : vm.getPredictions()) {

if (p.getConsumptionCurve() == null) continue;

for (var point : p.getConsumptionCurve()) {

if (point.hasReal()) real.getData().add(new XYChart.Data<>(point.getLabel(), point.getReal()));

if (point.hasPredicted()) predicted.getData().add(new XYChart.Data<>(point.getLabel(), point.getPredicted()));

}

}

predictionChart.getData().setAll(real, predicted);

}

private void renderTimeline() {

timelineContainer.getChildren().clear();

for (AiDecisionDto d : vm.getDecisions()) {

VBox item = new VBox(4);

item.getStyleClass().add("timeline-item");

item.getChildren().addAll(

new Label(d.getAction()),

new Label(d.getReason()),

new Label(NumberFormatter.formatTimestamp(d.getTimestamp()) + " - " + d.getImpactLabel())

);

if (d.isPending() && !d.isAcknowledged()) {

Button ack = new Button("Confirmer");

ack.setOnAction(e -> vm.acknowledgeDecision(d.getId()));

item.getChildren().add(ack);

}

timelineContainer.getChildren().add(item);

}

}

private void onNewMessage(ChatMessage msg) {

FxmlLoader.Result<ChatBubbleController> result =

FxmlLoader.loadWithResult("/fr/renewguard/fxml/components/ChatBubble.fxml");

result.controller().bind(msg.text(), msg.isUser(), msg.timestamp());

chatMessages.getChildren().add(result.root());

}

@FXML private void onSend() { vm.sendMessage(); }

@FXML private void onRefresh() { vm.refresh(); }

@FXML private void onDemoChatQuestion() {

vm.chatInputProperty().set("Pourquoi avez-vous coupé la climatisation ?");

vm.sendMessage();

}

}
