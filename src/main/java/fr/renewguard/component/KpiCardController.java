package fr.renewguard.component;

import javafx.animation.KeyFrame;

import javafx.animation.Timeline;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.control.Label;

import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;

import javafx.util.Duration;

import java.net.URL;

import java.util.ResourceBundle;

public class KpiCardController implements Initializable {

@FXML private VBox cardRoot;

@FXML private Label labelText;

@FXML private Label iconLabel;

@FXML private Label valueLabel;

@FXML private Label unitLabel;

@FXML private Label trendLabel;

@FXML private HBox valueRow;

@Override

public void initialize(URL url, ResourceBundle rb) {}

/**

* Called once after FXML injection to configure the static card identity.

*

* @param label card title

* @param icon emoji / symbol shown top-right

* @param color one of: GREEN, BLUE, AMBER, RED

*/

public void configure(String label, String icon, String color) {

labelText.setText(label);

iconLabel.setText(icon);

applyGlow(color);

}

/**

* Updates the live value displayed on the card.

*

* @param value formatted numeric string

* @param unit unit suffix (may be empty)

* @param trend subtitle / delta text

* @param up true = green up, false = red down, null = neutral grey

*/

public void setValue(String value, String unit, String trend, Boolean up) {

animateValue(value);

unitLabel.setText(unit);

trendLabel.setText(trend);

trendLabel.getStyleClass().removeAll(

"kpi-trend-up", "kpi-trend-down", "kpi-sub");

if (up == null) {

trendLabel.getStyleClass().add("kpi-sub");

} else if (up) {

trendLabel.getStyleClass().add("kpi-trend-up");

} else {

trendLabel.getStyleClass().add("kpi-trend-down");

}

}

private void animateValue(String target) {

valueLabel.setOpacity(0);

valueLabel.setText(target);

Timeline fade = new Timeline(

new KeyFrame(Duration.ZERO,

new javafx.animation.KeyValue(valueLabel.opacityProperty(), 0)),

new KeyFrame(Duration.millis(350),

new javafx.animation.KeyValue(valueLabel.opacityProperty(), 1,

javafx.animation.Interpolator.EASE_OUT))

);

fade.play();

}

private void applyGlow(String color) {

cardRoot.getStyleClass().removeAll(

"card-glow-green", "card-glow-blue", "card-glow-amber", "card-glow-red");

String iconStyle = switch (color) {

case "GREEN" -> { cardRoot.getStyleClass().add("card-glow-green");

yield "-fx-text-fill: #22D3A5;"; }

case "BLUE" -> { cardRoot.getStyleClass().add("card-glow-blue");

yield "-fx-text-fill: #3B9DFF;"; }

case "AMBER" -> { cardRoot.getStyleClass().add("card-glow-amber");

yield "-fx-text-fill: #FFA53E;"; }

case "RED" -> { cardRoot.getStyleClass().add("card-glow-red");

yield "-fx-text-fill: #FF4D4D;"; }

default -> "-fx-text-fill: #8B93A7;";

};

iconLabel.setStyle(iconStyle + " -fx-font-size: 17px;");

}

}
