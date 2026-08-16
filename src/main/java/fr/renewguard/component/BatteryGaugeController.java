package fr.renewguard.component;

import javafx.animation.AnimationTimer;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.canvas.Canvas;

import javafx.scene.canvas.GraphicsContext;

import javafx.scene.control.Label;

import javafx.scene.paint.Color;

import javafx.scene.shape.ArcType;

import javafx.scene.shape.StrokeLineCap;

import java.net.URL;

import java.util.ResourceBundle;

public class BatteryGaugeController implements Initializable {

@FXML private Canvas gaugeCanvas;

@FXML private Label percentLabel;

@FXML private Label etaLabel;

@FXML private Label trendLabel;

private int displayedPercent = 0;

private int targetPercent = 0;

private AnimationTimer animator;

private static final double TRACK_WIDTH = 9;

private static final Color TRACK_COLOR = Color.web("#1E2A3A");

@Override

public void initialize(URL url, ResourceBundle rb) {

drawArc(0);

startAnimator();

}

// ── Public API ───────────────────────────────────────────────────

public void setPercent(int percent) {

this.targetPercent = Math.max(0, Math.min(100, percent));

percentLabel.setText(percent + "%");

}

public void setEta(String eta) {

etaLabel.setText(eta != null ? eta : "---");

}

public void setCharging(boolean charging, double rateKw) {

if (charging) {

trendLabel.setText("↑ En charge · +" +

String.format(java.util.Locale.FRANCE, "%.1f", rateKw) + " kW");

trendLabel.setStyle("-fx-text-fill: #22D3A5; -fx-font-size: 11px;");

} else {

trendLabel.setText("↓ Décharge");

trendLabel.setStyle("-fx-text-fill: #FFA53E; -fx-font-size: 11px;");

}

}

// ── Animation ────────────────────────────────────────────────────

private void startAnimator() {

animator = new AnimationTimer() {

@Override

public void handle(long now) {

if (displayedPercent != targetPercent) {

int step = targetPercent > displayedPercent ? 1 : -1;

displayedPercent += step;

drawArc(displayedPercent);

}

}

};

animator.start();

}

// ── Canvas drawing ───────────────────────────────────────────────

private void drawArc(int percent) {

double w = gaugeCanvas.getWidth();

double h = gaugeCanvas.getHeight();

double cx = w / 2.0;

double cy = h / 2.0;

double r = Math.min(w, h) / 2.0 - TRACK_WIDTH;

GraphicsContext gc = gaugeCanvas.getGraphicsContext2D();

gc.clearRect(0, 0, w, h);

// Track (full circle)

gc.setStroke(TRACK_COLOR);

gc.setLineWidth(TRACK_WIDTH);

gc.setLineCap(StrokeLineCap.ROUND);

gc.strokeArc(cx - r, cy - r, r * 2, r * 2,

0, 360, ArcType.OPEN);

// Filled arc

double sweep = 360.0 * percent / 100.0;

Color arcColor = arcColorForPercent(percent);

gc.setStroke(arcColor);

gc.setLineWidth(TRACK_WIDTH);

gc.setLineCap(StrokeLineCap.ROUND);

gc.strokeArc(cx - r, cy - r, r * 2, r * 2,

90, -sweep, ArcType.OPEN);

// Glow dot at arc tip

if (percent > 2) {

double angleRad = Math.toRadians(90 - sweep);

double dotX = cx + r * Math.cos(angleRad);

double dotY = cy - r * Math.sin(angleRad);

gc.setFill(arcColor);

gc.fillOval(dotX - TRACK_WIDTH / 2.0, dotY - TRACK_WIDTH / 2.0,

TRACK_WIDTH, TRACK_WIDTH);

}

// Update percent label colour

String hex = toHex(arcColor);

percentLabel.setStyle(

"-fx-font-family: 'JetBrains Mono'; -fx-font-size: 22px;" +

"-fx-font-weight: 700; -fx-text-fill: " + hex + ";");

}

private Color arcColorForPercent(int percent) {

if (percent <= 15) return Color.web("#FF4D4D");

if (percent <= 30) return Color.web("#FFA53E");

return Color.web("#3B9DFF");

}

private String toHex(Color c) {

return String.format("#%02X%02X%02X",

(int) (c.getRed() * 255),

(int) (c.getGreen() * 255),

(int) (c.getBlue() * 255));

}

}
