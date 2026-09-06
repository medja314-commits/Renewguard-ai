package fr.renewguard.controller;

import fr.renewguard.viewmodel.shared.SessionViewModel;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.control.Label;

import javafx.scene.input.MouseEvent;

import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;

import java.net.URL;

import java.util.Map;

import java.util.ResourceBundle;

import java.util.function.Consumer;

public class SidebarController implements Initializable {

@FXML private VBox sidebarRoot;

@FXML private Label siteInfoLabel;

@FXML private HBox navDashboard;

@FXML private HBox navEquipement;

@FXML private HBox navPriorites;

@FXML private HBox navIA;

@FXML private HBox navHistorique;

private Consumer<String> navigationCallback;

private final SessionViewModel session = SessionViewModel.getInstance();

@Override
public void initialize(URL url, ResourceBundle rb) {
	setActive("dashboard");
}

@FXML
private void onNavItemClicked(MouseEvent event) {
	if (event.getSource() instanceof HBox hbox) {
		String screen = (String) hbox.getUserData();
		if (screen != null) {
			navigate(screen);
			setActive(screen);
		}
	}
}

@FXML
private void onToggleCollapse() {
	// Implementation will be added if needed
}

public void setNavigationCallback(Consumer<String> callback) { this.navigationCallback = callback; }

private void navigate(String screen) { if (navigationCallback != null) navigationCallback.accept(screen); }

public void setActive(String screen) {
	Map<String, HBox> items = Map.of(
		"dashboard", navDashboard, "equipment", navEquipement,
		"priorities", navPriorites, "ai", navIA, "history", navHistorique);

	items.forEach((key, item) -> {
		item.getStyleClass().removeAll("nav-item-active");
		item.getStyleClass().add("nav-item");
	});

	HBox active = items.get(screen);
	if (active != null) {
		active.getStyleClass().remove("nav-item");
		active.getStyleClass().add("nav-item-active");
	}
}

}
