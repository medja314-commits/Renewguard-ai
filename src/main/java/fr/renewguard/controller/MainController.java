package fr.renewguard.controller;

import fr.renewguard.navigation.SceneManager;

import fr.renewguard.util.FxmlLoader;

import fr.renewguard.viewmodel.shared.SessionViewModel;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.Parent;

import javafx.scene.input.KeyCode;

import javafx.scene.input.KeyCodeCombination;

import javafx.scene.input.KeyCombination;

import javafx.scene.layout.*;

import java.net.URL;

import java.util.ResourceBundle;

public class MainController implements Initializable {

@FXML private BorderPane rootPane;

@FXML private StackPane contentArea;

@FXML private VBox emergencyBanner;

@FXML private VBox sidebarContainer;

@FXML private HBox topbarContainer;

private SidebarController sidebarController;

private TopbarController topbarController;

private final SessionViewModel session = SessionViewModel.getInstance();

@Override

public void initialize(URL url, ResourceBundle rb) {

loadSidebar(); loadTopbar(); navigateTo("dashboard");

bindEmergencyBanner(); registerKeyShortcuts();

}

private void loadSidebar() {

FxmlLoader.Result<SidebarController> result = FxmlLoader.loadWithResult("/fr/renewguard/fxml/Sidebar.fxml");

sidebarController = result.controller();

sidebarController.setNavigationCallback(this::navigateTo);

sidebarContainer.getChildren().setAll(result.root());

VBox.setVgrow(result.root(), Priority.ALWAYS);

}

private void loadTopbar() {

FxmlLoader.Result<TopbarController> result = FxmlLoader.loadWithResult("/fr/renewguard/fxml/Topbar.fxml");

topbarController = result.controller();

topbarController.setOnLogout(this::handleLogout);

topbarContainer.getChildren().setAll(result.root());

HBox.setHgrow(result.root(), Priority.ALWAYS);

}

public void navigateTo(String screen) {

String fxmlPath = switch (screen) {

case "dashboard" -> "/fr/renewguard/fxml/Dashboard.fxml";

case "equipment" -> "/fr/renewguard/fxml/Equipment.fxml";

case "priorities" -> "/fr/renewguard/fxml/Priorities.fxml";

case "ai" -> "/fr/renewguard/fxml/Ai.fxml";

case "history" -> "/fr/renewguard/fxml/History.fxml";

default -> "/fr/renewguard/fxml/Dashboard.fxml";

};

Parent view = FxmlLoader.load(fxmlPath);

contentArea.getChildren().setAll(view);

session.setActiveScreen(screen);

if (sidebarController != null) sidebarController.setActive(screen);

if (topbarController != null) topbarController.setTitle(screenTitle(screen));

}

private String screenTitle(String screen) {

return switch (screen) {

case "dashboard" -> "Dashboard energetique";

case "equipment" -> "Gestion des equipements";

case "priorities" -> "Priorites energetiques";

case "ai" -> "Analyse IA & Assistant";

case "history" -> "Historique & Alertes";

default -> "RenewGuard AI";

};

}

private void bindEmergencyBanner() {

emergencyBanner.visibleProperty().bind(session.emergencyModeProperty());

emergencyBanner.managedProperty().bind(session.emergencyModeProperty());

session.emergencyModeProperty().addListener((obs, old, active) -> {

if (active) rootPane.getStyleClass().add("emergency");

else rootPane.getStyleClass().remove("emergency");

});

}

@FXML private void onDisableEmergency() { session.setEmergencyMode(false); }

private void registerKeyShortcuts() {

rootPane.sceneProperty().addListener((obs, old, scene) -> {

if (scene == null) return;

scene.getAccelerators().put(new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.CONTROL_DOWN), () -> navigateTo("dashboard"));

scene.getAccelerators().put(new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.CONTROL_DOWN), () -> navigateTo("equipment"));

scene.getAccelerators().put(new KeyCodeCombination(KeyCode.DIGIT3, KeyCombination.CONTROL_DOWN), () -> navigateTo("priorities"));

scene.getAccelerators().put(new KeyCodeCombination(KeyCode.DIGIT4, KeyCombination.CONTROL_DOWN), () -> navigateTo("ai"));

scene.getAccelerators().put(new KeyCodeCombination(KeyCode.DIGIT5, KeyCombination.CONTROL_DOWN), () -> navigateTo("history"));

scene.getAccelerators().put(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN), () -> session.toggleEmergency());

scene.getAccelerators().put(new KeyCodeCombination(KeyCode.ESCAPE), this::handleEscape);

});

}

private void handleEscape() { if (session.isEmergencyMode()) session.setEmergencyMode(false); }

private void handleLogout() { session.logout(); SceneManager.navigate("auth"); }

}
