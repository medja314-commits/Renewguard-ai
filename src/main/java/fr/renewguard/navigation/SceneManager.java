package fr.renewguard.navigation;

import fr.renewguard.util.FxmlLoader;

import javafx.scene.Parent;

import javafx.scene.Scene;

import javafx.stage.Stage;

import java.util.HashMap;

import java.util.Map;

import java.util.Objects;

public class SceneManager {

private static Stage primaryStage;

private static Scene rootScene;

private static final Map<String, String> ROUTES = new HashMap<>();

static {

ROUTES.put("auth", "/fr/renewguard/fxml/Auth.fxml");

ROUTES.put("main", "/fr/renewguard/fxml/Main.fxml");

ROUTES.put("dashboard", "/fr/renewguard/fxml/Dashboard.fxml");

ROUTES.put("equipment", "/fr/renewguard/fxml/Equipment.fxml");

ROUTES.put("priorities", "/fr/renewguard/fxml/Priorities.fxml");

ROUTES.put("ai", "/fr/renewguard/fxml/Ai.fxml");

ROUTES.put("history", "/fr/renewguard/fxml/History.fxml");

}

public static void init(Stage stage) {

primaryStage = stage;

primaryStage.setTitle("RenewGuard AI");

primaryStage.setWidth(1280);

primaryStage.setHeight(800);

primaryStage.setMinWidth(1024);

primaryStage.setMinHeight(640);

primaryStage.setResizable(true);

}

public static void navigate(String route) {

String path = ROUTES.get(route);

if (path == null) throw new IllegalArgumentException("Unknown route: " + route);

Parent root = FxmlLoader.load(path);

if (rootScene == null) {

rootScene = new Scene(root);

applyStylesheets(rootScene);

primaryStage.setScene(rootScene);

primaryStage.show();

} else {

rootScene.setRoot(root);

}

}

private static void applyStylesheets(Scene scene) {

// Ordre important : base.css (resets generiques .button/.text-input/...)

// DOIT etre charge avant theme-dark.css/components.css, sinon les resets

// generiques ecrasent les styles specifiques (meme specificite CSS, donc

// c'est l'ordre de chargement qui tranche).

scene.getStylesheets().add(resource("/fr/renewguard/css/base.css"));

scene.getStylesheets().add(resource("/fr/renewguard/css/theme-dark.css"));

scene.getStylesheets().add(resource("/fr/renewguard/css/components.css"));

}

private static String resource(String path) {

return Objects.requireNonNull(SceneManager.class.getResource(path)).toExternalForm();

}

public static Stage getStage() { return primaryStage; }

public static Scene getScene() { return rootScene; }

}
