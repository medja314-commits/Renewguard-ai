package fr.renewguard.component;

import fr.renewguard.util.NumberFormatter;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.control.Label;

import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;

import java.net.URL;

import java.time.LocalDateTime;

import java.util.ResourceBundle;

public class ChatBubbleController implements Initializable {

@FXML private HBox bubbleRoot;

@FXML private VBox bubble;

@FXML private Label textLabel;

@FXML private Label timeLabel;

@Override

public void initialize(URL url, ResourceBundle rb) {}

public void bind(String text, boolean isUser, LocalDateTime timestamp) {

textLabel.setText(text);

timeLabel.setText(NumberFormatter.formatTimeOnly(timestamp));

bubble.getStyleClass().add(isUser ? "bubble-user" : "bubble-ai");

bubbleRoot.setStyle(isUser ? "-fx-alignment: CENTER_RIGHT;" : "-fx-alignment: CENTER_LEFT;");

}

}
