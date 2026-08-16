**RenewGuard AI**

Logiciel de bureau --- Java / JavaFX

*Architecture MVVM · Client REST FastAPI · Dark Mode Glassmorphism*

Dossier technique --- Arborescence, code source et recommandations

**Sommaire**

- 1\. Vue d\'ensemble du projet

- 2\. Arborescence complète

- 3\. Conseils et recommandations

- 4\. Configuration Maven (pom.xml, module-info.java)

- 5\. Point d\'entrée et navigation

- 6\. Utilitaires

- 7\. Modèles de données --- Enums

- 8\. Modèles de données --- DTOs

- 9\. Couche API --- Services

- 10\. ViewModels

- 11\. Contrôleurs

- 12\. Composants réutilisables

- 13\. Vues FXML

- 14\. Styles CSS

- 15\. Configuration & fichiers complémentaires

**1. Vue d\'ensemble du projet**

RenewGuard AI est un logiciel de bureau de pilotage énergétique intelligent, développé en Java 21 / JavaFX 21 selon le pattern architectural MVVM (Model-View-ViewModel). Il supervise en temps réel un système solaire décentralisé --- panneau solaire, capteurs IoT, microcontrôleur ESP32 --- via un service Python/FastAPI intermédiaire qui héberge la logique Machine Learning et les décisions d\'optimisation IA.

Le client Java ne contient aucune logique métier d\'intelligence artificielle : il consomme exclusivement une API REST (endpoints /energy, /equipment, /priorities, /ai, /history, /alerts) et affiche les résultats dans une interface dark-mode glassmorphique cohérente sur 5 écrans (Dashboard, Équipements, Priorités, IA, Historique).

  ----------------------------------- -------------------------------------------------------------------
  **Nombre de fichiers Java**         52

  **Nombre de vues FXML**             16 (9 écrans + 7 composants) --- 16/16 complet

  **Nombre de fichiers CSS**          3 (theme-dark, base, components) --- complet

  **Pattern architectural**           MVVM (ViewModel observable JavaFX)

  **Couche réseau**                   OkHttp + CompletableFuture + Jackson

  **Écrans principaux**               Dashboard, Équipements, Priorités, IA, Historique

  **Palette**                         #0A0E14 / #22D3A5 / #3B9DFF / #FFA53E / #FF4D4D

  **Statut du projet**                Compilable apres ajout de application.properties + AppConfig.java
  ----------------------------------- -------------------------------------------------------------------

**2. Arborescence complète**

Structure Maven standard. Les fichiers marqués (\*) sont détaillés dans les sections dédiées (styles, vues FXML, contrôleurs d\'écrans) mais suivent rigoureusement le même pattern que les fichiers déjà développés en intégralité dans ce document.

> renewguard-ai/
>
> ├── src/
>
> │ ├── main/
>
> │ │ ├── java/
>
> │ │ │ ├── fr/
>
> │ │ │ │ └── renewguard/
>
> │ │ │ │ ├── component/
>
> │ │ │ │ │ ├── AiDecisionCardController.java
>
> │ │ │ │ │ ├── AlertItemController.java
>
> │ │ │ │ │ ├── BatteryGaugeController.java
>
> │ │ │ │ │ ├── ChatBubbleController.java
>
> │ │ │ │ │ ├── EquipmentRowController.java
>
> │ │ │ │ │ ├── KpiCardController.java
>
> │ │ │ │ │ └── PriorityKanbanCardController.java
>
> │ │ │ │ ├── config/
>
> │ │ │ │ │ └── AppConfig.java
>
> │ │ │ │ ├── controller/
>
> │ │ │ │ │ ├── AiController.java
>
> │ │ │ │ │ ├── AuthController.java
>
> │ │ │ │ │ ├── DashboardController.java
>
> │ │ │ │ │ ├── EquipmentController.java
>
> │ │ │ │ │ ├── HistoryController.java
>
> │ │ │ │ │ ├── MainController.java
>
> │ │ │ │ │ ├── PrioritiesController.java
>
> │ │ │ │ │ ├── SidebarController.java
>
> │ │ │ │ │ └── TopbarController.java
>
> │ │ │ │ ├── model/
>
> │ │ │ │ │ ├── dto/
>
> │ │ │ │ │ │ ├── AiDecisionDto.java
>
> │ │ │ │ │ │ ├── AlertDto.java
>
> │ │ │ │ │ │ ├── AuthResponseDto.java
>
> │ │ │ │ │ │ ├── ChatResponseDto.java
>
> │ │ │ │ │ │ ├── EnergySnapshotDto.java
>
> │ │ │ │ │ │ ├── EquipmentDto.java
>
> │ │ │ │ │ │ ├── HistoryPointDto.java
>
> │ │ │ │ │ │ ├── HistorySummaryDto.java
>
> │ │ │ │ │ │ ├── PredictionDto.java
>
> │ │ │ │ │ │ ├── PriorityLevelDto.java
>
> │ │ │ │ │ │ └── RuleDto.java
>
> │ │ │ │ │ └── enums/
>
> │ │ │ │ │ ├── AlertSeverity.java
>
> │ │ │ │ │ ├── EquipmentStatus.java
>
> │ │ │ │ │ └── PriorityLevel.java
>
> │ │ │ │ ├── navigation/
>
> │ │ │ │ │ └── SceneManager.java
>
> │ │ │ │ ├── service/
>
> │ │ │ │ │ ├── AiService.java
>
> │ │ │ │ │ ├── ApiClient.java
>
> │ │ │ │ │ ├── AuthInterceptor.java
>
> │ │ │ │ │ ├── EnergyService.java
>
> │ │ │ │ │ ├── EquipmentService.java
>
> │ │ │ │ │ ├── HistoryService.java
>
> │ │ │ │ │ └── PriorityService.java
>
> │ │ │ │ ├── util/
>
> │ │ │ │ │ ├── FxmlLoader.java
>
> │ │ │ │ │ ├── NumberFormatter.java
>
> │ │ │ │ │ ├── PollingScheduler.java
>
> │ │ │ │ │ └── TokenStore.java
>
> │ │ │ │ ├── viewmodel/
>
> │ │ │ │ │ ├── shared/
>
> │ │ │ │ │ │ ├── NotificationViewModel.java
>
> │ │ │ │ │ │ └── SessionViewModel.java
>
> │ │ │ │ │ ├── AiViewModel.java
>
> │ │ │ │ │ ├── AuthViewModel.java
>
> │ │ │ │ │ ├── DashboardViewModel.java
>
> │ │ │ │ │ ├── EquipmentViewModel.java
>
> │ │ │ │ │ ├── HistoryViewModel.java
>
> │ │ │ │ │ └── PrioritiesViewModel.java
>
> │ │ │ │ └── RenewGuardApp.java
>
> │ │ │ └── module-info.java
>
> │ │ └── resources/
>
> │ │ ├── fr/
>
> │ │ │ └── renewguard/
>
> │ │ │ ├── css/
>
> │ │ │ │ ├── base.css
>
> │ │ │ │ ├── components.css
>
> │ │ │ │ └── theme-dark.css
>
> │ │ │ └── fxml/
>
> │ │ │ ├── components/
>
> │ │ │ │ ├── AiDecisionCard.fxml
>
> │ │ │ │ ├── AlertItem.fxml
>
> │ │ │ │ ├── BatteryGauge.fxml
>
> │ │ │ │ ├── ChatBubble.fxml
>
> │ │ │ │ ├── EquipmentRow.fxml
>
> │ │ │ │ ├── KpiCard.fxml
>
> │ │ │ │ └── PriorityKanbanCard.fxml
>
> │ │ │ ├── Ai.fxml
>
> │ │ │ ├── Auth.fxml
>
> │ │ │ ├── Dashboard.fxml
>
> │ │ │ ├── Equipment.fxml
>
> │ │ │ ├── History.fxml
>
> │ │ │ ├── Main.fxml
>
> │ │ │ ├── Priorities.fxml
>
> │ │ │ ├── Sidebar.fxml
>
> │ │ │ └── Topbar.fxml
>
> │ │ └── application.properties
>
> │ └── test/
>
> │ └── java/
>
> │ └── fr/
>
> │ └── renewguard/
>
> │ ├── api/
>
> │ │ └── ApiClientTest.java
>
> │ └── viewmodel/
>
> │ ├── AuthViewModelTest.java
>
> │ ├── DashboardViewModelTest.java
>
> │ └── EquipmentViewModelTest.java
>
> └── pom.xml

**3. Conseils et recommandations**

**3.1 Etat du projet --- resolu depuis la derniere version**

- Coherence des packages : fr.renewguard.service retenu partout (l\'ancien fr.renewguard.api.service a ete elimine).

- AuthResponseDto : version complete a 6 champs (accessToken, tokenType, expiresIn, username, siteName, role) conservee.

- Les 4 ecrans restants (Equipment, Priorities, Ai, History) sont maintenant integralement code : controllers Java ecrits manuellement (alignes sur les ViewModels), vues FXML verifiees fx:id par fx:id contre ces controllers.

- Main.fxml (coquille racine BorderPane) ajoute --- il manquait alors que MainController.java existait deja.

- Les 3 fichiers CSS (theme-dark.css, base.css, components.css) sont complets et couvrent toutes les classes utilisees dans le projet (verifie par extraction automatique des styleClass FXML + des getStyleClass().add(\...) Java).

**3.2 Bug corrige : ordre de chargement des feuilles de style**

base.css contient un reset generique .button (fond transparent, padding 0). Comme tout Button JavaFX porte automatiquement la classe CSS button en plus de ses classes personnalisees (btn-primary, chip, sidebar-item\...), un chargement dans le mauvais ordre aurait fait gagner le reset generique sur les styles specifiques (meme specificite CSS a une classe, donc c\'est l\'ordre de chargement qui tranche). SceneManager.java charge desormais : base.css -\> theme-dark.css -\> components.css (resets d\'abord, styles specifiques ensuite) --- convention standard evitant ce piege.

**3.3 Icônes et assets visuels**

Le dossier assets/icons/ ne peut pas être généré par une IA texte-à-texte : ce sont des fichiers binaires. Le projet utilise pour l\'instant des caracteres Unicode/texte comme substituts temporaires dans les FXML deja integres. Deux options pour la suite :

- Utiliser une police d\'icônes vectorielles comme Ikonli (déjà compatible JavaFX, aucun asset à gérer) --- recommandé.

- Exporter manuellement les icônes depuis Figma au format SVG/PNG et les placer dans src/main/resources/fr/renewguard/assets/icons/.

**3.4 Prochaine etape immediate : application.properties + AppConfig.java**

Ce sont les 2 seuls fichiers manquants avant une premiere compilation complete (mvn compile). Ils sont purs Java/texte, sans dimension visuelle --- a ecrire directement, pas besoin de Figma Make (voir Section 15 pour le detail).

**3.5 Tests unitaires**

Les 4 classes de test prévues dans l\'architecture (DashboardViewModelTest, EquipmentViewModelTest, AuthViewModelTest, ApiClientTest) doivent utiliser JUnit 5 + Mockito pour mocker ApiClient et vérifier le comportement des ViewModels sans appel réseau réel. Dépendances déjà incluses dans le pom.xml. A ecrire en dernier, une fois le code stabilise (pas avant, sinon tests a reecrire a chaque changement de logique).

**3.6 Sécurité**

- Ne jamais committer application-local.properties (destiné aux overrides d\'environnement, notamment l\'URL de l\'API en production).

- Le JWT est stocké via java.util.prefs.Preferences en clair --- pour un usage en production sensible, envisager un chiffrement supplémentaire (ex. via un Keystore Java) avant persistance disque.

- Ajoutez un intercepteur de logs désactivable (logging.api.requests=false par défaut dans application.properties) pour éviter de journaliser des données énergétiques sensibles en production.

**3.7 Prochaines étapes suggérées**

- 1\. Ecrire application.properties + AppConfig.java (Section 15).

- 2\. Compiler le projet avec mvn compile pour détecter les éventuelles erreurs restantes (imports ControlsFX ToggleSwitch notamment, a verifier dans le pom.xml).

- 3\. Démarrer un service FastAPI de test (mock) exposant les endpoints attendus (/energy/snapshot, /equipment, etc.) pour valider la couche réseau de bout en bout.

- 4\. Lancer mvn javafx:run et verifier visuellement chaque ecran (dark mode, boutons, TableView, Drag&Drop des priorites, graphiques).

- 5\. Charger les polices Inter/JetBrains Mono via Font.loadFont() dans RenewGuardApp (le CSS seul ne peut pas charger de polices custom en JavaFX).

- 6\. Ajouter les tests unitaires une fois l\'application stable.

- 7\. Remplacer les icônes texte temporaires par Ikonli pour un rendu professionnel homogène sur toutes les plateformes.

**4. Configuration Maven**

**pom.xml**

> \<?xml version=\"1.0\" encoding=\"UTF-8\"?\>
>
> \<project xmlns=\"http://maven.apache.org/POM/4.0.0\"
>
> xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"
>
> xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"\>
>
> \<modelVersion\>4.0.0\</modelVersion\>
>
> \<groupId\>fr.renewguard\</groupId\>
>
> \<artifactId\>renewguard-ai\</artifactId\>
>
> \<version\>1.0.0\</version\>
>
> \<packaging\>jar\</packaging\>
>
> \<properties\>
>
> \<maven.compiler.source\>21\</maven.compiler.source\>
>
> \<maven.compiler.target\>21\</maven.compiler.target\>
>
> \<project.build.sourceEncoding\>UTF-8\</project.build.sourceEncoding\>
>
> \<javafx.version\>21\</javafx.version\>
>
> \<main.class\>fr.renewguard.RenewGuardApp\</main.class\>
>
> \</properties\>
>
> \<dependencies\>
>
> \<!\-- JavaFX \--\>
>
> \<dependency\>
>
> \<groupId\>org.openjfx\</groupId\>
>
> \<artifactId\>javafx-controls\</artifactId\>
>
> \<version\>\${javafx.version}\</version\>
>
> \</dependency\>
>
> \<dependency\>
>
> \<groupId\>org.openjfx\</groupId\>
>
> \<artifactId\>javafx-fxml\</artifactId\>
>
> \<version\>\${javafx.version}\</version\>
>
> \</dependency\>
>
> \<dependency\>
>
> \<groupId\>org.openjfx\</groupId\>
>
> \<artifactId\>javafx-web\</artifactId\>
>
> \<version\>\${javafx.version}\</version\>
>
> \</dependency\>
>
> \<!\-- HTTP client \--\>
>
> \<dependency\>
>
> \<groupId\>com.squareup.okhttp3\</groupId\>
>
> \<artifactId\>okhttp\</artifactId\>
>
> \<version\>4.12.0\</version\>
>
> \</dependency\>
>
> \<!\-- JSON \--\>
>
> \<dependency\>
>
> \<groupId\>com.fasterxml.jackson.core\</groupId\>
>
> \<artifactId\>jackson-databind\</artifactId\>
>
> \<version\>2.17.1\</version\>
>
> \</dependency\>
>
> \<dependency\>
>
> \<groupId\>com.fasterxml.jackson.datatype\</groupId\>
>
> \<artifactId\>jackson-datatype-jsr310\</artifactId\>
>
> \<version\>2.17.1\</version\>
>
> \</dependency\>
>
> \<!\-- Composants UI avancés \--\>
>
> \<dependency\>
>
> \<groupId\>org.controlsfx\</groupId\>
>
> \<artifactId\>controlsfx\</artifactId\>
>
> \<version\>11.2.1\</version\>
>
> \</dependency\>
>
> \<dependency\>
>
> \<groupId\>eu.hansolo.fx\</groupId\>
>
> \<artifactId\>charts\</artifactId\>
>
> \<version\>21.0.1\</version\>
>
> \</dependency\>
>
> \<!\-- Logs \--\>
>
> \<dependency\>
>
> \<groupId\>org.slf4j\</groupId\>
>
> \<artifactId\>slf4j-api\</artifactId\>
>
> \<version\>2.0.13\</version\>
>
> \</dependency\>
>
> \<dependency\>
>
> \<groupId\>ch.qos.logback\</groupId\>
>
> \<artifactId\>logback-classic\</artifactId\>
>
> \<version\>1.5.6\</version\>
>
> \</dependency\>
>
> \<!\-- Tests \--\>
>
> \<dependency\>
>
> \<groupId\>org.junit.jupiter\</groupId\>
>
> \<artifactId\>junit-jupiter\</artifactId\>
>
> \<version\>5.10.2\</version\>
>
> \<scope\>test\</scope\>
>
> \</dependency\>
>
> \<dependency\>
>
> \<groupId\>org.mockito\</groupId\>
>
> \<artifactId\>mockito-core\</artifactId\>
>
> \<version\>5.11.0\</version\>
>
> \<scope\>test\</scope\>
>
> \</dependency\>
>
> \</dependencies\>
>
> \<build\>
>
> \<plugins\>
>
> \<plugin\>
>
> \<groupId\>org.openjfx\</groupId\>
>
> \<artifactId\>javafx-maven-plugin\</artifactId\>
>
> \<version\>0.0.8\</version\>
>
> \<configuration\>
>
> \<mainClass\>\${main.class}\</mainClass\>
>
> \</configuration\>
>
> \</plugin\>
>
> \<plugin\>
>
> \<groupId\>org.apache.maven.plugins\</groupId\>
>
> \<artifactId\>maven-shade-plugin\</artifactId\>
>
> \<version\>3.5.3\</version\>
>
> \<executions\>
>
> \<execution\>
>
> \<phase\>package\</phase\>
>
> \<goals\>\<goal\>shade\</goal\>\</goals\>
>
> \<configuration\>
>
> \<transformers\>
>
> \<transformer implementation=\"org.apache.maven.plugins.shade.resource.ManifestResourceTransformer\"\>
>
> \<mainClass\>\${main.class}\</mainClass\>
>
> \</transformer\>
>
> \</transformers\>
>
> \</configuration\>
>
> \</execution\>
>
> \</executions\>
>
> \</plugin\>
>
> \</plugins\>
>
> \</build\>
>
> \</project\>

**src/main/java/module-info.java**

> module fr.renewguard {
>
> requires javafx.controls;
>
> requires javafx.fxml;
>
> requires javafx.web;
>
> requires okhttp3;
>
> requires com.fasterxml.jackson.databind;
>
> requires com.fasterxml.jackson.datatype.jsr310;
>
> requires org.controlsfx.controls;
>
> requires eu.hansolo.fx.charts;
>
> requires org.slf4j;
>
> requires java.prefs;
>
> opens fr.renewguard to javafx.fxml;
>
> opens fr.renewguard.controller to javafx.fxml;
>
> opens fr.renewguard.component to javafx.fxml;
>
> opens fr.renewguard.model.dto to com.fasterxml.jackson.databind;
>
> opens fr.renewguard.model.enums to com.fasterxml.jackson.databind;
>
> exports fr.renewguard;
>
> exports fr.renewguard.viewmodel;
>
> exports fr.renewguard.viewmodel.shared;
>
> }

**5. Point d\'entrée et navigation**

**src/main/java/fr/renewguard/RenewGuardApp.java**

> package fr.renewguard;
>
> import fr.renewguard.navigation.SceneManager;
>
> import fr.renewguard.util.PollingScheduler;
>
> import fr.renewguard.util.TokenStore;
>
> import javafx.application.Application;
>
> import javafx.stage.Stage;
>
> public class RenewGuardApp extends Application {
>
> \@Override
>
> public void start(Stage stage) {
>
> SceneManager.init(stage);
>
> if (TokenStore.getInstance().get() != null) {
>
> SceneManager.navigate(\"main\");
>
> } else {
>
> SceneManager.navigate(\"auth\");
>
> }
>
> }
>
> \@Override
>
> public void stop() {
>
> PollingScheduler.getInstance().shutdown();
>
> }
>
> public static void main(String\[\] args) {
>
> launch(args);
>
> }
>
> }

**src/main/java/fr/renewguard/navigation/SceneManager.java**

> package fr.renewguard.navigation;
>
> import fr.renewguard.util.FxmlLoader;
>
> import javafx.scene.Parent;
>
> import javafx.scene.Scene;
>
> import javafx.stage.Stage;
>
> import java.util.HashMap;
>
> import java.util.Map;
>
> import java.util.Objects;
>
> public class SceneManager {
>
> private static Stage primaryStage;
>
> private static Scene rootScene;
>
> private static final Map\<String, String\> ROUTES = new HashMap\<\>();
>
> static {
>
> ROUTES.put(\"auth\", \"/fr/renewguard/fxml/Auth.fxml\");
>
> ROUTES.put(\"main\", \"/fr/renewguard/fxml/Main.fxml\");
>
> ROUTES.put(\"dashboard\", \"/fr/renewguard/fxml/Dashboard.fxml\");
>
> ROUTES.put(\"equipment\", \"/fr/renewguard/fxml/Equipment.fxml\");
>
> ROUTES.put(\"priorities\", \"/fr/renewguard/fxml/Priorities.fxml\");
>
> ROUTES.put(\"ai\", \"/fr/renewguard/fxml/Ai.fxml\");
>
> ROUTES.put(\"history\", \"/fr/renewguard/fxml/History.fxml\");
>
> }
>
> public static void init(Stage stage) {
>
> primaryStage = stage;
>
> primaryStage.setTitle(\"RenewGuard AI\");
>
> primaryStage.setWidth(1280);
>
> primaryStage.setHeight(800);
>
> primaryStage.setMinWidth(1024);
>
> primaryStage.setMinHeight(640);
>
> primaryStage.setResizable(true);
>
> }
>
> public static void navigate(String route) {
>
> String path = ROUTES.get(route);
>
> if (path == null) throw new IllegalArgumentException(\"Unknown route: \" + route);
>
> Parent root = FxmlLoader.load(path);
>
> if (rootScene == null) {
>
> rootScene = new Scene(root);
>
> applyStylesheets(rootScene);
>
> primaryStage.setScene(rootScene);
>
> primaryStage.show();
>
> } else {
>
> rootScene.setRoot(root);
>
> }
>
> }
>
> private static void applyStylesheets(Scene scene) {
>
> // Ordre important : base.css (resets generiques .button/.text-input/\...)
>
> // DOIT etre charge avant theme-dark.css/components.css, sinon les resets
>
> // generiques ecrasent les styles specifiques (meme specificite CSS, donc
>
> // c\'est l\'ordre de chargement qui tranche).
>
> scene.getStylesheets().add(resource(\"/fr/renewguard/css/base.css\"));
>
> scene.getStylesheets().add(resource(\"/fr/renewguard/css/theme-dark.css\"));
>
> scene.getStylesheets().add(resource(\"/fr/renewguard/css/components.css\"));
>
> }
>
> private static String resource(String path) {
>
> return Objects.requireNonNull(SceneManager.class.getResource(path)).toExternalForm();
>
> }
>
> public static Stage getStage() { return primaryStage; }
>
> public static Scene getScene() { return rootScene; }
>
> }

**6. Utilitaires**

**src/main/java/fr/renewguard/util/FxmlLoader.java**

> package fr.renewguard.util;
>
> import javafx.fxml.FXMLLoader;
>
> import javafx.scene.Parent;
>
> import java.io.IOException;
>
> import java.net.URL;
>
> import java.util.Objects;
>
> public final class FxmlLoader {
>
> private FxmlLoader() {}
>
> public static Parent load(String fxmlPath) {
>
> return loadWithResult(fxmlPath).root();
>
> }
>
> public static \<T\> Result\<T\> loadWithResult(String fxmlPath) {
>
> URL url = Objects.requireNonNull(FxmlLoader.class.getResource(fxmlPath), \"FXML not found: \" + fxmlPath);
>
> FXMLLoader loader = new FXMLLoader(url);
>
> try {
>
> Parent root = loader.load();
>
> T controller = loader.getController();
>
> return new Result\<\>(root, controller);
>
> } catch (IOException e) {
>
> throw new RuntimeException(\"Failed to load FXML: \" + fxmlPath, e);
>
> }
>
> }
>
> public static \<T\> Result\<T\> loadWithController(String fxmlPath, T controller) {
>
> URL url = Objects.requireNonNull(FxmlLoader.class.getResource(fxmlPath), \"FXML not found: \" + fxmlPath);
>
> FXMLLoader loader = new FXMLLoader(url);
>
> loader.setController(controller);
>
> try {
>
> Parent root = loader.load();
>
> return new Result\<\>(root, controller);
>
> } catch (IOException e) {
>
> throw new RuntimeException(\"Failed to load FXML: \" + fxmlPath, e);
>
> }
>
> }
>
> public record Result\<T\>(Parent root, T controller) {}
>
> }

**src/main/java/fr/renewguard/util/NumberFormatter.java**

> package fr.renewguard.util;
>
> import java.util.Locale;
>
> public final class NumberFormatter {
>
> private static final Locale FR = Locale.FRANCE;
>
> private NumberFormatter() {}
>
> public static String formatKw(double kw) { return String.format(FR, \"%.1f\", kw); }
>
> public static String formatKwh(double kwh) {
>
> if (kwh \>= 1000) return String.format(FR, \"%.1f MWh\", kwh / 1000.0);
>
> return String.format(FR, \"%.0f kWh\", kwh);
>
> }
>
> public static String formatWatts(int watts) {
>
> if (watts \>= 1000) return String.format(FR, \"%.1f kW\", watts / 1000.0);
>
> return watts + \" W\";
>
> }
>
> public static String formatVoltage(double volts) { return String.format(FR, \"%.0f V\", volts); }
>
> public static String formatHz(double hz) { return String.format(FR, \"%.2f Hz\", hz); }
>
> public static String formatPercent(int percent) { return percent + \" %\"; }
>
> public static String formatPercentDouble(double percent) { return String.format(FR, \"%.1f %%\", percent); }
>
> public static String formatCo2Kg(double kg) {
>
> if (kg \>= 1000) return String.format(FR, \"%.2f t CO2\", kg / 1000.0);
>
> return String.format(FR, \"%.1f kg CO2\", kg);
>
> }
>
> public static String formatCurrency(double eur) { return String.format(FR, \"%.2f EUR\", eur); }
>
> public static String formatCurrencyRounded(double eur) { return String.format(FR, \"%.0f EUR\", eur); }
>
> public static String formatEta(long totalMinutes) {
>
> if (totalMinutes \<= 0) return \"-\";
>
> long h = totalMinutes / 60;
>
> long m = totalMinutes % 60;
>
> if (h == 0) return m + \"min\";
>
> return h + \"h \" + String.format(\"%02d\", m) + \"min\";
>
> }
>
> public static String formatTimestamp(java.time.LocalDateTime dt) {
>
> if (dt == null) return \"-\";
>
> return dt.format(java.time.format.DateTimeFormatter.ofPattern(\"HH:mm - dd/MM\"));
>
> }
>
> public static String formatTimeOnly(java.time.LocalDateTime dt) {
>
> if (dt == null) return \"-\";
>
> return dt.format(java.time.format.DateTimeFormatter.ofPattern(\"HH:mm\"));
>
> }
>
> }

**src/main/java/fr/renewguard/util/PollingScheduler.java**

> package fr.renewguard.util;
>
> import javafx.application.Platform;
>
> import java.util.concurrent.\*;
>
> import java.util.concurrent.atomic.AtomicInteger;
>
> public final class PollingScheduler {
>
> private static final PollingScheduler INSTANCE = new PollingScheduler();
>
> private final ScheduledExecutorService executor;
>
> private final AtomicInteger threadIndex = new AtomicInteger(0);
>
> private PollingScheduler() {
>
> executor = Executors.newScheduledThreadPool(3, r -\> {
>
> Thread t = new Thread(r, \"rg-poll-\" + threadIndex.getAndIncrement());
>
> t.setDaemon(true);
>
> t.setPriority(Thread.MIN_PRIORITY);
>
> return t;
>
> });
>
> }
>
> public static PollingScheduler getInstance() { return INSTANCE; }
>
> public Handle schedule(Runnable fxTask, long intervalSeconds) {
>
> return schedule(fxTask, intervalSeconds, intervalSeconds);
>
> }
>
> public Handle schedule(Runnable fxTask, long initialDelay, long intervalSeconds) {
>
> ScheduledFuture\<?\> future = executor.scheduleAtFixedRate(() -\> {
>
> try { Platform.runLater(fxTask); }
>
> catch (Exception e) { System.err.println(\"\[PollingScheduler\] Task error: \" + e.getMessage()); }
>
> }, initialDelay, intervalSeconds, TimeUnit.SECONDS);
>
> return new FutureHandle(future);
>
> }
>
> public Handle scheduleOnce(Runnable fxTask, long delaySeconds) {
>
> ScheduledFuture\<?\> future = executor.schedule(() -\> Platform.runLater(fxTask), delaySeconds, TimeUnit.SECONDS);
>
> return new FutureHandle(future);
>
> }
>
> public void shutdown() {
>
> executor.shutdownNow();
>
> try { executor.awaitTermination(2, TimeUnit.SECONDS); }
>
> catch (InterruptedException e) { Thread.currentThread().interrupt(); }
>
> }
>
> public interface Handle { void cancel(); boolean isCancelled(); }
>
> private record FutureHandle(ScheduledFuture\<?\> future) implements Handle {
>
> \@Override public void cancel() { future.cancel(false); }
>
> \@Override public boolean isCancelled() { return future.isCancelled(); }
>
> }
>
> public static Handle compose(Handle\... handles) {
>
> return new Handle() {
>
> \@Override public void cancel() { for (Handle h : handles) h.cancel(); }
>
> \@Override public boolean isCancelled() { for (Handle h : handles) if (!h.isCancelled()) return false; return true; }
>
> };
>
> }
>
> }

**src/main/java/fr/renewguard/util/TokenStore.java**

> package fr.renewguard.util;
>
> import java.util.prefs.Preferences;
>
> public final class TokenStore {
>
> private static final TokenStore INSTANCE = new TokenStore();
>
> private static final String PREF_KEY = \"rg_jwt\";
>
> private final Preferences prefs = Preferences.userNodeForPackage(TokenStore.class);
>
> private String memToken;
>
> private TokenStore() { memToken = prefs.get(PREF_KEY, null); }
>
> public static TokenStore getInstance() { return INSTANCE; }
>
> public void save(String token, boolean persist) {
>
> memToken = token;
>
> if (persist) prefs.put(PREF_KEY, token);
>
> else prefs.remove(PREF_KEY);
>
> }
>
> public String get() { return memToken; }
>
> public void clear() { memToken = null; prefs.remove(PREF_KEY); }
>
> }

**7. Modèles de données --- Enums**

**src/main/java/fr/renewguard/model/enums/AlertSeverity.java**

> package fr.renewguard.model.enums;
>
> import com.fasterxml.jackson.annotation.JsonProperty;
>
> public enum AlertSeverity {
>
> \@JsonProperty(\"CRITICAL\") CRITICAL,
>
> \@JsonProperty(\"WARNING\") WARNING,
>
> \@JsonProperty(\"INFO\") INFO;
>
> public String displayLabel() {
>
> return switch (this) {
>
> case CRITICAL -\> \"Critique\";
>
> case WARNING -\> \"Attention\";
>
> case INFO -\> \"Information\";
>
> };
>
> }
>
> public String dotColor() {
>
> return switch (this) {
>
> case CRITICAL -\> \"#FF4D4D\";
>
> case WARNING -\> \"#FFA53E\";
>
> case INFO -\> \"#3B9DFF\";
>
> };
>
> }
>
> public String cssClass() {
>
> return switch (this) {
>
> case CRITICAL -\> \"alert-critical\";
>
> case WARNING -\> \"alert-warning\";
>
> case INFO -\> \"alert-info\";
>
> };
>
> }
>
> }

**src/main/java/fr/renewguard/model/enums/EquipmentStatus.java**

> package fr.renewguard.model.enums;
>
> import com.fasterxml.jackson.annotation.JsonProperty;
>
> public enum EquipmentStatus {
>
> \@JsonProperty(\"ON\") ON,
>
> \@JsonProperty(\"OFF\") OFF,
>
> \@JsonProperty(\"AI_OFF\") AI_OFF,
>
> \@JsonProperty(\"OFFLINE\") OFFLINE;
>
> public boolean isOperational() { return this == ON; }
>
> public String displayLabel() {
>
> return switch (this) {
>
> case ON -\> \"Actif\";
>
> case OFF -\> \"Inactif\";
>
> case AI_OFF -\> \"Off (IA)\";
>
> case OFFLINE -\> \"Hors ligne\";
>
> };
>
> }
>
> public String cssClass() {
>
> return switch (this) {
>
> case ON -\> \"status-on\";
>
> case OFF -\> \"status-off\";
>
> case AI_OFF -\> \"status-ai-off\";
>
> case OFFLINE -\> \"status-offline\";
>
> };
>
> }
>
> public String dotColor() {
>
> return switch (this) {
>
> case ON -\> \"#22D3A5\";
>
> case OFF -\> \"#8B93A7\";
>
> case AI_OFF -\> \"#3B9DFF\";
>
> case OFFLINE -\> \"#FF4D4D\";
>
> };
>
> }
>
> }

**src/main/java/fr/renewguard/model/enums/PriorityLevel.java**

> package fr.renewguard.model.enums;
>
> import com.fasterxml.jackson.annotation.JsonProperty;
>
> public enum PriorityLevel {
>
> \@JsonProperty(\"CRITICAL\") CRITICAL(1),
>
> \@JsonProperty(\"IMPORTANT\") IMPORTANT(2),
>
> \@JsonProperty(\"LOW\") LOW(3);
>
> private final int rank;
>
> PriorityLevel(int rank) { this.rank = rank; }
>
> public int getRank() { return rank; }
>
> public String displayLabel() {
>
> return switch (this) {
>
> case CRITICAL -\> \"Critique\";
>
> case IMPORTANT -\> \"Important\";
>
> case LOW -\> \"Non prioritaire\";
>
> };
>
> }
>
> public String badgeCssClass() {
>
> return switch (this) {
>
> case CRITICAL -\> \"priority-critical\";
>
> case IMPORTANT -\> \"priority-important\";
>
> case LOW -\> \"priority-low\";
>
> };
>
> }
>
> public String accentColor() {
>
> return switch (this) {
>
> case CRITICAL -\> \"#FF4D4D\";
>
> case IMPORTANT -\> \"#FFA53E\";
>
> case LOW -\> \"#8B93A7\";
>
> };
>
> }
>
> public static PriorityLevel fromRank(int rank) {
>
> for (PriorityLevel p : values()) if (p.rank == rank) return p;
>
> throw new IllegalArgumentException(\"No PriorityLevel for rank: \" + rank);
>
> }
>
> }

**8. Modèles de données --- DTOs**

**src/main/java/fr/renewguard/model/dto/AiDecisionDto.java**

> package fr.renewguard.model.dto;
>
> import com.fasterxml.jackson.annotation.JsonProperty;
>
> import java.time.LocalDateTime;
>
> public class AiDecisionDto {
>
> \@JsonProperty(\"id\") private long id;
>
> \@JsonProperty(\"action\") private String action;
>
> \@JsonProperty(\"equipment_name\") private String equipmentName;
>
> \@JsonProperty(\"equipment_id\") private Long equipmentId;
>
> \@JsonProperty(\"reason\") private String reason;
>
> \@JsonProperty(\"impact_label\") private String impactLabel;
>
> \@JsonProperty(\"confidence_percent\") private int confidencePercent;
>
> \@JsonProperty(\"status\") private String status;
>
> \@JsonProperty(\"timestamp\") private LocalDateTime timestamp;
>
> \@JsonProperty(\"acknowledged\") private boolean acknowledged;
>
> public long getId() { return id; }
>
> public String getAction() { return action; }
>
> public String getEquipmentName() { return equipmentName; }
>
> public Long getEquipmentId() { return equipmentId; }
>
> public String getReason() { return reason; }
>
> public String getImpactLabel() { return impactLabel; }
>
> public int getConfidencePercent() { return confidencePercent; }
>
> public String getStatus() { return status; }
>
> public LocalDateTime getTimestamp() { return timestamp; }
>
> public boolean isAcknowledged() { return acknowledged; }
>
> public void setAcknowledged(boolean v) { this.acknowledged = v; }
>
> public boolean isPending() { return \"PENDING\".equalsIgnoreCase(status); }
>
> public boolean isDone() { return \"DONE\".equalsIgnoreCase(status); }
>
> }

**src/main/java/fr/renewguard/model/dto/AlertDto.java**

> package fr.renewguard.model.dto;
>
> import com.fasterxml.jackson.annotation.JsonProperty;
>
> import fr.renewguard.model.enums.AlertSeverity;
>
> import java.time.LocalDateTime;
>
> public class AlertDto {
>
> \@JsonProperty(\"id\") private long id;
>
> \@JsonProperty(\"severity\") private AlertSeverity severity;
>
> \@JsonProperty(\"title\") private String title;
>
> \@JsonProperty(\"description\") private String description;
>
> \@JsonProperty(\"timestamp\") private LocalDateTime timestamp;
>
> \@JsonProperty(\"resolved\") private boolean resolved;
>
> \@JsonProperty(\"resolved_at\") private LocalDateTime resolvedAt;
>
> \@JsonProperty(\"suggested_action\") private String suggestedAction;
>
> public long getId() { return id; }
>
> public AlertSeverity getSeverity() { return severity; }
>
> public String getTitle() { return title; }
>
> public String getDescription() { return description; }
>
> public LocalDateTime getTimestamp() { return timestamp; }
>
> public boolean isResolved() { return resolved; }
>
> public LocalDateTime getResolvedAt() { return resolvedAt; }
>
> public String getSuggestedAction() { return suggestedAction; }
>
> public void setResolved(boolean resolved) { this.resolved = resolved; }
>
> public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
>
> }

**src/main/java/fr/renewguard/model/dto/AuthResponseDto.java**

> package fr.renewguard.model.dto;
>
> import com.fasterxml.jackson.annotation.JsonProperty;
>
> public class AuthResponseDto {
>
> \@JsonProperty(\"access_token\") private String accessToken;
>
> \@JsonProperty(\"token_type\") private String tokenType;
>
> \@JsonProperty(\"expires_in\") private int expiresIn;
>
> \@JsonProperty(\"username\") private String username;
>
> \@JsonProperty(\"site_name\") private String siteName;
>
> \@JsonProperty(\"role\") private String role;
>
> public String getAccessToken() { return accessToken; }
>
> public String getTokenType() { return tokenType; }
>
> public int getExpiresIn() { return expiresIn; }
>
> public String getUsername() { return username; }
>
> public String getSiteName() { return siteName; }
>
> public String getRole() { return role; }
>
> }

**src/main/java/fr/renewguard/model/dto/ChatResponseDto.java**

> package fr.renewguard.model.dto;
>
> import com.fasterxml.jackson.annotation.JsonProperty;
>
> import java.time.LocalDateTime;
>
> public class ChatResponseDto {
>
> \@JsonProperty(\"message\") private String message;
>
> \@JsonProperty(\"timestamp\") private LocalDateTime timestamp;
>
> \@JsonProperty(\"model_version\") private String modelVersion;
>
> \@JsonProperty(\"tokens_used\") private int tokensUsed;
>
> public String getMessage() { return message; }
>
> public LocalDateTime getTimestamp() { return timestamp; }
>
> public String getModelVersion() { return modelVersion; }
>
> public int getTokensUsed() { return tokensUsed; }
>
> }

**src/main/java/fr/renewguard/model/dto/EnergySnapshotDto.java**

> package fr.renewguard.model.dto;
>
> import com.fasterxml.jackson.annotation.JsonProperty;
>
> public class EnergySnapshotDto {
>
> \@JsonProperty(\"solar_production\") private double solarProduction;
>
> \@JsonProperty(\"consumption\") private double consumption;
>
> \@JsonProperty(\"battery_percent\") private int batteryPercent;
>
> \@JsonProperty(\"battery_eta\") private String batteryEta;
>
> \@JsonProperty(\"battery_charging\") private boolean batteryCharging;
>
> \@JsonProperty(\"battery_charge_rate_kw\") private double batteryChargeRateKw;
>
> \@JsonProperty(\"grid_available\") private boolean gridAvailable;
>
> \@JsonProperty(\"grid_voltage\") private double gridVoltage;
>
> \@JsonProperty(\"grid_frequency\") private double gridFrequency;
>
> \@JsonProperty(\"grid_import_kw\") private double gridImport;
>
> \@JsonProperty(\"ai_score\") private int aiScore;
>
> \@JsonProperty(\"active_equipment_count\") private int activeEquipmentCount;
>
> \@JsonProperty(\"total_equipment_count\") private int totalEquipmentCount;
>
> \@JsonProperty(\"energy_mix\") private EnergyMixDto energyMix;
>
> \@JsonProperty(\"energy_saved_kwh\") private double energySaved;
>
> \@JsonProperty(\"co2_avoided_kg\") private double co2Avoided;
>
> \@JsonProperty(\"last_ai_decision\") private AiDecisionDto lastAiDecision;
>
> public double getSolarProduction() { return solarProduction; }
>
> public double getConsumption() { return consumption; }
>
> public int getBatteryPercent() { return batteryPercent; }
>
> public String getBatteryEta() { return batteryEta; }
>
> public boolean isBatteryCharging() { return batteryCharging; }
>
> public double getBatteryChargeRateKw() { return batteryChargeRateKw; }
>
> public boolean isGridAvailable() { return gridAvailable; }
>
> public double getGridVoltage() { return gridVoltage; }
>
> public double getGridFrequency() { return gridFrequency; }
>
> public double getGridImport() { return gridImport; }
>
> public int getAiScore() { return aiScore; }
>
> public int getActiveEquipmentCount() { return activeEquipmentCount; }
>
> public int getTotalEquipmentCount() { return totalEquipmentCount; }
>
> public EnergyMixDto getEnergyMix() { return energyMix; }
>
> public double getEnergySaved() { return energySaved; }
>
> public double getCo2Avoided() { return co2Avoided; }
>
> public AiDecisionDto getLastAiDecision() { return lastAiDecision; }
>
> public static class EnergyMixDto {
>
> \@JsonProperty(\"solar_percent\") private double solarPercent;
>
> \@JsonProperty(\"battery_percent\") private double batteryPercent;
>
> \@JsonProperty(\"grid_percent\") private double gridPercent;
>
> public double getSolarPercent() { return solarPercent; }
>
> public double getBatteryPercent() { return batteryPercent; }
>
> public double getGridPercent() { return gridPercent; }
>
> }
>
> }

**src/main/java/fr/renewguard/model/dto/EquipmentDto.java**

> package fr.renewguard.model.dto;
>
> import com.fasterxml.jackson.annotation.JsonProperty;
>
> import fr.renewguard.model.enums.EquipmentStatus;
>
> import fr.renewguard.model.enums.PriorityLevel;
>
> public class EquipmentDto {
>
> \@JsonProperty(\"id\") private long id;
>
> \@JsonProperty(\"name\") private String name;
>
> \@JsonProperty(\"location\") private String location;
>
> \@JsonProperty(\"icon\") private String icon;
>
> \@JsonProperty(\"priority\") private PriorityLevel priority;
>
> \@JsonProperty(\"status\") private EquipmentStatus status;
>
> \@JsonProperty(\"power_watts\") private int powerWatts;
>
> \@JsonProperty(\"last_activity\") private String lastActivity;
>
> \@JsonProperty(\"ai_managed\") private boolean aiManaged;
>
> public long getId() { return id; }
>
> public String getName() { return name; }
>
> public String getLocation() { return location; }
>
> public String getIcon() { return icon; }
>
> public PriorityLevel getPriority() { return priority; }
>
> public EquipmentStatus getStatus() { return status; }
>
> public int getPowerWatts() { return powerWatts; }
>
> public String getLastActivity() { return lastActivity; }
>
> public boolean isAiManaged() { return aiManaged; }
>
> public void setId(long id) { this.id = id; }
>
> public void setName(String name) { this.name = name; }
>
> public void setLocation(String location) { this.location = location; }
>
> public void setIcon(String icon) { this.icon = icon; }
>
> public void setPriority(PriorityLevel priority) { this.priority = priority; }
>
> public void setStatus(EquipmentStatus status) { this.status = status; }
>
> public void setPowerWatts(int watts) { this.powerWatts = watts; }
>
> public void setLastActivity(String v) { this.lastActivity = v; }
>
> public void setAiManaged(boolean v) { this.aiManaged = v; }
>
> }

**src/main/java/fr/renewguard/model/dto/HistoryPointDto.java**

> package fr.renewguard.model.dto;
>
> import com.fasterxml.jackson.annotation.JsonProperty;
>
> public class HistoryPointDto {
>
> \@JsonProperty(\"label\") private String label;
>
> \@JsonProperty(\"production_kwh\") private double production;
>
> \@JsonProperty(\"consumption_kwh\") private double consumption;
>
> \@JsonProperty(\"saved_kwh\") private double saved;
>
> \@JsonProperty(\"co2_kg\") private double co2;
>
> \@JsonProperty(\"grid_import_kwh\") private double gridImport;
>
> \@JsonProperty(\"grid_export_kwh\") private double gridExport;
>
> public String getLabel() { return label; }
>
> public double getProduction() { return production; }
>
> public double getConsumption() { return consumption; }
>
> public double getSaved() { return saved; }
>
> public double getCo2() { return co2; }
>
> public double getGridImport() { return gridImport; }
>
> public double getGridExport() { return gridExport; }
>
> }

**src/main/java/fr/renewguard/model/dto/HistorySummaryDto.java**

> package fr.renewguard.model.dto;
>
> import com.fasterxml.jackson.annotation.JsonProperty;
>
> public class HistorySummaryDto {
>
> \@JsonProperty(\"total_saved_kwh\") private double totalSavedKwh;
>
> \@JsonProperty(\"total_co2_kg\") private double totalCo2Kg;
>
> \@JsonProperty(\"financial_savings_eur\") private double financialSavingsEur;
>
> \@JsonProperty(\"solar_coverage_percent\") private double solarCoveragePercent;
>
> \@JsonProperty(\"grid_export_eur\") private double gridExportEur;
>
> \@JsonProperty(\"grid_import_cost_eur\") private double gridImportCostEur;
>
> \@JsonProperty(\"net_benefit_eur\") private double netBenefitEur;
>
> \@JsonProperty(\"monthly_projection_eur\") private double monthlyProjectionEur;
>
> public double getTotalSavedKwh() { return totalSavedKwh; }
>
> public double getTotalCo2Kg() { return totalCo2Kg; }
>
> public double getFinancialSavingsEur() { return financialSavingsEur; }
>
> public double getSolarCoveragePercent() { return solarCoveragePercent; }
>
> public double getGridExportEur() { return gridExportEur; }
>
> public double getGridImportCostEur() { return gridImportCostEur; }
>
> public double getNetBenefitEur() { return netBenefitEur; }
>
> public double getMonthlyProjectionEur() { return monthlyProjectionEur; }
>
> }

**src/main/java/fr/renewguard/model/dto/PredictionDto.java**

> package fr.renewguard.model.dto;
>
> import com.fasterxml.jackson.annotation.JsonProperty;
>
> import java.util.List;
>
> public class PredictionDto {
>
> \@JsonProperty(\"consumption_curve\") private List\<PredictionPoint\> consumptionCurve;
>
> \@JsonProperty(\"production_curve\") private List\<PredictionPoint\> productionCurve;
>
> \@JsonProperty(\"battery_forecast_label\") private String batteryForecastLabel;
>
> \@JsonProperty(\"solar_forecast_label\") private String solarForecastLabel;
>
> \@JsonProperty(\"autonomy_forecast_label\") private String autonomyForecastLabel;
>
> \@JsonProperty(\"consumption_forecast_kw\") private double consumptionForecastKw;
>
> \@JsonProperty(\"production_forecast_kw\") private double productionForecastKw;
>
> \@JsonProperty(\"battery_forecast_percent\") private int batteryForecastPercent;
>
> public List\<PredictionPoint\> getConsumptionCurve() { return consumptionCurve; }
>
> public List\<PredictionPoint\> getProductionCurve() { return productionCurve; }
>
> public String getBatteryForecastLabel() { return batteryForecastLabel; }
>
> public String getSolarForecastLabel() { return solarForecastLabel; }
>
> public String getAutonomyForecastLabel() { return autonomyForecastLabel; }
>
> public double getConsumptionForecastKw() { return consumptionForecastKw; }
>
> public double getProductionForecastKw() { return productionForecastKw; }
>
> public int getBatteryForecastPercent() { return batteryForecastPercent; }
>
> public static class PredictionPoint {
>
> \@JsonProperty(\"label\") private String label;
>
> \@JsonProperty(\"real\") private Double real;
>
> \@JsonProperty(\"predicted\") private Double predicted;
>
> \@JsonProperty(\"lower_bound\") private Double lowerBound;
>
> \@JsonProperty(\"upper_bound\") private Double upperBound;
>
> public String getLabel() { return label; }
>
> public Double getReal() { return real; }
>
> public Double getPredicted() { return predicted; }
>
> public Double getLowerBound() { return lowerBound; }
>
> public Double getUpperBound() { return upperBound; }
>
> public boolean hasReal() { return real != null; }
>
> public boolean hasPredicted() { return predicted != null; }
>
> }
>
> }

**src/main/java/fr/renewguard/model/dto/PriorityLevelDto.java**

> package fr.renewguard.model.dto;
>
> import com.fasterxml.jackson.annotation.JsonProperty;
>
> import fr.renewguard.model.enums.PriorityLevel;
>
> import java.util.List;
>
> public class PriorityLevelDto {
>
> \@JsonProperty(\"level\") private int level;
>
> \@JsonProperty(\"priority\") private PriorityLevel priority;
>
> \@JsonProperty(\"label\") private String label;
>
> \@JsonProperty(\"description\") private String description;
>
> \@JsonProperty(\"equipment_ids\") private List\<Long\> equipmentIds;
>
> \@JsonProperty(\"total_power_watts\") private int totalPowerWatts;
>
> public int getLevel() { return level; }
>
> public PriorityLevel getPriority() { return priority; }
>
> public String getLabel() { return label; }
>
> public String getDescription() { return description; }
>
> public List\<Long\> getEquipmentIds() { return equipmentIds; }
>
> public int getTotalPowerWatts() { return totalPowerWatts; }
>
> public void setLevel(int level) { this.level = level; }
>
> public void setPriority(PriorityLevel priority) { this.priority = priority; }
>
> public void setLabel(String label) { this.label = label; }
>
> public void setDescription(String description) { this.description = description; }
>
> public void setEquipmentIds(List\<Long\> ids) { this.equipmentIds = ids; }
>
> public void setTotalPowerWatts(int watts) { this.totalPowerWatts = watts; }
>
> }

**src/main/java/fr/renewguard/model/dto/RuleDto.java**

> package fr.renewguard.model.dto;
>
> import com.fasterxml.jackson.annotation.JsonProperty;
>
> public class RuleDto {
>
> \@JsonProperty(\"id\") private long id;
>
> \@JsonProperty(\"condition\") private String condition;
>
> \@JsonProperty(\"action\") private String action;
>
> \@JsonProperty(\"active\") private boolean active;
>
> \@JsonProperty(\"priority_level\") private int priorityLevel;
>
> \@JsonProperty(\"threshold_value\") private double thresholdValue;
>
> \@JsonProperty(\"threshold_unit\") private String thresholdUnit;
>
> public long getId() { return id; }
>
> public String getCondition() { return condition; }
>
> public String getAction() { return action; }
>
> public boolean isActive() { return active; }
>
> public int getPriorityLevel() { return priorityLevel; }
>
> public double getThresholdValue() { return thresholdValue; }
>
> public String getThresholdUnit() { return thresholdUnit; }
>
> public void setActive(boolean active) { this.active = active; }
>
> }

**9. Couche API --- Services**

**src/main/java/fr/renewguard/service/AiService.java**

> package fr.renewguard.service;
>
> import com.fasterxml.jackson.core.type.TypeReference;
>
> import fr.renewguard.model.dto.AiDecisionDto;
>
> import fr.renewguard.model.dto.ChatResponseDto;
>
> import fr.renewguard.model.dto.PredictionDto;
>
> import java.util.List;
>
> import java.util.Map;
>
> import java.util.concurrent.CompletableFuture;
>
> public final class AiService {
>
> private static final AiService INSTANCE = new AiService();
>
> private final ApiClient api = ApiClient.getInstance();
>
> private AiService() {}
>
> public static AiService getInstance() { return INSTANCE; }
>
> public CompletableFuture\<List\<AiDecisionDto\>\> getDecisions() {
>
> return api.get(\"/ai/decisions\", new TypeReference\<List\<AiDecisionDto\>\>() {});
>
> }
>
> public CompletableFuture\<List\<AiDecisionDto\>\> getDecisions(int limit) {
>
> return api.get(\"/ai/decisions?limit=\" + limit, new TypeReference\<List\<AiDecisionDto\>\>() {});
>
> }
>
> public CompletableFuture\<PredictionDto\> getPredictions() {
>
> return api.get(\"/ai/predictions\", PredictionDto.class);
>
> }
>
> public CompletableFuture\<ChatResponseDto\> sendMessage(String message) {
>
> Map\<String, String\> body = Map.of(\"message\", message);
>
> return api.post(\"/ai/chat\", body, ChatResponseDto.class);
>
> }
>
> public CompletableFuture\<Void\> acknowledgeDecision(long id) {
>
> return api.patch(\"/ai/decisions/\" + id + \"/acknowledge\", Map.of(), Void.class).thenApply(v -\> null);
>
> }
>
> }

**src/main/java/fr/renewguard/service/ApiClient.java**

> package fr.renewguard.service;
>
> import com.fasterxml.jackson.core.type.TypeReference;
>
> import com.fasterxml.jackson.databind.DeserializationFeature;
>
> import com.fasterxml.jackson.databind.ObjectMapper;
>
> import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
>
> import fr.renewguard.util.TokenStore;
>
> import okhttp3.\*;
>
> import java.io.IOException;
>
> import java.util.concurrent.CompletableFuture;
>
> public final class ApiClient {
>
> private static final ApiClient INSTANCE = new ApiClient();
>
> private final OkHttpClient http;
>
> private final ObjectMapper mapper;
>
> private String baseUrl;
>
> private static final MediaType JSON = MediaType.get(\"application/json; charset=utf-8\");
>
> private ApiClient() {
>
> this.baseUrl = System.getProperty(\"api.base-url\", \"http://localhost:8000\");
>
> this.mapper = new ObjectMapper()
>
> .registerModule(new JavaTimeModule())
>
> .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
>
> this.http = new OkHttpClient.Builder()
>
> .connectTimeout(5, java.util.concurrent.TimeUnit.SECONDS)
>
> .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
>
> .writeTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
>
> .addInterceptor(new AuthInterceptor())
>
> .build();
>
> }
>
> public static ApiClient getInstance() { return INSTANCE; }
>
> public void setBaseUrl(String url) { this.baseUrl = url; }
>
> public \<T\> CompletableFuture\<T\> get(String path, Class\<T\> type) {
>
> Request req = new Request.Builder().url(baseUrl + path).get().build();
>
> return execute(req, body -\> mapper.readValue(body, type));
>
> }
>
> public \<T\> CompletableFuture\<T\> get(String path, TypeReference\<T\> typeRef) {
>
> Request req = new Request.Builder().url(baseUrl + path).get().build();
>
> return execute(req, body -\> mapper.readValue(body, typeRef));
>
> }
>
> public \<T\> CompletableFuture\<T\> post(String path, Object body, Class\<T\> type) {
>
> Request req = new Request.Builder().url(baseUrl + path).post(toRequestBody(body)).build();
>
> return execute(req, raw -\> mapper.readValue(raw, type));
>
> }
>
> public \<T\> CompletableFuture\<T\> patch(String path, Object body, Class\<T\> type) {
>
> Request req = new Request.Builder().url(baseUrl + path).patch(toRequestBody(body)).build();
>
> return execute(req, raw -\> mapper.readValue(raw, type));
>
> }
>
> public \<T\> CompletableFuture\<T\> put(String path, Object body, Class\<T\> type) {
>
> Request req = new Request.Builder().url(baseUrl + path).put(toRequestBody(body)).build();
>
> return execute(req, raw -\> mapper.readValue(raw, type));
>
> }
>
> public \<T\> CompletableFuture\<T\> put(String path, Object body, TypeReference\<T\> typeRef) {
>
> Request req = new Request.Builder().url(baseUrl + path).put(toRequestBody(body)).build();
>
> return execute(req, raw -\> mapper.readValue(raw, typeRef));
>
> }
>
> public CompletableFuture\<Void\> delete(String path) {
>
> Request req = new Request.Builder().url(baseUrl + path).delete().build();
>
> return execute(req, raw -\> null);
>
> }
>
> private \<T\> CompletableFuture\<T\> execute(Request request, ResponseMapper\<T\> mapper) {
>
> CompletableFuture\<T\> future = new CompletableFuture\<\>();
>
> http.newCall(request).enqueue(new Callback() {
>
> \@Override
>
> public void onFailure(Call call, IOException e) {
>
> future.completeExceptionally(e);
>
> }
>
> \@Override
>
> public void onResponse(Call call, Response response) {
>
> try (ResponseBody rb = response.body()) {
>
> handleHttpError(response.code(), request.url().encodedPath());
>
> String rawBody = rb != null ? rb.string() : \"{}\";
>
> future.complete(mapper.map(rawBody));
>
> } catch (Exception e) {
>
> future.completeExceptionally(e);
>
> }
>
> }
>
> });
>
> return future;
>
> }
>
> private void handleHttpError(int code, String path) {
>
> if (code == 401) {
>
> TokenStore.getInstance().clear();
>
> javafx.application.Platform.runLater(
>
> () -\> fr.renewguard.navigation.SceneManager.navigate(\"auth\")
>
> );
>
> throw new ApiException(401, \"Unauthorized --- session expiree\");
>
> }
>
> if (code == 403) throw new ApiException(403, \"Acces refuse : \" + path);
>
> if (code == 404) throw new ApiException(404, \"Ressource introuvable : \" + path);
>
> if (code == 422) throw new ApiException(422, \"Donnees invalides envoyees a : \" + path);
>
> if (code \>= 500) throw new ApiException(code, \"Erreur serveur sur : \" + path);
>
> }
>
> private RequestBody toRequestBody(Object body) {
>
> try {
>
> return RequestBody.create(mapper.writeValueAsString(body), JSON);
>
> } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
>
> throw new RuntimeException(\"Serialization error\", e);
>
> }
>
> }
>
> \@FunctionalInterface
>
> private interface ResponseMapper\<T\> { T map(String body) throws IOException; }
>
> public static class ApiException extends RuntimeException {
>
> private final int statusCode;
>
> public ApiException(int code, String message) { super(message); this.statusCode = code; }
>
> public int getStatusCode() { return statusCode; }
>
> }
>
> }

**src/main/java/fr/renewguard/service/AuthInterceptor.java**

> package fr.renewguard.service;
>
> import fr.renewguard.util.TokenStore;
>
> import okhttp3.Interceptor;
>
> import okhttp3.Request;
>
> import okhttp3.Response;
>
> import java.io.IOException;
>
> public final class AuthInterceptor implements Interceptor {
>
> \@Override
>
> public Response intercept(Chain chain) throws IOException {
>
> String token = TokenStore.getInstance().get();
>
> Request original = chain.request();
>
> if (token == null \|\| token.isBlank()) return chain.proceed(original);
>
> Request authenticated = original.newBuilder()
>
> .header(\"Authorization\", \"Bearer \" + token)
>
> .header(\"Accept\", \"application/json\")
>
> .build();
>
> return chain.proceed(authenticated);
>
> }
>
> }

**src/main/java/fr/renewguard/service/EnergyService.java**

> package fr.renewguard.service;
>
> import com.fasterxml.jackson.core.type.TypeReference;
>
> import fr.renewguard.model.dto.EnergySnapshotDto;
>
> import fr.renewguard.model.dto.HistoryPointDto;
>
> import java.util.List;
>
> import java.util.concurrent.CompletableFuture;
>
> public final class EnergyService {
>
> private static final EnergyService INSTANCE = new EnergyService();
>
> private final ApiClient api = ApiClient.getInstance();
>
> private EnergyService() {}
>
> public static EnergyService getInstance() { return INSTANCE; }
>
> public CompletableFuture\<EnergySnapshotDto\> getSnapshot() {
>
> return api.get(\"/energy/snapshot\", EnergySnapshotDto.class);
>
> }
>
> public CompletableFuture\<List\<HistoryPointDto\>\> getHistory(String period) {
>
> return api.get(\"/energy/history?period=\" + period, new TypeReference\<List\<HistoryPointDto\>\>() {});
>
> }
>
> public CompletableFuture\<EnergySnapshotDto\> refreshSnapshot() {
>
> return api.post(\"/energy/snapshot/refresh\", null, EnergySnapshotDto.class);
>
> }
>
> }

**src/main/java/fr/renewguard/service/EquipmentService.java**

> package fr.renewguard.service;
>
> import com.fasterxml.jackson.core.type.TypeReference;
>
> import fr.renewguard.model.dto.EquipmentDto;
>
> import fr.renewguard.model.enums.EquipmentStatus;
>
> import java.util.List;
>
> import java.util.Map;
>
> import java.util.concurrent.CompletableFuture;
>
> public final class EquipmentService {
>
> private static final EquipmentService INSTANCE = new EquipmentService();
>
> private final ApiClient api = ApiClient.getInstance();
>
> private EquipmentService() {}
>
> public static EquipmentService getInstance() { return INSTANCE; }
>
> public CompletableFuture\<List\<EquipmentDto\>\> getAll() {
>
> return api.get(\"/equipment\", new TypeReference\<List\<EquipmentDto\>\>() {});
>
> }
>
> public CompletableFuture\<EquipmentDto\> getById(long id) {
>
> return api.get(\"/equipment/\" + id, EquipmentDto.class);
>
> }
>
> public CompletableFuture\<EquipmentDto\> setStatus(long id, EquipmentStatus status) {
>
> Map\<String, String\> body = Map.of(\"status\", status.name());
>
> return api.patch(\"/equipment/\" + id + \"/status\", body, EquipmentDto.class);
>
> }
>
> public CompletableFuture\<EquipmentDto\> create(EquipmentDto dto) {
>
> return api.post(\"/equipment\", dto, EquipmentDto.class);
>
> }
>
> public CompletableFuture\<EquipmentDto\> update(long id, EquipmentDto dto) {
>
> return api.put(\"/equipment/\" + id, dto, EquipmentDto.class);
>
> }
>
> public CompletableFuture\<Void\> delete(long id) {
>
> return api.delete(\"/equipment/\" + id);
>
> }
>
> }

**src/main/java/fr/renewguard/service/HistoryService.java**

> package fr.renewguard.service;
>
> import com.fasterxml.jackson.core.type.TypeReference;
>
> import fr.renewguard.model.dto.AlertDto;
>
> import fr.renewguard.model.dto.HistoryPointDto;
>
> import fr.renewguard.model.dto.HistorySummaryDto;
>
> import java.util.List;
>
> import java.util.concurrent.CompletableFuture;
>
> public final class HistoryService {
>
> private static final HistoryService INSTANCE = new HistoryService();
>
> private final ApiClient api = ApiClient.getInstance();
>
> private HistoryService() {}
>
> public static HistoryService getInstance() { return INSTANCE; }
>
> public CompletableFuture\<List\<HistoryPointDto\>\> getHistory(String period) {
>
> return api.get(\"/history?period=\" + period, new TypeReference\<List\<HistoryPointDto\>\>() {});
>
> }
>
> public CompletableFuture\<List\<HistoryPointDto\>\> getHistory(String from, String to) {
>
> return api.get(\"/history?from=\" + from + \"&to=\" + to, new TypeReference\<List\<HistoryPointDto\>\>() {});
>
> }
>
> public CompletableFuture\<HistorySummaryDto\> getSummary(String period) {
>
> return api.get(\"/history/summary?period=\" + period, HistorySummaryDto.class);
>
> }
>
> public CompletableFuture\<List\<AlertDto\>\> getAlerts() {
>
> return api.get(\"/alerts\", new TypeReference\<List\<AlertDto\>\>() {});
>
> }
>
> public CompletableFuture\<List\<AlertDto\>\> getAlerts(String severity) {
>
> return api.get(\"/alerts?severity=\" + severity, new TypeReference\<List\<AlertDto\>\>() {});
>
> }
>
> public CompletableFuture\<AlertDto\> resolveAlert(long id) {
>
> return api.post(\"/alerts/\" + id + \"/resolve\", null, AlertDto.class);
>
> }
>
> public CompletableFuture\<byte\[\]\> exportReport(String period, String format) {
>
> return api.get(\"/history/export?period=\" + period + \"&format=\" + format, new TypeReference\<byte\[\]\>() {});
>
> }
>
> }

**src/main/java/fr/renewguard/service/PriorityService.java**

> package fr.renewguard.service;
>
> import com.fasterxml.jackson.core.type.TypeReference;
>
> import fr.renewguard.model.dto.PriorityLevelDto;
>
> import fr.renewguard.model.dto.RuleDto;
>
> import java.util.List;
>
> import java.util.concurrent.CompletableFuture;
>
> public final class PriorityService {
>
> private static final PriorityService INSTANCE = new PriorityService();
>
> private final ApiClient api = ApiClient.getInstance();
>
> private PriorityService() {}
>
> public static PriorityService getInstance() { return INSTANCE; }
>
> public CompletableFuture\<List\<PriorityLevelDto\>\> getPriorities() {
>
> return api.get(\"/priorities\", new TypeReference\<List\<PriorityLevelDto\>\>() {});
>
> }
>
> public CompletableFuture\<List\<PriorityLevelDto\>\> savePriorities(List\<PriorityLevelDto\> levels) {
>
> return api.put(\"/priorities\", levels, new TypeReference\<List\<PriorityLevelDto\>\>() {});
>
> }
>
> public CompletableFuture\<List\<RuleDto\>\> getRules() {
>
> return api.get(\"/rules\", new TypeReference\<List\<RuleDto\>\>() {});
>
> }
>
> public CompletableFuture\<List\<RuleDto\>\> saveRules(List\<RuleDto\> rules) {
>
> return api.put(\"/rules\", rules, new TypeReference\<List\<RuleDto\>\>() {});
>
> }
>
> public CompletableFuture\<RuleDto\> toggleRule(long id, boolean active) {
>
> return api.patch(\"/rules/\" + id, new ToggleBody(active), RuleDto.class);
>
> }
>
> private record ToggleBody(boolean active) {}
>
> }

**10. ViewModels**

**src/main/java/fr/renewguard/viewmodel/AiViewModel.java**

> package fr.renewguard.viewmodel;
>
> import fr.renewguard.model.dto.AiDecisionDto;
>
> import fr.renewguard.model.dto.PredictionDto;
>
> import fr.renewguard.service.AiService;
>
> import javafx.application.Platform;
>
> import javafx.beans.property.\*;
>
> import javafx.collections.FXCollections;
>
> import javafx.collections.ObservableList;
>
> import java.time.LocalDateTime;
>
> import java.util.function.Consumer;
>
> public class AiViewModel {
>
> private final AiService service = AiService.getInstance();
>
> private final ObservableList\<PredictionDto\> predictions = FXCollections.observableArrayList();
>
> private final ObservableList\<AiDecisionDto\> decisions = FXCollections.observableArrayList();
>
> public record ChatMessage(String text, boolean isUser, LocalDateTime timestamp) {}
>
> private final ObservableList\<ChatMessage\> chatMessages = FXCollections.observableArrayList();
>
> private final StringProperty chatInput = new SimpleStringProperty(\"\");
>
> private final BooleanProperty chatLoading = new SimpleBooleanProperty(false);
>
> private final IntegerProperty totalDecisions = new SimpleIntegerProperty(0);
>
> private final IntegerProperty pendingDecisions = new SimpleIntegerProperty(0);
>
> private Consumer\<ChatMessage\> onNewMessage;
>
> public void refresh() { refreshPredictions(); refreshDecisions(); }
>
> public void refreshPredictions() {
>
> service.getPredictions().thenAcceptAsync(p -\> predictions.setAll(p), Platform::runLater)
>
> .exceptionally(ex -\> null);
>
> }
>
> public void refreshDecisions() {
>
> service.getDecisions(20).thenAcceptAsync(list -\> {
>
> decisions.setAll(list);
>
> totalDecisions.set(list.size());
>
> pendingDecisions.set((int) list.stream().filter(AiDecisionDto::isPending).count());
>
> }, Platform::runLater).exceptionally(ex -\> null);
>
> }
>
> public void sendMessage() {
>
> String text = chatInput.get();
>
> if (text == null \|\| text.isBlank()) return;
>
> ChatMessage userMsg = new ChatMessage(text.trim(), true, LocalDateTime.now());
>
> chatMessages.add(userMsg);
>
> if (onNewMessage != null) onNewMessage.accept(userMsg);
>
> chatInput.set(\"\"); chatLoading.set(true);
>
> service.sendMessage(text.trim()).thenAcceptAsync(response -\> {
>
> ChatMessage aiMsg = new ChatMessage(response.getMessage(), false,
>
> response.getTimestamp() != null ? response.getTimestamp() : LocalDateTime.now());
>
> chatMessages.add(aiMsg);
>
> if (onNewMessage != null) onNewMessage.accept(aiMsg);
>
> chatLoading.set(false);
>
> }, Platform::runLater).exceptionally(ex -\> { Platform.runLater(() -\> chatLoading.set(false)); return null; });
>
> }
>
> public void acknowledgeDecision(long id) {
>
> service.acknowledgeDecision(id).thenRunAsync(() -\> decisions.stream()
>
> .filter(d -\> d.getId() == id).findFirst().ifPresent(d -\> d.setAcknowledged(true)), Platform::runLater)
>
> .exceptionally(ex -\> null);
>
> }
>
> public ObservableList\<PredictionDto\> getPredictions() { return predictions; }
>
> public ObservableList\<AiDecisionDto\> getDecisions() { return decisions; }
>
> public ObservableList\<ChatMessage\> getChatMessages() { return chatMessages; }
>
> public StringProperty chatInputProperty() { return chatInput; }
>
> public BooleanProperty chatLoadingProperty() { return chatLoading; }
>
> public IntegerProperty totalDecisionsProperty() { return totalDecisions; }
>
> public IntegerProperty pendingDecisionsProperty() { return pendingDecisions; }
>
> public void setOnNewMessage(Consumer\<ChatMessage\> callback) { this.onNewMessage = callback; }
>
> }

**src/main/java/fr/renewguard/viewmodel/AuthViewModel.java**

> package fr.renewguard.viewmodel;
>
> import fr.renewguard.model.dto.AuthResponseDto;
>
> import fr.renewguard.service.ApiClient;
>
> import fr.renewguard.viewmodel.shared.SessionViewModel;
>
> import javafx.application.Platform;
>
> import javafx.beans.property.\*;
>
> public class AuthViewModel {
>
> private final StringProperty email = new SimpleStringProperty(\"\");
>
> private final StringProperty password = new SimpleStringProperty(\"\");
>
> private final BooleanProperty rememberMe = new SimpleBooleanProperty(true);
>
> private final BooleanProperty loading = new SimpleBooleanProperty(false);
>
> private final StringProperty errorMessage = new SimpleStringProperty(null);
>
> private Runnable onSuccess;
>
> public void setOnSuccess(Runnable callback) { this.onSuccess = callback; }
>
> public void login() {
>
> if (email.get().isBlank() \|\| password.get().isBlank()) {
>
> errorMessage.set(\"Veuillez renseigner tous les champs.\");
>
> return;
>
> }
>
> errorMessage.set(null);
>
> loading.set(true);
>
> var body = new LoginRequest(email.get(), password.get());
>
> ApiClient.getInstance().post(\"/auth/token\", body, AuthResponseDto.class)
>
> .thenAcceptAsync(this::handleSuccess, Platform::runLater)
>
> .exceptionally(ex -\> {
>
> Platform.runLater(() -\> {
>
> loading.set(false);
>
> errorMessage.set(\"Identifiants incorrects ou serveur indisponible.\");
>
> });
>
> return null;
>
> });
>
> }
>
> private void handleSuccess(AuthResponseDto dto) {
>
> SessionViewModel.getInstance().authenticate(
>
> dto.getAccessToken(), dto.getUsername(), dto.getSiteName(), rememberMe.get());
>
> loading.set(false);
>
> if (onSuccess != null) onSuccess.run();
>
> }
>
> public String getEmail() { return email.get(); }
>
> public StringProperty emailProperty() { return email; }
>
> public String getPassword() { return password.get(); }
>
> public StringProperty passwordProperty() { return password; }
>
> public boolean isRememberMe() { return rememberMe.get(); }
>
> public BooleanProperty rememberMeProperty() { return rememberMe; }
>
> public boolean isLoading() { return loading.get(); }
>
> public BooleanProperty loadingProperty() { return loading; }
>
> public String getErrorMessage() { return errorMessage.get(); }
>
> public StringProperty errorMessageProperty() { return errorMessage; }
>
> public record LoginRequest(String username, String password) {}
>
> }

**src/main/java/fr/renewguard/viewmodel/DashboardViewModel.java**

> package fr.renewguard.viewmodel;
>
> import fr.renewguard.model.dto.AiDecisionDto;
>
> import fr.renewguard.model.dto.EnergySnapshotDto;
>
> import fr.renewguard.model.dto.HistoryPointDto;
>
> import fr.renewguard.api.service.AiService;
>
> import fr.renewguard.api.service.EnergyService;
>
> import fr.renewguard.util.PollingScheduler;
>
> import javafx.application.Platform;
>
> import javafx.beans.property.\*;
>
> import javafx.collections.FXCollections;
>
> import javafx.collections.ObservableList;
>
> import java.util.List;
>
> public class DashboardViewModel {
>
> // ── Services ─────────────────────────────────────────────────────
>
> private final EnergyService energyService = EnergyService.getInstance();
>
> private final AiService aiService = AiService.getInstance();
>
> // ── Snapshot properties ──────────────────────────────────────────
>
> private final DoubleProperty solarProduction = new SimpleDoubleProperty(0);
>
> private final DoubleProperty consumption = new SimpleDoubleProperty(0);
>
> private final IntegerProperty aiScore = new SimpleIntegerProperty(0);
>
> private final StringProperty equipmentCount = new SimpleStringProperty(\"---\");
>
> private final IntegerProperty batteryPercent = new SimpleIntegerProperty(0);
>
> private final StringProperty batteryEta = new SimpleStringProperty(\"---\");
>
> private final BooleanProperty batteryCharging = new SimpleBooleanProperty(false);
>
> private final DoubleProperty batteryChargeRate = new SimpleDoubleProperty(0);
>
> private final BooleanProperty gridAvailable = new SimpleBooleanProperty(true);
>
> private final DoubleProperty gridVoltage = new SimpleDoubleProperty(0);
>
> private final DoubleProperty gridFrequency = new SimpleDoubleProperty(0);
>
> private final DoubleProperty gridImport = new SimpleDoubleProperty(0);
>
> private final DoubleProperty energySaved = new SimpleDoubleProperty(0);
>
> private final DoubleProperty co2Avoided = new SimpleDoubleProperty(0);
>
> private final DoubleProperty solarMix = new SimpleDoubleProperty(0);
>
> private final DoubleProperty batteryMix = new SimpleDoubleProperty(0);
>
> private final DoubleProperty gridMix = new SimpleDoubleProperty(0);
>
> // ── AI decision ──────────────────────────────────────────────────
>
> private final ObjectProperty\<AiDecisionDto\> lastDecision =
>
> new SimpleObjectProperty\<\>(null);
>
> // ── Chart data ───────────────────────────────────────────────────
>
> private final ObservableList\<HistoryPointDto\> chartData =
>
> FXCollections.observableArrayList();
>
> private final StringProperty selectedPeriod = new SimpleStringProperty(\"day\");
>
> // ── System view ──────────────────────────────────────────────────
>
> private final BooleanProperty systemViewVisible = new SimpleBooleanProperty(false);
>
> // ── State ────────────────────────────────────────────────────────
>
> private final BooleanProperty loading = new SimpleBooleanProperty(false);
>
> private final StringProperty error = new SimpleStringProperty(null);
>
> private PollingScheduler.Handle snapshotHandle;
>
> private PollingScheduler.Handle aiHandle;
>
> // ── Init / dispose ───────────────────────────────────────────────
>
> public void startPolling() {
>
> refreshSnapshot();
>
> refreshChart();
>
> refreshLastDecision();
>
> snapshotHandle = PollingScheduler.getInstance()
>
> .schedule(this::refreshSnapshot, 30);
>
> aiHandle = PollingScheduler.getInstance()
>
> .schedule(this::refreshLastDecision, 60);
>
> }
>
> public void stopPolling() {
>
> if (snapshotHandle != null) snapshotHandle.cancel();
>
> if (aiHandle != null) aiHandle.cancel();
>
> }
>
> // ── Refresh methods ──────────────────────────────────────────────
>
> public void refreshSnapshot() {
>
> energyService.getSnapshot()
>
> .thenAcceptAsync(this::applySnapshot, Platform::runLater)
>
> .exceptionally(ex -\> {
>
> Platform.runLater(() -\> error.set(ex.getMessage()));
>
> return null;
>
> });
>
> }
>
> public void refreshChart() {
>
> energyService.getHistory(selectedPeriod.get())
>
> .thenAcceptAsync(points -\> {
>
> chartData.setAll(points);
>
> }, Platform::runLater)
>
> .exceptionally(ex -\> {
>
> Platform.runLater(() -\> error.set(ex.getMessage()));
>
> return null;
>
> });
>
> }
>
> public void refreshLastDecision() {
>
> aiService.getDecisions(1)
>
> .thenAcceptAsync(list -\> {
>
> if (list != null && !list.isEmpty()) lastDecision.set(list.get(0));
>
> }, Platform::runLater)
>
> .exceptionally(ex -\> null);
>
> }
>
> public void changePeriod(String period) {
>
> selectedPeriod.set(period);
>
> refreshChart();
>
> }
>
> public void toggleSystemView() {
>
> systemViewVisible.set(!systemViewVisible.get());
>
> }
>
> // ── Private applier ──────────────────────────────────────────────
>
> private void applySnapshot(EnergySnapshotDto s) {
>
> solarProduction.set(s.getSolarProduction());
>
> consumption.set(s.getConsumption());
>
> aiScore.set(s.getAiScore());
>
> equipmentCount.set(s.getActiveEquipmentCount() + \" / \" + s.getTotalEquipmentCount());
>
> batteryPercent.set(s.getBatteryPercent());
>
> batteryEta.set(s.getBatteryEta() != null ? s.getBatteryEta() : \"---\");
>
> batteryCharging.set(s.isBatteryCharging());
>
> batteryChargeRate.set(s.getBatteryChargeRateKw());
>
> gridAvailable.set(s.isGridAvailable());
>
> gridVoltage.set(s.getGridVoltage());
>
> gridFrequency.set(s.getGridFrequency());
>
> gridImport.set(s.getGridImport());
>
> energySaved.set(s.getEnergySaved());
>
> co2Avoided.set(s.getCo2Avoided());
>
> if (s.getEnergyMix() != null) {
>
> solarMix.set(s.getEnergyMix().getSolarPercent());
>
> batteryMix.set(s.getEnergyMix().getBatteryPercent());
>
> gridMix.set(s.getEnergyMix().getGridPercent());
>
> }
>
> if (s.getLastAiDecision() != null) lastDecision.set(s.getLastAiDecision());
>
> }
>
> // ── Property accessors ───────────────────────────────────────────
>
> public DoubleProperty solarProductionProperty() { return solarProduction; }
>
> public DoubleProperty consumptionProperty() { return consumption; }
>
> public IntegerProperty aiScoreProperty() { return aiScore; }
>
> public StringProperty equipmentCountProperty() { return equipmentCount; }
>
> public IntegerProperty batteryPercentProperty() { return batteryPercent; }
>
> public StringProperty batteryEtaProperty() { return batteryEta; }
>
> public BooleanProperty batteryChargingProperty() { return batteryCharging; }
>
> public DoubleProperty batteryChargeRateProperty() { return batteryChargeRate; }
>
> public BooleanProperty gridAvailableProperty() { return gridAvailable; }
>
> public DoubleProperty gridVoltageProperty() { return gridVoltage; }
>
> public DoubleProperty gridFrequencyProperty() { return gridFrequency; }
>
> public DoubleProperty gridImportProperty() { return gridImport; }
>
> public DoubleProperty energySavedProperty() { return energySaved; }
>
> public DoubleProperty co2AvoidedProperty() { return co2Avoided; }
>
> public DoubleProperty solarMixProperty() { return solarMix; }
>
> public DoubleProperty batteryMixProperty() { return batteryMix; }
>
> public DoubleProperty gridMixProperty() { return gridMix; }
>
> public ObjectProperty\<AiDecisionDto\> lastDecisionProperty() { return lastDecision; }
>
> public ObservableList\<HistoryPointDto\> getChartData() { return chartData; }
>
> public StringProperty selectedPeriodProperty() { return selectedPeriod; }
>
> public BooleanProperty systemViewVisibleProperty() { return systemViewVisible; }
>
> public BooleanProperty loadingProperty() { return loading; }
>
> public StringProperty errorProperty() { return error; }
>
> public double getSolarProduction() { return solarProduction.get(); }
>
> public double getConsumption() { return consumption.get(); }
>
> public int getAiScore() { return aiScore.get(); }
>
> public int getBatteryPercent() { return batteryPercent.get(); }
>
> public boolean isGridAvailable() { return gridAvailable.get(); }
>
> public double getSolarMix() { return solarMix.get(); }
>
> public double getBatteryMix() { return batteryMix.get(); }
>
> public double getGridMix() { return gridMix.get(); }
>
> }

**src/main/java/fr/renewguard/viewmodel/EquipmentViewModel.java**

> package fr.renewguard.viewmodel;
>
> import fr.renewguard.model.dto.EquipmentDto;
>
> import fr.renewguard.model.enums.EquipmentStatus;
>
> import fr.renewguard.model.enums.PriorityLevel;
>
> import fr.renewguard.service.EquipmentService;
>
> import javafx.application.Platform;
>
> import javafx.beans.property.\*;
>
> import javafx.collections.FXCollections;
>
> import javafx.collections.ObservableList;
>
> import javafx.collections.transformation.FilteredList;
>
> import java.util.function.Predicate;
>
> public class EquipmentViewModel {
>
> private final EquipmentService service = EquipmentService.getInstance();
>
> private final ObservableList\<EquipmentDto\> allEquipments = FXCollections.observableArrayList();
>
> private final FilteredList\<EquipmentDto\> filteredEquipments = new FilteredList\<\>(allEquipments, e -\> true);
>
> private final StringProperty searchQuery = new SimpleStringProperty(\"\");
>
> private final StringProperty activeFilter = new SimpleStringProperty(\"ALL\");
>
> private final BooleanProperty loading = new SimpleBooleanProperty(false);
>
> private final StringProperty errorMessage = new SimpleStringProperty(null);
>
> private final IntegerProperty totalActive = new SimpleIntegerProperty(0);
>
> private final IntegerProperty totalPowerW = new SimpleIntegerProperty(0);
>
> public EquipmentViewModel() {
>
> searchQuery.addListener((o, old, val) -\> applyFilter());
>
> activeFilter.addListener((o, old, val) -\> applyFilter());
>
> }
>
> public void refresh() {
>
> loading.set(true);
>
> service.getAll()
>
> .thenAcceptAsync(list -\> {
>
> allEquipments.setAll(list);
>
> recalcStats();
>
> loading.set(false);
>
> }, Platform::runLater)
>
> .exceptionally(ex -\> { Platform.runLater(() -\> { errorMessage.set(ex.getMessage()); loading.set(false); }); return null; });
>
> }
>
> public void toggleStatus(long id) {
>
> allEquipments.stream().filter(e -\> e.getId() == id).findFirst().ifPresent(eq -\> {
>
> EquipmentStatus next = eq.getStatus() == EquipmentStatus.ON ? EquipmentStatus.OFF : EquipmentStatus.ON;
>
> eq.setStatus(next);
>
> int idx = allEquipments.indexOf(eq);
>
> allEquipments.set(idx, eq);
>
> recalcStats();
>
> service.setStatus(id, next).exceptionally(ex -\> null);
>
> });
>
> }
>
> public void setFilter(String filter) { activeFilter.set(filter); }
>
> private void applyFilter() {
>
> String query = searchQuery.get() == null ? \"\" : searchQuery.get().toLowerCase();
>
> filteredEquipments.setPredicate(buildPredicate(query, activeFilter.get()));
>
> }
>
> private Predicate\<EquipmentDto\> buildPredicate(String query, String filter) {
>
> Predicate\<EquipmentDto\> textMatch = e -\> query.isBlank()
>
> \|\| e.getName().toLowerCase().contains(query)
>
> \|\| (e.getLocation() != null && e.getLocation().toLowerCase().contains(query));
>
> Predicate\<EquipmentDto\> filterMatch = switch (filter) {
>
> case \"CRITICAL\" -\> e -\> e.getPriority() == PriorityLevel.CRITICAL;
>
> case \"IMPORTANT\" -\> e -\> e.getPriority() == PriorityLevel.IMPORTANT;
>
> case \"LOW\" -\> e -\> e.getPriority() == PriorityLevel.LOW;
>
> case \"ACTIVE\" -\> e -\> e.getStatus() == EquipmentStatus.ON;
>
> case \"INACTIVE\" -\> e -\> e.getStatus() != EquipmentStatus.ON;
>
> default -\> e -\> true;
>
> };
>
> return textMatch.and(filterMatch);
>
> }
>
> private void recalcStats() {
>
> int active = (int) allEquipments.stream().filter(e -\> e.getStatus() == EquipmentStatus.ON).count();
>
> int power = allEquipments.stream().filter(e -\> e.getStatus() == EquipmentStatus.ON)
>
> .mapToInt(EquipmentDto::getPowerWatts).sum();
>
> totalActive.set(active); totalPowerW.set(power);
>
> }
>
> public ObservableList\<EquipmentDto\> getFilteredEquipments() { return filteredEquipments; }
>
> public StringProperty searchQueryProperty() { return searchQuery; }
>
> public BooleanProperty loadingProperty() { return loading; }
>
> public IntegerProperty totalActiveProperty() { return totalActive; }
>
> public IntegerProperty totalPowerWProperty() { return totalPowerW; }
>
> public int getTotalCount() { return allEquipments.size(); }
>
> public int getTotalActive() { return totalActive.get(); }
>
> public int getTotalPowerW() { return totalPowerW.get(); }
>
> }

**src/main/java/fr/renewguard/viewmodel/HistoryViewModel.java**

> package fr.renewguard.viewmodel;
>
> import fr.renewguard.model.dto.AlertDto;
>
> import fr.renewguard.model.dto.HistoryPointDto;
>
> import fr.renewguard.model.dto.HistorySummaryDto;
>
> import fr.renewguard.service.HistoryService;
>
> import javafx.application.Platform;
>
> import javafx.beans.property.\*;
>
> import javafx.collections.FXCollections;
>
> import javafx.collections.ObservableList;
>
> public class HistoryViewModel {
>
> private final HistoryService service = HistoryService.getInstance();
>
> private final ObservableList\<HistoryPointDto\> productionData = FXCollections.observableArrayList();
>
> private final ObservableList\<HistoryPointDto\> consumptionData = FXCollections.observableArrayList();
>
> private final ObservableList\<HistoryPointDto\> savingsData = FXCollections.observableArrayList();
>
> private final ObservableList\<HistoryPointDto\> co2Data = FXCollections.observableArrayList();
>
> private final ObjectProperty\<HistorySummaryDto\> summary = new SimpleObjectProperty\<\>(null);
>
> private final ObservableList\<AlertDto\> alerts = FXCollections.observableArrayList();
>
> private final StringProperty selectedPeriod = new SimpleStringProperty(\"week\");
>
> public HistoryViewModel() { selectedPeriod.addListener((o, old, val) -\> refresh()); }
>
> public void refresh() {
>
> String period = selectedPeriod.get();
>
> service.getHistory(period).thenAcceptAsync(points -\> {
>
> productionData.setAll(points); consumptionData.setAll(points);
>
> savingsData.setAll(points); co2Data.setAll(points);
>
> }, Platform::runLater).exceptionally(ex -\> null);
>
> service.getSummary(period).thenAcceptAsync(summary::set, Platform::runLater).exceptionally(ex -\> null);
>
> service.getAlerts().thenAcceptAsync(alerts::setAll, Platform::runLater).exceptionally(ex -\> null);
>
> }
>
> public void resolveAlert(long id) {
>
> service.resolveAlert(id).thenRunAsync(() -\> alerts.removeIf(a -\> a.getId() == id), Platform::runLater)
>
> .exceptionally(ex -\> null);
>
> }
>
> public void setPeriod(String period) { selectedPeriod.set(period); }
>
> public void exportReport(String format) { service.exportReport(selectedPeriod.get(), format).exceptionally(ex -\> null); }
>
> public ObservableList\<HistoryPointDto\> getProductionData() { return productionData; }
>
> public ObservableList\<HistoryPointDto\> getConsumptionData() { return consumptionData; }
>
> public ObservableList\<HistoryPointDto\> getSavingsData() { return savingsData; }
>
> public ObservableList\<HistoryPointDto\> getCo2Data() { return co2Data; }
>
> public ObservableList\<AlertDto\> getAlerts() { return alerts; }
>
> public ObjectProperty\<HistorySummaryDto\> summaryProperty() { return summary; }
>
> }

**src/main/java/fr/renewguard/viewmodel/PrioritiesViewModel.java**

> package fr.renewguard.viewmodel;
>
> import fr.renewguard.model.dto.EquipmentDto;
>
> import fr.renewguard.model.dto.PriorityLevelDto;
>
> import fr.renewguard.model.dto.RuleDto;
>
> import fr.renewguard.model.enums.PriorityLevel;
>
> import fr.renewguard.service.EquipmentService;
>
> import fr.renewguard.service.PriorityService;
>
> import javafx.application.Platform;
>
> import javafx.beans.property.\*;
>
> import javafx.collections.FXCollections;
>
> import javafx.collections.ObservableList;
>
> import java.util.ArrayList;
>
> import java.util.List;
>
> public class PrioritiesViewModel {
>
> private final PriorityService priorityService = PriorityService.getInstance();
>
> private final EquipmentService equipmentService = EquipmentService.getInstance();
>
> private final ObservableList\<EquipmentDto\> level1 = FXCollections.observableArrayList();
>
> private final ObservableList\<EquipmentDto\> level2 = FXCollections.observableArrayList();
>
> private final ObservableList\<EquipmentDto\> level3 = FXCollections.observableArrayList();
>
> private final ObservableList\<RuleDto\> rules = FXCollections.observableArrayList();
>
> private final BooleanProperty modified = new SimpleBooleanProperty(false);
>
> private final BooleanProperty loading = new SimpleBooleanProperty(false);
>
> private final BooleanProperty saved = new SimpleBooleanProperty(false);
>
> private final StringProperty errorMsg = new SimpleStringProperty(null);
>
> public void refresh() {
>
> loading.set(true);
>
> equipmentService.getAll().thenAcceptAsync(items -\> {
>
> level1.clear(); level2.clear(); level3.clear();
>
> for (EquipmentDto e : items) {
>
> if (e.getPriority() == PriorityLevel.CRITICAL) level1.add(e);
>
> else if (e.getPriority() == PriorityLevel.IMPORTANT) level2.add(e);
>
> else level3.add(e);
>
> }
>
> loading.set(false);
>
> }, Platform::runLater).exceptionally(ex -\> { Platform.runLater(() -\> { errorMsg.set(ex.getMessage()); loading.set(false); }); return null; });
>
> priorityService.getRules().thenAcceptAsync(rules::setAll, Platform::runLater).exceptionally(ex -\> null);
>
> }
>
> public void moveEquipment(EquipmentDto equipment, int targetLevel, int insertIndex) {
>
> removeFromAll(equipment);
>
> PriorityLevel newPriority = switch (targetLevel) {
>
> case 1 -\> PriorityLevel.CRITICAL;
>
> case 2 -\> PriorityLevel.IMPORTANT;
>
> default -\> PriorityLevel.LOW;
>
> };
>
> equipment.setPriority(newPriority);
>
> ObservableList\<EquipmentDto\> target = listForLevel(targetLevel);
>
> int idx = Math.min(insertIndex, target.size());
>
> target.add(idx, equipment);
>
> modified.set(true); saved.set(false);
>
> }
>
> private void removeFromAll(EquipmentDto equipment) {
>
> level1.removeIf(e -\> e.getId() == equipment.getId());
>
> level2.removeIf(e -\> e.getId() == equipment.getId());
>
> level3.removeIf(e -\> e.getId() == equipment.getId());
>
> }
>
> private ObservableList\<EquipmentDto\> listForLevel(int level) {
>
> return switch (level) { case 1 -\> level1; case 2 -\> level2; default -\> level3; };
>
> }
>
> public void save() {
>
> List\<PriorityLevelDto\> payload = new ArrayList\<\>();
>
> payload.add(buildLevelDto(1, PriorityLevel.CRITICAL, level1));
>
> payload.add(buildLevelDto(2, PriorityLevel.IMPORTANT, level2));
>
> payload.add(buildLevelDto(3, PriorityLevel.LOW, level3));
>
> loading.set(true);
>
> priorityService.savePriorities(payload).thenAcceptAsync(result -\> {
>
> modified.set(false); saved.set(true); loading.set(false);
>
> }, Platform::runLater).exceptionally(ex -\> { Platform.runLater(() -\> { errorMsg.set(\"Erreur lors de la sauvegarde\"); loading.set(false); }); return null; });
>
> }
>
> public void toggleRule(long id) {
>
> rules.stream().filter(r -\> r.getId() == id).findFirst().ifPresent(rule -\> {
>
> rule.setActive(!rule.isActive());
>
> int idx = rules.indexOf(rule);
>
> rules.set(idx, rule);
>
> priorityService.toggleRule(id, rule.isActive()).exceptionally(ex -\> null);
>
> });
>
> }
>
> private PriorityLevelDto buildLevelDto(int level, PriorityLevel priority, List\<EquipmentDto\> items) {
>
> PriorityLevelDto dto = new PriorityLevelDto();
>
> dto.setLevel(level); dto.setPriority(priority);
>
> dto.setEquipmentIds(items.stream().map(EquipmentDto::getId).toList());
>
> dto.setTotalPowerWatts(items.stream().mapToInt(EquipmentDto::getPowerWatts).sum());
>
> return dto;
>
> }
>
> public ObservableList\<EquipmentDto\> getLevel1() { return level1; }
>
> public ObservableList\<EquipmentDto\> getLevel2() { return level2; }
>
> public ObservableList\<EquipmentDto\> getLevel3() { return level3; }
>
> public ObservableList\<RuleDto\> getRules() { return rules; }
>
> public BooleanProperty modifiedProperty() { return modified; }
>
> public BooleanProperty savedProperty() { return saved; }
>
> }

**src/main/java/fr/renewguard/viewmodel/shared/NotificationViewModel.java**

> package fr.renewguard.viewmodel.shared;
>
> import fr.renewguard.model.dto.AlertDto;
>
> import fr.renewguard.model.enums.AlertSeverity;
>
> import fr.renewguard.service.HistoryService;
>
> import javafx.application.Platform;
>
> import javafx.beans.property.\*;
>
> import javafx.collections.FXCollections;
>
> import javafx.collections.ObservableList;
>
> import javafx.collections.ListChangeListener;
>
> public class NotificationViewModel {
>
> private static NotificationViewModel INSTANCE;
>
> public static NotificationViewModel getInstance() {
>
> if (INSTANCE == null) INSTANCE = new NotificationViewModel();
>
> return INSTANCE;
>
> }
>
> private final HistoryService service = HistoryService.getInstance();
>
> private final ObservableList\<AlertDto\> notifications = FXCollections.observableArrayList();
>
> private final IntegerProperty unreadCount = new SimpleIntegerProperty(0);
>
> private final BooleanProperty hasUnread = new SimpleBooleanProperty(false);
>
> private NotificationViewModel() {
>
> notifications.addListener((ListChangeListener\<AlertDto\>) c -\> {
>
> long count = notifications.stream()
>
> .filter(n -\> !n.isResolved() && n.getSeverity() != AlertSeverity.INFO)
>
> .count();
>
> unreadCount.set((int) count);
>
> hasUnread.set(count \> 0);
>
> });
>
> }
>
> public void fetchAlerts() {
>
> service.getAlerts(AlertSeverity.CRITICAL.name())
>
> .thenAcceptAsync(notifications::setAll, Platform::runLater)
>
> .exceptionally(ex -\> null);
>
> }
>
> public void addLocal(AlertDto alert) { Platform.runLater(() -\> notifications.add(0, alert)); }
>
> public void markAllRead() {
>
> notifications.forEach(n -\> n.setResolved(true));
>
> unreadCount.set(0); hasUnread.set(false);
>
> }
>
> public ObservableList\<AlertDto\> getNotifications() { return notifications; }
>
> public IntegerProperty unreadCountProperty() { return unreadCount; }
>
> public BooleanProperty hasUnreadProperty() { return hasUnread; }
>
> }

**src/main/java/fr/renewguard/viewmodel/shared/SessionViewModel.java**

> package fr.renewguard.viewmodel.shared;
>
> import fr.renewguard.util.TokenStore;
>
> import javafx.beans.property.\*;
>
> public final class SessionViewModel {
>
> private static final SessionViewModel INSTANCE = new SessionViewModel();
>
> private final StringProperty username = new SimpleStringProperty(\"\");
>
> private final StringProperty siteName = new SimpleStringProperty(\"Site principal\");
>
> private final BooleanProperty connected = new SimpleBooleanProperty(false);
>
> private final BooleanProperty emergencyMode = new SimpleBooleanProperty(false);
>
> private final StringProperty activeScreen = new SimpleStringProperty(\"dashboard\");
>
> private SessionViewModel() {}
>
> public static SessionViewModel getInstance() { return INSTANCE; }
>
> public void authenticate(String token, String user, String site, boolean persist) {
>
> TokenStore.getInstance().save(token, persist);
>
> username.set(user);
>
> siteName.set(site);
>
> connected.set(true);
>
> }
>
> public void logout() {
>
> TokenStore.getInstance().clear();
>
> username.set(\"\"); siteName.set(\"\"); connected.set(false);
>
> emergencyMode.set(false); activeScreen.set(\"dashboard\");
>
> }
>
> public void toggleEmergency() { emergencyMode.set(!emergencyMode.get()); }
>
> public String getUsername() { return username.get(); }
>
> public StringProperty usernameProperty() { return username; }
>
> public String getSiteName() { return siteName.get(); }
>
> public StringProperty siteNameProperty() { return siteName; }
>
> public boolean isConnected() { return connected.get(); }
>
> public BooleanProperty connectedProperty() { return connected; }
>
> public boolean isEmergencyMode() { return emergencyMode.get(); }
>
> public void setEmergencyMode(boolean v) { emergencyMode.set(v); }
>
> public BooleanProperty emergencyModeProperty() { return emergencyMode; }
>
> public String getActiveScreen() { return activeScreen.get(); }
>
> public void setActiveScreen(String v) { activeScreen.set(v); }
>
> public StringProperty activeScreenProperty() { return activeScreen; }
>
> }

**11. Contrôleurs (fondation + Dashboard complet)**

**src/main/java/fr/renewguard/controller/AiController.java**

> package fr.renewguard.controller;
>
> import fr.renewguard.component.ChatBubbleController;
>
> import fr.renewguard.model.dto.AiDecisionDto;
>
> import fr.renewguard.model.dto.PredictionDto;
>
> import fr.renewguard.util.FxmlLoader;
>
> import fr.renewguard.util.NumberFormatter;
>
> import fr.renewguard.viewmodel.AiViewModel;
>
> import fr.renewguard.viewmodel.AiViewModel.ChatMessage;
>
> import javafx.collections.ListChangeListener;
>
> import javafx.fxml.FXML;
>
> import javafx.fxml.Initializable;
>
> import javafx.scene.chart.\*;
>
> import javafx.scene.control.Button;
>
> import javafx.scene.control.Label;
>
> import javafx.scene.control.TextField;
>
> import javafx.scene.layout.HBox;
>
> import javafx.scene.layout.VBox;
>
> import java.net.URL;
>
> import java.util.ResourceBundle;
>
> public class AiController implements Initializable {
>
> \@FXML private Label totalDecisionsLabel;
>
> \@FXML private Label pendingDecisionsLabel;
>
> \@FXML private LineChart\<String, Number\> predictionChart;
>
> \@FXML private VBox timelineContainer;
>
> \@FXML private VBox chatMessages;
>
> \@FXML private TextField chatInputField;
>
> \@FXML private Button sendBtn;
>
> \@FXML private HBox typingIndicator;
>
> private final AiViewModel vm = new AiViewModel();
>
> \@Override
>
> public void initialize(URL url, ResourceBundle rb) {
>
> predictionChart.setAnimated(false);
>
> predictionChart.setCreateSymbols(false);
>
> vm.getPredictions().addListener((ListChangeListener\<PredictionDto\>) c -\> refreshChart());
>
> vm.getDecisions().addListener((ListChangeListener\<AiDecisionDto\>) c -\> renderTimeline());
>
> vm.totalDecisionsProperty().addListener((o, old, val) -\> totalDecisionsLabel.setText(val + \" decisions\"));
>
> vm.pendingDecisionsProperty().addListener((o, old, val) -\> pendingDecisionsLabel.setText(val + \" en attente\"));
>
> chatInputField.textProperty().bindBidirectional(vm.chatInputProperty());
>
> chatInputField.setOnAction(e -\> vm.sendMessage());
>
> vm.chatLoadingProperty().addListener((o, old, loading) -\> {
>
> sendBtn.setDisable(loading);
>
> typingIndicator.setVisible(loading);
>
> typingIndicator.setManaged(loading);
>
> });
>
> vm.setOnNewMessage(this::onNewMessage);
>
> vm.refresh();
>
> }
>
> private void refreshChart() {
>
> XYChart.Series\<String, Number\> real = new XYChart.Series\<\>();
>
> real.setName(\"Reel\");
>
> XYChart.Series\<String, Number\> predicted = new XYChart.Series\<\>();
>
> predicted.setName(\"Prediction IA\");
>
> for (PredictionDto p : vm.getPredictions()) {
>
> if (p.getConsumptionCurve() == null) continue;
>
> for (var point : p.getConsumptionCurve()) {
>
> if (point.hasReal()) real.getData().add(new XYChart.Data\<\>(point.getLabel(), point.getReal()));
>
> if (point.hasPredicted()) predicted.getData().add(new XYChart.Data\<\>(point.getLabel(), point.getPredicted()));
>
> }
>
> }
>
> predictionChart.getData().setAll(real, predicted);
>
> }
>
> private void renderTimeline() {
>
> timelineContainer.getChildren().clear();
>
> for (AiDecisionDto d : vm.getDecisions()) {
>
> VBox item = new VBox(4);
>
> item.getStyleClass().add(\"timeline-item\");
>
> item.getChildren().addAll(
>
> new Label(d.getAction()),
>
> new Label(d.getReason()),
>
> new Label(NumberFormatter.formatTimestamp(d.getTimestamp()) + \" - \" + d.getImpactLabel())
>
> );
>
> if (d.isPending() && !d.isAcknowledged()) {
>
> Button ack = new Button(\"Confirmer\");
>
> ack.setOnAction(e -\> vm.acknowledgeDecision(d.getId()));
>
> item.getChildren().add(ack);
>
> }
>
> timelineContainer.getChildren().add(item);
>
> }
>
> }
>
> private void onNewMessage(ChatMessage msg) {
>
> FxmlLoader.Result\<ChatBubbleController\> result =
>
> FxmlLoader.loadWithResult(\"/fr/renewguard/fxml/components/ChatBubble.fxml\");
>
> result.controller().bind(msg.text(), msg.isUser(), msg.timestamp());
>
> chatMessages.getChildren().add(result.root());
>
> }
>
> \@FXML private void onSend() { vm.sendMessage(); }
>
> \@FXML private void onRefresh() { vm.refresh(); }
>
> }

**src/main/java/fr/renewguard/controller/AuthController.java**

> package fr.renewguard.controller;
>
> import fr.renewguard.navigation.SceneManager;
>
> import fr.renewguard.viewmodel.AuthViewModel;
>
> import javafx.beans.binding.Bindings;
>
> import javafx.fxml.FXML;
>
> import javafx.fxml.Initializable;
>
> import javafx.scene.control.\*;
>
> import javafx.scene.layout.HBox;
>
> import java.net.URL;
>
> import java.util.ResourceBundle;
>
> public class AuthController implements Initializable {
>
> \@FXML private TextField emailField;
>
> \@FXML private PasswordField passwordField;
>
> \@FXML private CheckBox rememberMeBox;
>
> \@FXML private Button submitBtn;
>
> \@FXML private Label errorLabel;
>
> \@FXML private HBox errorRow;
>
> \@FXML private Label submitBtnLabel;
>
> private final AuthViewModel vm = new AuthViewModel();
>
> \@Override
>
> public void initialize(URL url, ResourceBundle rb) {
>
> emailField.textProperty().bindBidirectional(vm.emailProperty());
>
> passwordField.textProperty().bindBidirectional(vm.passwordProperty());
>
> rememberMeBox.selectedProperty().bindBidirectional(vm.rememberMeProperty());
>
> submitBtn.disableProperty().bind(vm.loadingProperty());
>
> submitBtnLabel.textProperty().bind(
>
> Bindings.when(vm.loadingProperty()).then(\"Connexion en cours\...\").otherwise(\"Se connecter\"));
>
> errorRow.visibleProperty().bind(vm.errorMessageProperty().isNotNull());
>
> errorRow.managedProperty().bind(vm.errorMessageProperty().isNotNull());
>
> errorLabel.textProperty().bind(vm.errorMessageProperty());
>
> vm.setOnSuccess(() -\> SceneManager.navigate(\"main\"));
>
> passwordField.setOnAction(e -\> vm.login());
>
> emailField.setOnAction(e -\> passwordField.requestFocus());
>
> }
>
> \@FXML private void onSubmit() { vm.login(); }
>
> }

**src/main/java/fr/renewguard/controller/DashboardController.java**

> package fr.renewguard.controller;
>
> import fr.renewguard.component.AiDecisionCardController;
>
> import fr.renewguard.component.BatteryGaugeController;
>
> import fr.renewguard.component.KpiCardController;
>
> import fr.renewguard.model.dto.HistoryPointDto;
>
> import fr.renewguard.util.FxmlLoader;
>
> import fr.renewguard.util.NumberFormatter;
>
> import fr.renewguard.viewmodel.DashboardViewModel;
>
> import javafx.collections.FXCollections;
>
> import javafx.fxml.FXML;
>
> import javafx.fxml.Initializable;
>
> import javafx.scene.chart.\*;
>
> import javafx.scene.control.Button;
>
> import javafx.scene.control.Label;
>
> import javafx.scene.layout.\*;
>
> import javafx.scene.shape.Circle;
>
> import java.net.URL;
>
> import java.util.List;
>
> import java.util.ResourceBundle;
>
> public class DashboardController implements Initializable {
>
> // ── Status bar ───────────────────────────────────────────────────
>
> \@FXML private Circle statusDot;
>
> \@FXML private Label statusBadge;
>
> \@FXML private Button systemViewBtn;
>
> \@FXML private VBox systemViewPanel;
>
> // ── KPI placeholders (GridPane cells) ───────────────────────────
>
> \@FXML private StackPane kpiSolarSlot;
>
> \@FXML private StackPane kpiConsumptionSlot;
>
> \@FXML private StackPane kpiScoreSlot;
>
> \@FXML private StackPane kpiEquipmentSlot;
>
> // ── Battery / Network / AI decision slots ────────────────────────
>
> \@FXML private StackPane batterySlot;
>
> \@FXML private StackPane aiDecisionSlot;
>
> // ── Network card (inline, no sub-component) ──────────────────────
>
> \@FXML private Circle networkDot;
>
> \@FXML private Label networkStatusLabel;
>
> \@FXML private Label networkVoltageLabel;
>
> \@FXML private Label networkFrequencyLabel;
>
> \@FXML private Label networkImportLabel;
>
> // ── Chart ────────────────────────────────────────────────────────
>
> \@FXML private AreaChart\<String, Number\> mainChart;
>
> \@FXML private Button btnDay;
>
> \@FXML private Button btnWeek;
>
> \@FXML private Button btnMonth;
>
> // ── Donut ────────────────────────────────────────────────────────
>
> \@FXML private PieChart pieChart;
>
> \@FXML private Label energySavedLabel;
>
> \@FXML private Label co2AvoidedLabel;
>
> // ── Sub-controllers (injected manually) ──────────────────────────
>
> private KpiCardController kpiSolarCtrl;
>
> private KpiCardController kpiConsumptionCtrl;
>
> private KpiCardController kpiScoreCtrl;
>
> private KpiCardController kpiEquipmentCtrl;
>
> private BatteryGaugeController batteryCtrl;
>
> private AiDecisionCardController aiDecisionCtrl;
>
> // ── ViewModel ────────────────────────────────────────────────────
>
> private final DashboardViewModel vm = new DashboardViewModel();
>
> // ── Init ─────────────────────────────────────────────────────────
>
> \@Override
>
> public void initialize(URL url, ResourceBundle rb) {
>
> injectComponents();
>
> bindViewModel();
>
> configureChart();
>
> configurePeriodButtons(btnDay);
>
> vm.startPolling();
>
> }
>
> public void dispose() {
>
> vm.stopPolling();
>
> }
>
> // ── Component injection ──────────────────────────────────────────
>
> private void injectComponents() {
>
> var solarResult = FxmlLoader.loadWithResult(
>
> \"/fr/renewguard/fxml/components/KpiCard.fxml\");
>
> kpiSolarCtrl = solarResult.controller();
>
> kpiSolarCtrl.configure(\"Production solaire\", \"☀\", \"GREEN\");
>
> kpiSolarSlot.getChildren().setAll(solarResult.root());
>
> var consoResult = FxmlLoader.loadWithResult(
>
> \"/fr/renewguard/fxml/components/KpiCard.fxml\");
>
> kpiConsumptionCtrl = consoResult.controller();
>
> kpiConsumptionCtrl.configure(\"Consommation\", \"⚡\", \"BLUE\");
>
> kpiConsumptionSlot.getChildren().setAll(consoResult.root());
>
> var scoreResult = FxmlLoader.loadWithResult(
>
> \"/fr/renewguard/fxml/components/KpiCard.fxml\");
>
> kpiScoreCtrl = scoreResult.controller();
>
> kpiScoreCtrl.configure(\"Score IA\", \"🧠\", \"GREEN\");
>
> kpiScoreSlot.getChildren().setAll(scoreResult.root());
>
> var equipResult = FxmlLoader.loadWithResult(
>
> \"/fr/renewguard/fxml/components/KpiCard.fxml\");
>
> kpiEquipmentCtrl = equipResult.controller();
>
> kpiEquipmentCtrl.configure(\"Équipements actifs\", \"🔌\", \"AMBER\");
>
> kpiEquipmentSlot.getChildren().setAll(equipResult.root());
>
> var batteryResult = FxmlLoader.loadWithResult(
>
> \"/fr/renewguard/fxml/components/BatteryGauge.fxml\");
>
> batteryCtrl = batteryResult.controller();
>
> batterySlot.getChildren().setAll(batteryResult.root());
>
> var aiResult = FxmlLoader.loadWithResult(
>
> \"/fr/renewguard/fxml/components/AiDecisionCard.fxml\");
>
> aiDecisionCtrl = aiResult.controller();
>
> aiDecisionSlot.getChildren().setAll(aiResult.root());
>
> }
>
> // ── Bindings ─────────────────────────────────────────────────────
>
> private void bindViewModel() {
>
> vm.solarProductionProperty().addListener((o, old, val) -\>
>
> kpiSolarCtrl.setValue(
>
> NumberFormatter.formatKw(val.doubleValue()), \"kW\",
>
> \"+12% vs hier\", true));
>
> vm.consumptionProperty().addListener((o, old, val) -\>
>
> kpiConsumptionCtrl.setValue(
>
> NumberFormatter.formatKw(val.doubleValue()), \"kW\",
>
> \"--8% vs moy.\", false));
>
> vm.aiScoreProperty().addListener((o, old, val) -\>
>
> kpiScoreCtrl.setValue(
>
> String.valueOf(val.intValue()), \"/ 100\",
>
> \"Optimisation active\", null));
>
> vm.equipmentCountProperty().addListener((o, old, val) -\>
>
> kpiEquipmentCtrl.setValue(val, \"\", \"2 OFF par IA\", null));
>
> vm.batteryPercentProperty().addListener((o, old, val) -\>
>
> batteryCtrl.setPercent(val.intValue()));
>
> vm.batteryEtaProperty().addListener((o, old, val) -\>
>
> batteryCtrl.setEta(val));
>
> vm.batteryChargingProperty().addListener((o, old, val) -\>
>
> batteryCtrl.setCharging(val, vm.batteryChargeRateProperty().get()));
>
> vm.gridAvailableProperty().addListener((o, old, available) -\>
>
> updateNetworkCard(available));
>
> vm.gridVoltageProperty().addListener((o, old, val) -\>
>
> networkVoltageLabel.setText(NumberFormatter.formatVoltage(val.doubleValue())));
>
> vm.gridFrequencyProperty().addListener((o, old, val) -\>
>
> networkFrequencyLabel.setText(NumberFormatter.formatHz(val.doubleValue())));
>
> vm.gridImportProperty().addListener((o, old, val) -\>
>
> networkImportLabel.setText(NumberFormatter.formatKw(val.doubleValue()) + \" kW\"));
>
> vm.lastDecisionProperty().addListener((o, old, decision) -\> {
>
> if (decision != null) aiDecisionCtrl.apply(decision);
>
> });
>
> vm.getChartData().addListener(
>
> (javafx.collections.ListChangeListener\<HistoryPointDto\>) c -\>
>
> refreshChart(vm.getChartData())
>
> );
>
> vm.energySavedProperty().addListener((o, old, val) -\>
>
> energySavedLabel.setText(NumberFormatter.formatKwh(val.doubleValue())));
>
> vm.co2AvoidedProperty().addListener((o, old, val) -\>
>
> co2AvoidedLabel.setText(NumberFormatter.formatCo2Kg(val.doubleValue())));
>
> vm.solarMixProperty().addListener((o, old, v) -\> refreshDonut());
>
> vm.batteryMixProperty().addListener((o, old, v) -\> refreshDonut());
>
> vm.gridMixProperty().addListener((o, old, v) -\> refreshDonut());
>
> vm.systemViewVisibleProperty().addListener((o, old, visible) -\> {
>
> systemViewPanel.setVisible(visible);
>
> systemViewPanel.setManaged(visible);
>
> systemViewBtn.setText(visible ? \"Masquer le système\" : \"Vue système\");
>
> });
>
> }
>
> // ── Network card ─────────────────────────────────────────────────
>
> private void updateNetworkCard(boolean available) {
>
> String color = available ? \"#22D3A5\" : \"#FF4D4D\";
>
> networkDot.setStyle(\"-fx-fill: \" + color + \";\");
>
> networkStatusLabel.setText(available ? \"Réseau disponible\" : \"Coupure détectée\");
>
> networkStatusLabel.setStyle(\"-fx-text-fill: \" + color + \"; -fx-font-size: 15px; -fx-font-weight: 700;\");
>
> }
>
> // ── Chart ────────────────────────────────────────────────────────
>
> private void configureChart() {
>
> mainChart.setAnimated(false);
>
> mainChart.setLegendVisible(true);
>
> mainChart.getStyleClass().add(\"chart-dark\");
>
> }
>
> private void refreshChart(List\<HistoryPointDto\> points) {
>
> XYChart.Series\<String, Number\> production = new XYChart.Series\<\>();
>
> XYChart.Series\<String, Number\> consumption = new XYChart.Series\<\>();
>
> production.setName(\"Production solaire\");
>
> consumption.setName(\"Consommation\");
>
> for (HistoryPointDto p : points) {
>
> production.getData().add(
>
> new XYChart.Data\<\>(p.getLabel(), p.getProduction()));
>
> consumption.getData().add(
>
> new XYChart.Data\<\>(p.getLabel(), p.getConsumption()));
>
> }
>
> mainChart.getData().clear();
>
> mainChart.getData().addAll(production, consumption);
>
> javafx.application.Platform.runLater(() -\> {
>
> if (production.getNode() != null)
>
> production.getNode().setStyle(
>
> \"-fx-stroke: #22D3A5; -fx-stroke-width: 2;\");
>
> if (consumption.getNode() != null)
>
> consumption.getNode().setStyle(
>
> \"-fx-stroke: #3B9DFF; -fx-stroke-width: 2;\");
>
> for (XYChart.Data\<?, ?\> d : production.getData())
>
> if (d.getNode() != null)
>
> d.getNode().setStyle(\"-fx-background-color: #22D3A5, white;\");
>
> for (XYChart.Data\<?, ?\> d : consumption.getData())
>
> if (d.getNode() != null)
>
> d.getNode().setStyle(\"-fx-background-color: #3B9DFF, white;\");
>
> });
>
> }
>
> // ── Donut ────────────────────────────────────────────────────────
>
> private void refreshDonut() {
>
> double solar = vm.getSolarMix();
>
> double battery = vm.getBatteryMix();
>
> double grid = vm.getGridMix();
>
> pieChart.setData(FXCollections.observableArrayList(
>
> new PieChart.Data(\"Solaire\", solar),
>
> new PieChart.Data(\"Batterie\", battery),
>
> new PieChart.Data(\"Réseau\", grid)
>
> ));
>
> javafx.application.Platform.runLater(() -\> {
>
> var data = pieChart.getData();
>
> if (data.size() \>= 3) {
>
> data.get(0).getNode().setStyle(\"-fx-pie-color: #22D3A5;\");
>
> data.get(1).getNode().setStyle(\"-fx-pie-color: #3B9DFF;\");
>
> data.get(2).getNode().setStyle(\"-fx-pie-color: #FFA53E;\");
>
> }
>
> });
>
> }
>
> // ── Period buttons ───────────────────────────────────────────────
>
> \@FXML private void onPeriodDay() { switchPeriod(\"day\", btnDay); }
>
> \@FXML private void onPeriodWeek() { switchPeriod(\"week\", btnWeek); }
>
> \@FXML private void onPeriodMonth() { switchPeriod(\"month\", btnMonth); }
>
> private void switchPeriod(String period, Button active) {
>
> for (Button b : List.of(btnDay, btnWeek, btnMonth)) {
>
> b.getStyleClass().removeAll(\"period-btn-active\");
>
> b.getStyleClass().add(\"period-btn\");
>
> }
>
> active.getStyleClass().remove(\"period-btn\");
>
> active.getStyleClass().add(\"period-btn-active\");
>
> vm.changePeriod(period);
>
> }
>
> private void configurePeriodButtons(Button initial) {
>
> for (Button b : List.of(btnDay, btnWeek, btnMonth))
>
> b.getStyleClass().add(\"period-btn\");
>
> initial.getStyleClass().remove(\"period-btn\");
>
> initial.getStyleClass().add(\"period-btn-active\");
>
> }
>
> // ── System view ──────────────────────────────────────────────────
>
> \@FXML
>
> private void onSystemViewToggle() {
>
> vm.toggleSystemView();
>
> }
>
> }

**src/main/java/fr/renewguard/controller/EquipmentController.java**

> package fr.renewguard.controller;
>
> import fr.renewguard.component.EquipmentRowController;
>
> import fr.renewguard.model.dto.EquipmentDto;
>
> import fr.renewguard.model.enums.EquipmentStatus;
>
> import fr.renewguard.model.enums.PriorityLevel;
>
> import fr.renewguard.util.FxmlLoader;
>
> import fr.renewguard.util.NumberFormatter;
>
> import fr.renewguard.viewmodel.EquipmentViewModel;
>
> import javafx.fxml.FXML;
>
> import javafx.fxml.Initializable;
>
> import javafx.scene.control.\*;
>
> import javafx.scene.layout.HBox;
>
> import java.net.URL;
>
> import java.util.List;
>
> import java.util.ResourceBundle;
>
> public class EquipmentController implements Initializable {
>
> \@FXML private Label statsLabel;
>
> \@FXML private TextField searchField;
>
> \@FXML private Button addBtn;
>
> \@FXML private Button chipAll;
>
> \@FXML private Button chipCritical;
>
> \@FXML private Button chipImportant;
>
> \@FXML private Button chipLow;
>
> \@FXML private Button chipActive;
>
> \@FXML private Button chipInactive;
>
> \@FXML private TableView\<EquipmentDto\> table;
>
> \@FXML private TableColumn\<EquipmentDto, String\> colName;
>
> \@FXML private TableColumn\<EquipmentDto, String\> colLocation;
>
> \@FXML private TableColumn\<EquipmentDto, PriorityLevel\> colPriority;
>
> \@FXML private TableColumn\<EquipmentDto, Integer\> colPower;
>
> \@FXML private TableColumn\<EquipmentDto, EquipmentStatus\> colStatus;
>
> \@FXML private TableColumn\<EquipmentDto, String\> colActivity;
>
> \@FXML private TableColumn\<EquipmentDto, Void\> colToggle;
>
> private final EquipmentViewModel vm = new EquipmentViewModel();
>
> private List\<Button\> allChips;
>
> \@Override
>
> public void initialize(URL url, ResourceBundle rb) {
>
> allChips = List.of(chipAll, chipCritical, chipImportant, chipLow, chipActive, chipInactive);
>
> searchField.textProperty().bindBidirectional(vm.searchQueryProperty());
>
> configureColumns();
>
> table.setItems(vm.getFilteredEquipments());
>
> table.setPlaceholder(new Label(\"Aucun equipement trouve.\"));
>
> vm.totalActiveProperty().addListener((o, old, val) -\> refreshStats());
>
> vm.totalPowerWProperty().addListener((o, old, val) -\> refreshStats());
>
> setActiveChip(chipAll);
>
> vm.refresh();
>
> refreshStats();
>
> }
>
> private void refreshStats() {
>
> statsLabel.setText(vm.getTotalCount() + \" equipements - \" + vm.getTotalActive()
>
> \+ \" actifs - \" + NumberFormatter.formatWatts(vm.getTotalPowerW()) + \" consommes\");
>
> }
>
> private void configureColumns() {
>
> colName.setCellValueFactory(d -\> new javafx.beans.property.SimpleStringProperty(d.getValue().getName()));
>
> colLocation.setCellValueFactory(d -\> new javafx.beans.property.SimpleStringProperty(d.getValue().getLocation()));
>
> colPriority.setCellValueFactory(d -\> new javafx.beans.property.SimpleObjectProperty\<\>(d.getValue().getPriority()));
>
> colPriority.setCellFactory(col -\> new TableCell\<\>() {
>
> \@Override protected void updateItem(PriorityLevel item, boolean empty) {
>
> super.updateItem(item, empty);
>
> if (empty \|\| item == null) { setGraphic(null); return; }
>
> Label badge = new Label(item.displayLabel());
>
> badge.getStyleClass().add(item.badgeCssClass());
>
> setGraphic(badge);
>
> }
>
> });
>
> colPower.setCellValueFactory(d -\> new javafx.beans.property.SimpleObjectProperty\<\>(d.getValue().getPowerWatts()));
>
> colPower.setCellFactory(col -\> new TableCell\<\>() {
>
> \@Override protected void updateItem(Integer item, boolean empty) {
>
> super.updateItem(item, empty);
>
> setText(empty \|\| item == null ? null : NumberFormatter.formatWatts(item));
>
> }
>
> });
>
> colStatus.setCellValueFactory(d -\> new javafx.beans.property.SimpleObjectProperty\<\>(d.getValue().getStatus()));
>
> colStatus.setCellFactory(col -\> new TableCell\<\>() {
>
> \@Override protected void updateItem(EquipmentStatus item, boolean empty) {
>
> super.updateItem(item, empty);
>
> setText(empty \|\| item == null ? null : item.displayLabel());
>
> }
>
> });
>
> colActivity.setCellValueFactory(d -\> new javafx.beans.property.SimpleStringProperty(d.getValue().getLastActivity()));
>
> colToggle.setCellFactory(col -\> new TableCell\<\>() {
>
> private final FxmlLoader.Result\<EquipmentRowController\> result =
>
> FxmlLoader.loadWithResult(\"/fr/renewguard/fxml/components/EquipmentRow.fxml\");
>
> \@Override protected void updateItem(Void item, boolean empty) {
>
> super.updateItem(item, empty);
>
> if (empty \|\| getTableRow() == null \|\| getTableRow().getItem() == null) { setGraphic(null); return; }
>
> EquipmentDto eq = getTableRow().getItem();
>
> result.controller().bind(eq, id -\> { vm.toggleStatus(id); table.refresh(); });
>
> setGraphic(result.root());
>
> }
>
> });
>
> }
>
> \@FXML private void onChipAll() { applyChip(chipAll, \"ALL\"); }
>
> \@FXML private void onChipCritical() { applyChip(chipCritical, \"CRITICAL\"); }
>
> \@FXML private void onChipImportant() { applyChip(chipImportant, \"IMPORTANT\"); }
>
> \@FXML private void onChipLow() { applyChip(chipLow, \"LOW\"); }
>
> \@FXML private void onChipActive() { applyChip(chipActive, \"ACTIVE\"); }
>
> \@FXML private void onChipInactive() { applyChip(chipInactive, \"INACTIVE\"); }
>
> private void applyChip(Button chip, String filter) { vm.setFilter(filter); setActiveChip(chip); }
>
> private void setActiveChip(Button active) {
>
> for (Button b : allChips) { b.getStyleClass().removeAll(\"chip-active\"); b.getStyleClass().add(\"chip\"); }
>
> active.getStyleClass().remove(\"chip\");
>
> active.getStyleClass().add(\"chip-active\");
>
> }
>
> \@FXML private void onAdd() { /\* dialog d\'ajout, a implementer \*/ }
>
> \@FXML private void onRefresh() { vm.refresh(); }
>
> }

**src/main/java/fr/renewguard/controller/HistoryController.java**

> package fr.renewguard.controller;
>
> import fr.renewguard.component.AlertItemController;
>
> import fr.renewguard.model.dto.AlertDto;
>
> import fr.renewguard.model.dto.HistoryPointDto;
>
> import fr.renewguard.util.FxmlLoader;
>
> import fr.renewguard.util.NumberFormatter;
>
> import fr.renewguard.viewmodel.HistoryViewModel;
>
> import javafx.collections.ListChangeListener;
>
> import javafx.fxml.FXML;
>
> import javafx.fxml.Initializable;
>
> import javafx.scene.chart.\*;
>
> import javafx.scene.control.Button;
>
> import javafx.scene.control.Label;
>
> import javafx.scene.layout.VBox;
>
> import java.net.URL;
>
> import java.util.ResourceBundle;
>
> public class HistoryController implements Initializable {
>
> \@FXML private Button chipDay;
>
> \@FXML private Button chipWeek;
>
> \@FXML private Button chipMonth;
>
> \@FXML private Button chipYear;
>
> \@FXML private Button exportCsvBtn;
>
> \@FXML private Button exportPdfBtn;
>
> \@FXML private Label kpiSaved;
>
> \@FXML private Label kpiCo2;
>
> \@FXML private Label kpiSolar;
>
> \@FXML private Label kpiNet;
>
> \@FXML private AreaChart\<String, Number\> productionChart;
>
> \@FXML private AreaChart\<String, Number\> consumptionChart;
>
> \@FXML private BarChart\<String, Number\> savingsChart;
>
> \@FXML private AreaChart\<String, Number\> co2Chart;
>
> \@FXML private VBox alertsList;
>
> \@FXML private Label alertsCountLabel;
>
> private final HistoryViewModel vm = new HistoryViewModel();
>
> private java.util.List\<Button\> allChips;
>
> \@Override
>
> public void initialize(URL url, ResourceBundle rb) {
>
> allChips = java.util.List.of(chipDay, chipWeek, chipMonth, chipYear);
>
> vm.getProductionData().addListener((ListChangeListener\<HistoryPointDto\>) c -\> refreshProduction());
>
> vm.getConsumptionData().addListener((ListChangeListener\<HistoryPointDto\>) c -\> refreshConsumption());
>
> vm.getSavingsData().addListener((ListChangeListener\<HistoryPointDto\>) c -\> refreshSavings());
>
> vm.getCo2Data().addListener((ListChangeListener\<HistoryPointDto\>) c -\> refreshCo2());
>
> vm.getAlerts().addListener((ListChangeListener\<AlertDto\>) c -\> renderAlerts());
>
> vm.summaryProperty().addListener((o, old, s) -\> {
>
> if (s == null) return;
>
> kpiSaved.setText(NumberFormatter.formatKwh(s.getTotalSavedKwh()));
>
> kpiCo2.setText(NumberFormatter.formatCo2Kg(s.getTotalCo2Kg()));
>
> kpiSolar.setText(NumberFormatter.formatPercentDouble(s.getSolarCoveragePercent()));
>
> kpiNet.setText(NumberFormatter.formatCurrencyRounded(s.getNetBenefitEur()));
>
> });
>
> setActiveChip(chipWeek);
>
> vm.refresh();
>
> }
>
> private void refreshProduction() {
>
> XYChart.Series\<String, Number\> s = new XYChart.Series\<\>();
>
> for (HistoryPointDto p : vm.getProductionData()) s.getData().add(new XYChart.Data\<\>(p.getLabel(), p.getProduction()));
>
> productionChart.getData().setAll(s);
>
> }
>
> private void refreshConsumption() {
>
> XYChart.Series\<String, Number\> prod = new XYChart.Series\<\>();
>
> XYChart.Series\<String, Number\> cons = new XYChart.Series\<\>();
>
> for (HistoryPointDto p : vm.getConsumptionData()) {
>
> prod.getData().add(new XYChart.Data\<\>(p.getLabel(), p.getProduction()));
>
> cons.getData().add(new XYChart.Data\<\>(p.getLabel(), p.getConsumption()));
>
> }
>
> consumptionChart.getData().setAll(prod, cons);
>
> }
>
> private void refreshSavings() {
>
> XYChart.Series\<String, Number\> s = new XYChart.Series\<\>();
>
> for (HistoryPointDto p : vm.getSavingsData()) s.getData().add(new XYChart.Data\<\>(p.getLabel(), p.getSaved()));
>
> savingsChart.getData().setAll(s);
>
> }
>
> private void refreshCo2() {
>
> XYChart.Series\<String, Number\> s = new XYChart.Series\<\>();
>
> for (HistoryPointDto p : vm.getCo2Data()) s.getData().add(new XYChart.Data\<\>(p.getLabel(), p.getCo2()));
>
> co2Chart.getData().setAll(s);
>
> }
>
> private void renderAlerts() {
>
> alertsList.getChildren().clear();
>
> alertsCountLabel.setText(vm.getAlerts().size() + \" alertes\");
>
> for (AlertDto alert : vm.getAlerts()) {
>
> FxmlLoader.Result\<AlertItemController\> result =
>
> FxmlLoader.loadWithResult(\"/fr/renewguard/fxml/components/AlertItem.fxml\");
>
> result.controller().bind(alert, id -\> { vm.resolveAlert(id); renderAlerts(); });
>
> alertsList.getChildren().add(result.root());
>
> }
>
> }
>
> \@FXML private void onChipDay() { applyChip(chipDay, \"day\"); }
>
> \@FXML private void onChipWeek() { applyChip(chipWeek, \"week\"); }
>
> \@FXML private void onChipMonth() { applyChip(chipMonth, \"month\"); }
>
> \@FXML private void onChipYear() { applyChip(chipYear, \"year\"); }
>
> private void applyChip(Button chip, String period) { vm.setPeriod(period); setActiveChip(chip); }
>
> private void setActiveChip(Button active) {
>
> for (Button b : allChips) { b.getStyleClass().removeAll(\"chip-active\"); b.getStyleClass().add(\"chip\"); }
>
> active.getStyleClass().remove(\"chip\");
>
> active.getStyleClass().add(\"chip-active\");
>
> }
>
> \@FXML private void onExportCsv() { vm.exportReport(\"csv\"); }
>
> \@FXML private void onExportPdf() { vm.exportReport(\"pdf\"); }
>
> }

**src/main/java/fr/renewguard/controller/MainController.java**

> package fr.renewguard.controller;
>
> import fr.renewguard.navigation.SceneManager;
>
> import fr.renewguard.util.FxmlLoader;
>
> import fr.renewguard.viewmodel.shared.SessionViewModel;
>
> import javafx.fxml.FXML;
>
> import javafx.fxml.Initializable;
>
> import javafx.scene.Parent;
>
> import javafx.scene.input.KeyCode;
>
> import javafx.scene.input.KeyCodeCombination;
>
> import javafx.scene.input.KeyCombination;
>
> import javafx.scene.layout.\*;
>
> import java.net.URL;
>
> import java.util.ResourceBundle;
>
> public class MainController implements Initializable {
>
> \@FXML private BorderPane rootPane;
>
> \@FXML private StackPane contentArea;
>
> \@FXML private VBox emergencyBanner;
>
> \@FXML private VBox sidebarContainer;
>
> \@FXML private HBox topbarContainer;
>
> private SidebarController sidebarController;
>
> private TopbarController topbarController;
>
> private final SessionViewModel session = SessionViewModel.getInstance();
>
> \@Override
>
> public void initialize(URL url, ResourceBundle rb) {
>
> loadSidebar(); loadTopbar(); navigateTo(\"dashboard\");
>
> bindEmergencyBanner(); registerKeyShortcuts();
>
> }
>
> private void loadSidebar() {
>
> FxmlLoader.Result\<SidebarController\> result = FxmlLoader.loadWithResult(\"/fr/renewguard/fxml/Sidebar.fxml\");
>
> sidebarController = result.controller();
>
> sidebarController.setNavigationCallback(this::navigateTo);
>
> sidebarContainer.getChildren().setAll(result.root());
>
> VBox.setVgrow(result.root(), Priority.ALWAYS);
>
> }
>
> private void loadTopbar() {
>
> FxmlLoader.Result\<TopbarController\> result = FxmlLoader.loadWithResult(\"/fr/renewguard/fxml/Topbar.fxml\");
>
> topbarController = result.controller();
>
> topbarController.setOnLogout(this::handleLogout);
>
> topbarContainer.getChildren().setAll(result.root());
>
> HBox.setHgrow(result.root(), Priority.ALWAYS);
>
> }
>
> public void navigateTo(String screen) {
>
> String fxmlPath = switch (screen) {
>
> case \"dashboard\" -\> \"/fr/renewguard/fxml/Dashboard.fxml\";
>
> case \"equipment\" -\> \"/fr/renewguard/fxml/Equipment.fxml\";
>
> case \"priorities\" -\> \"/fr/renewguard/fxml/Priorities.fxml\";
>
> case \"ai\" -\> \"/fr/renewguard/fxml/Ai.fxml\";
>
> case \"history\" -\> \"/fr/renewguard/fxml/History.fxml\";
>
> default -\> \"/fr/renewguard/fxml/Dashboard.fxml\";
>
> };
>
> Parent view = FxmlLoader.load(fxmlPath);
>
> contentArea.getChildren().setAll(view);
>
> session.setActiveScreen(screen);
>
> if (sidebarController != null) sidebarController.setActive(screen);
>
> if (topbarController != null) topbarController.setTitle(screenTitle(screen));
>
> }
>
> private String screenTitle(String screen) {
>
> return switch (screen) {
>
> case \"dashboard\" -\> \"Dashboard energetique\";
>
> case \"equipment\" -\> \"Gestion des equipements\";
>
> case \"priorities\" -\> \"Priorites energetiques\";
>
> case \"ai\" -\> \"Analyse IA & Assistant\";
>
> case \"history\" -\> \"Historique & Alertes\";
>
> default -\> \"RenewGuard AI\";
>
> };
>
> }
>
> private void bindEmergencyBanner() {
>
> emergencyBanner.visibleProperty().bind(session.emergencyModeProperty());
>
> emergencyBanner.managedProperty().bind(session.emergencyModeProperty());
>
> session.emergencyModeProperty().addListener((obs, old, active) -\> {
>
> if (active) rootPane.getStyleClass().add(\"emergency\");
>
> else rootPane.getStyleClass().remove(\"emergency\");
>
> });
>
> }
>
> \@FXML private void onDisableEmergency() { session.setEmergencyMode(false); }
>
> private void registerKeyShortcuts() {
>
> rootPane.sceneProperty().addListener((obs, old, scene) -\> {
>
> if (scene == null) return;
>
> scene.getAccelerators().put(new KeyCodeCombination(KeyCode.DIGIT1, KeyCombination.CONTROL_DOWN), () -\> navigateTo(\"dashboard\"));
>
> scene.getAccelerators().put(new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.CONTROL_DOWN), () -\> navigateTo(\"equipment\"));
>
> scene.getAccelerators().put(new KeyCodeCombination(KeyCode.DIGIT3, KeyCombination.CONTROL_DOWN), () -\> navigateTo(\"priorities\"));
>
> scene.getAccelerators().put(new KeyCodeCombination(KeyCode.DIGIT4, KeyCombination.CONTROL_DOWN), () -\> navigateTo(\"ai\"));
>
> scene.getAccelerators().put(new KeyCodeCombination(KeyCode.DIGIT5, KeyCombination.CONTROL_DOWN), () -\> navigateTo(\"history\"));
>
> scene.getAccelerators().put(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN), () -\> session.toggleEmergency());
>
> scene.getAccelerators().put(new KeyCodeCombination(KeyCode.ESCAPE), this::handleEscape);
>
> });
>
> }
>
> private void handleEscape() { if (session.isEmergencyMode()) session.setEmergencyMode(false); }
>
> private void handleLogout() { session.logout(); SceneManager.navigate(\"auth\"); }
>
> }

**src/main/java/fr/renewguard/controller/PrioritiesController.java**

> package fr.renewguard.controller;
>
> import fr.renewguard.component.PriorityKanbanCardController;
>
> import fr.renewguard.model.dto.EquipmentDto;
>
> import fr.renewguard.model.dto.RuleDto;
>
> import fr.renewguard.util.FxmlLoader;
>
> import fr.renewguard.viewmodel.PrioritiesViewModel;
>
> import javafx.collections.ListChangeListener;
>
> import javafx.fxml.FXML;
>
> import javafx.fxml.Initializable;
>
> import javafx.scene.Node;
>
> import javafx.scene.control.Button;
>
> import javafx.scene.control.Label;
>
> import javafx.scene.control.ToggleButton;
>
> import javafx.scene.input.DragEvent;
>
> import javafx.scene.input.TransferMode;
>
> import javafx.scene.layout.HBox;
>
> import javafx.scene.layout.VBox;
>
> import java.net.URL;
>
> import java.util.ResourceBundle;
>
> public class PrioritiesController implements Initializable {
>
> \@FXML private HBox modifiedBanner;
>
> \@FXML private HBox savedBanner;
>
> \@FXML private VBox col1Cards;
>
> \@FXML private VBox col2Cards;
>
> \@FXML private VBox col3Cards;
>
> \@FXML private Label col1Stats;
>
> \@FXML private Label col2Stats;
>
> \@FXML private Label col3Stats;
>
> \@FXML private VBox dropZone1;
>
> \@FXML private VBox dropZone2;
>
> \@FXML private VBox dropZone3;
>
> \@FXML private VBox rulesContainer;
>
> \@FXML private VBox rulesList;
>
> private final PrioritiesViewModel vm = new PrioritiesViewModel();
>
> private EquipmentDto dragging;
>
> \@Override
>
> public void initialize(URL url, ResourceBundle rb) {
>
> modifiedBanner.visibleProperty().bind(vm.modifiedProperty());
>
> modifiedBanner.managedProperty().bind(vm.modifiedProperty());
>
> savedBanner.visibleProperty().bind(vm.savedProperty());
>
> savedBanner.managedProperty().bind(vm.savedProperty());
>
> vm.getLevel1().addListener((ListChangeListener\<EquipmentDto\>) c -\> renderColumn(1));
>
> vm.getLevel2().addListener((ListChangeListener\<EquipmentDto\>) c -\> renderColumn(2));
>
> vm.getLevel3().addListener((ListChangeListener\<EquipmentDto\>) c -\> renderColumn(3));
>
> vm.getRules().addListener((ListChangeListener\<RuleDto\>) c -\> renderRules());
>
> setupDropZone(dropZone1, 1);
>
> setupDropZone(dropZone2, 2);
>
> setupDropZone(dropZone3, 3);
>
> vm.refresh();
>
> }
>
> private void renderColumn(int level) {
>
> VBox target = level == 1 ? col1Cards : level == 2 ? col2Cards : col3Cards;
>
> Label stats = level == 1 ? col1Stats : level == 2 ? col2Stats : col3Stats;
>
> var items = level == 1 ? vm.getLevel1() : level == 2 ? vm.getLevel2() : vm.getLevel3();
>
> target.getChildren().clear();
>
> for (EquipmentDto eq : items) {
>
> FxmlLoader.Result\<PriorityKanbanCardController\> result =
>
> FxmlLoader.loadWithResult(\"/fr/renewguard/fxml/components/PriorityKanbanCard.fxml\");
>
> result.controller().bind(eq, this::startDrag);
>
> target.getChildren().add(result.root());
>
> }
>
> int totalW = items.stream().mapToInt(EquipmentDto::getPowerWatts).sum();
>
> stats.setText(totalW + \" W total - \" + items.size() + \" equipements\");
>
> }
>
> private void renderRules() {
>
> rulesList.getChildren().clear();
>
> for (RuleDto rule : vm.getRules()) {
>
> HBox row = new HBox(12);
>
> row.getStyleClass().add(\"rule-row\");
>
> row.setStyle(\"-fx-padding: 10 14 10 14;\");
>
> VBox text = new VBox(3);
>
> text.getChildren().addAll(
>
> new Label(\"Si \" + rule.getCondition()),
>
> new Label(\"-\> \" + rule.getAction()));
>
> ToggleButton toggle = new ToggleButton(rule.isActive() ? \"ON\" : \"OFF\");
>
> toggle.setSelected(rule.isActive());
>
> toggle.setOnAction(e -\> vm.toggleRule(rule.getId()));
>
> row.getChildren().addAll(text, toggle);
>
> rulesList.getChildren().add(row);
>
> }
>
> }
>
> private void startDrag(EquipmentDto equipment) { dragging = equipment; }
>
> private void setupDropZone(VBox zone, int level) {
>
> zone.setOnDragOver(e -\> {
>
> if (dragging != null && e.getGestureSource() != zone) e.acceptTransferModes(TransferMode.MOVE);
>
> e.consume();
>
> });
>
> zone.setOnDragDropped((DragEvent e) -\> {
>
> if (dragging != null) {
>
> int insertIdx = computeInsertIndex(zone, e.getY());
>
> vm.moveEquipment(dragging, level, insertIdx);
>
> dragging = null;
>
> e.setDropCompleted(true);
>
> }
>
> e.consume();
>
> });
>
> }
>
> private int computeInsertIndex(VBox zone, double dropY) {
>
> double accumulated = 0;
>
> int index = 0;
>
> for (Node child : zone.getChildren()) {
>
> if (accumulated + child.getBoundsInParent().getHeight() / 2 \> dropY) break;
>
> accumulated += child.getBoundsInParent().getHeight() + zone.getSpacing();
>
> index++;
>
> }
>
> return index;
>
> }
>
> \@FXML private void onSave() { vm.save(); }
>
> \@FXML private void onAddRule() { /\* dialog d\'ajout de regle, a implementer \*/ }
>
> \@FXML
>
> private void onToggleRulesPanel() {
>
> boolean visible = !rulesContainer.isVisible();
>
> rulesContainer.setVisible(visible);
>
> rulesContainer.setManaged(visible);
>
> }
>
> }

**src/main/java/fr/renewguard/controller/SidebarController.java**

> package fr.renewguard.controller;
>
> import fr.renewguard.viewmodel.shared.SessionViewModel;
>
> import javafx.fxml.FXML;
>
> import javafx.fxml.Initializable;
>
> import javafx.scene.control.Button;
>
> import javafx.scene.control.Label;
>
> import javafx.scene.layout.VBox;
>
> import java.net.URL;
>
> import java.util.Map;
>
> import java.util.ResourceBundle;
>
> import java.util.function.Consumer;
>
> public class SidebarController implements Initializable {
>
> \@FXML private VBox sidebarRoot;
>
> \@FXML private Label siteNameLabel;
>
> \@FXML private Button navDashboard;
>
> \@FXML private Button navEquipment;
>
> \@FXML private Button navPriorities;
>
> \@FXML private Button navAi;
>
> \@FXML private Button navHistory;
>
> private Consumer\<String\> navigationCallback;
>
> private final SessionViewModel session = SessionViewModel.getInstance();
>
> \@Override
>
> public void initialize(URL url, ResourceBundle rb) {
>
> siteNameLabel.textProperty().bind(session.siteNameProperty());
>
> setActive(\"dashboard\");
>
> navDashboard.setOnAction(e -\> navigate(\"dashboard\"));
>
> navEquipment.setOnAction(e -\> navigate(\"equipment\"));
>
> navPriorities.setOnAction(e -\> navigate(\"priorities\"));
>
> navAi.setOnAction(e -\> navigate(\"ai\"));
>
> navHistory.setOnAction(e -\> navigate(\"history\"));
>
> }
>
> public void setNavigationCallback(Consumer\<String\> callback) { this.navigationCallback = callback; }
>
> private void navigate(String screen) { if (navigationCallback != null) navigationCallback.accept(screen); }
>
> public void setActive(String screen) {
>
> Map\<String, Button\> buttons = Map.of(
>
> \"dashboard\", navDashboard, \"equipment\", navEquipment,
>
> \"priorities\", navPriorities, \"ai\", navAi, \"history\", navHistory);
>
> buttons.forEach((key, btn) -\> {
>
> btn.getStyleClass().removeAll(\"sidebar-item-active\");
>
> btn.getStyleClass().add(\"sidebar-item\");
>
> });
>
> Button active = buttons.get(screen);
>
> if (active != null) {
>
> active.getStyleClass().remove(\"sidebar-item\");
>
> active.getStyleClass().add(\"sidebar-item-active\");
>
> }
>
> }
>
> }

**src/main/java/fr/renewguard/controller/TopbarController.java**

> package fr.renewguard.controller;
>
> import fr.renewguard.viewmodel.shared.SessionViewModel;
>
> import javafx.animation.KeyFrame;
>
> import javafx.animation.Timeline;
>
> import javafx.beans.binding.Bindings;
>
> import javafx.fxml.FXML;
>
> import javafx.fxml.Initializable;
>
> import javafx.scene.control.\*;
>
> import javafx.util.Duration;
>
> import java.net.URL;
>
> import java.time.LocalTime;
>
> import java.time.format.DateTimeFormatter;
>
> import java.util.ResourceBundle;
>
> public class TopbarController implements Initializable {
>
> \@FXML private Label screenTitleLabel;
>
> \@FXML private TextField searchField;
>
> \@FXML private Button emergencyBtn;
>
> \@FXML private Label clockLabel;
>
> \@FXML private Label userInitials;
>
> \@FXML private javafx.scene.shape.Circle notifDot;
>
> private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(\"HH:mm:ss\");
>
> private final SessionViewModel session = SessionViewModel.getInstance();
>
> private Runnable onLogout;
>
> \@Override
>
> public void initialize(URL url, ResourceBundle rb) {
>
> bindEmergencyButton();
>
> startClock();
>
> bindUserInitials();
>
> }
>
> public void setOnLogout(Runnable callback) { this.onLogout = callback; }
>
> public void setTitle(String title) { screenTitleLabel.setText(title); }
>
> private void bindEmergencyButton() {
>
> emergencyBtn.textProperty().bind(
>
> Bindings.when(session.emergencyModeProperty()).then(\"URGENCE ACTIVE\").otherwise(\"Mode urgence\"));
>
> emergencyBtn.getStyleClass().add(\"btn-emergency\");
>
> }
>
> \@FXML private void onToggleEmergency() { session.toggleEmergency(); }
>
> \@FXML
>
> private void onToggleNotifications() {
>
> fr.renewguard.viewmodel.shared.NotificationViewModel notif =
>
> fr.renewguard.viewmodel.shared.NotificationViewModel.getInstance();
>
> notif.fetchAlerts();
>
> // TODO: afficher un popover ListView\<AlertDto\> lie a notif.getNotifications()
>
> // quand le composant NotificationPopover.fxml sera ajoute.
>
> if (notifDot != null) notifDot.setVisible(false);
>
> }
>
> \@FXML
>
> private void onUserMenu() {
>
> ContextMenu menu = new ContextMenu();
>
> MenuItem profile = new MenuItem(\"Profil\");
>
> MenuItem settings = new MenuItem(\"Parametres\");
>
> MenuItem logout = new MenuItem(\"Deconnexion\");
>
> logout.setOnAction(e -\> { if (onLogout != null) onLogout.run(); });
>
> menu.getItems().addAll(profile, settings, new SeparatorMenuItem(), logout);
>
> menu.show(userInitials, javafx.geometry.Side.BOTTOM, 0, 8);
>
> }
>
> private void startClock() {
>
> clockLabel.setText(LocalTime.now().format(TIME_FMT));
>
> Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1),
>
> e -\> clockLabel.setText(LocalTime.now().format(TIME_FMT))));
>
> clock.setCycleCount(Timeline.INDEFINITE);
>
> clock.play();
>
> }
>
> private void bindUserInitials() {
>
> session.usernameProperty().addListener((obs, old, name) -\> userInitials.setText(initials(name)));
>
> userInitials.setText(initials(session.getUsername()));
>
> }
>
> private String initials(String name) {
>
> if (name == null \|\| name.isBlank()) return \"?\";
>
> String\[\] parts = name.trim().split(\"\\\\s+\");
>
> if (parts.length \>= 2) return (\"\" + parts\[0\].charAt(0) + parts\[1\].charAt(0)).toUpperCase();
>
> return name.substring(0, Math.min(2, name.length())).toUpperCase();
>
> }
>
> }

**12. Composants réutilisables**

**src/main/java/fr/renewguard/component/AiDecisionCardController.java**

> package fr.renewguard.component;
>
> import fr.renewguard.model.dto.AiDecisionDto;
>
> import fr.renewguard.util.NumberFormatter;
>
> import javafx.animation.FadeTransition;
>
> import javafx.fxml.FXML;
>
> import javafx.fxml.Initializable;
>
> import javafx.scene.control.Label;
>
> import javafx.scene.control.ProgressBar;
>
> import javafx.scene.layout.HBox;
>
> import javafx.scene.layout.VBox;
>
> import javafx.util.Duration;
>
> import java.net.URL;
>
> import java.util.ResourceBundle;
>
> public class AiDecisionCardController implements Initializable {
>
> \@FXML private VBox cardRoot;
>
> \@FXML private Label titleLabel;
>
> \@FXML private Label actionLabel;
>
> \@FXML private Label reasonLabel;
>
> \@FXML private Label impactLabel;
>
> \@FXML private Label timestampLabel;
>
> \@FXML private Label statusBadge;
>
> \@FXML private ProgressBar confidenceBar;
>
> \@FXML private Label confidenceLabel;
>
> \@FXML private HBox detailsRow;
>
> \@Override
>
> public void initialize(URL url, ResourceBundle rb) {}
>
> public void apply(AiDecisionDto decision) {
>
> actionLabel.setText(decision.getAction());
>
> reasonLabel.setText(decision.getReason());
>
> impactLabel.setText(\"Impact : \" + decision.getImpactLabel());
>
> timestampLabel.setText(NumberFormatter.formatTimeOnly(decision.getTimestamp()));
>
> double confidence = decision.getConfidencePercent() / 100.0;
>
> confidenceBar.setProgress(confidence);
>
> confidenceLabel.setText(decision.getConfidencePercent() + \"%\");
>
> applyStatus(decision);
>
> fadeIn();
>
> }
>
> private void applyStatus(AiDecisionDto decision) {
>
> statusBadge.getStyleClass().removeAll(\"badge-done\", \"badge-pending\");
>
> if (decision.isPending()) {
>
> statusBadge.setText(\"EN ATTENTE\");
>
> statusBadge.getStyleClass().add(\"badge-pending\");
>
> } else {
>
> statusBadge.setText(\"EFFECTUÉ\");
>
> statusBadge.getStyleClass().add(\"badge-done\");
>
> }
>
> }
>
> private void fadeIn() {
>
> cardRoot.setOpacity(0);
>
> FadeTransition ft = new FadeTransition(Duration.millis(400), cardRoot);
>
> ft.setFromValue(0);
>
> ft.setToValue(1);
>
> ft.play();
>
> }
>
> }

**src/main/java/fr/renewguard/component/AlertItemController.java**

> package fr.renewguard.component;
>
> import fr.renewguard.model.dto.AlertDto;
>
> import fr.renewguard.util.NumberFormatter;
>
> import javafx.fxml.FXML;
>
> import javafx.fxml.Initializable;
>
> import javafx.scene.control.Button;
>
> import javafx.scene.control.Label;
>
> import javafx.scene.layout.HBox;
>
> import javafx.scene.shape.Circle;
>
> import java.net.URL;
>
> import java.util.ResourceBundle;
>
> import java.util.function.Consumer;
>
> public class AlertItemController implements Initializable {
>
> \@FXML private HBox itemRoot;
>
> \@FXML private Circle severityDot;
>
> \@FXML private Label titleLabel;
>
> \@FXML private Label descLabel;
>
> \@FXML private Label timeLabel;
>
> \@FXML private Button resolveBtn;
>
> private Consumer\<Long\> onResolve;
>
> \@Override
>
> public void initialize(URL url, ResourceBundle rb) {}
>
> public void bind(AlertDto alert, Consumer\<Long\> resolveCallback) {
>
> this.onResolve = resolveCallback;
>
> titleLabel.setText(alert.getTitle());
>
> descLabel.setText(alert.getDescription());
>
> timeLabel.setText(NumberFormatter.formatTimestamp(alert.getTimestamp()));
>
> severityDot.setStyle(\"-fx-fill: \" + alert.getSeverity().dotColor() + \";\");
>
> if (alert.isResolved()) {
>
> resolveBtn.setText(\"Resolu\");
>
> resolveBtn.setDisable(true);
>
> itemRoot.setOpacity(0.5);
>
> } else {
>
> resolveBtn.setOnAction(e -\> { if (onResolve != null) onResolve.accept(alert.getId()); });
>
> }
>
> }
>
> }

**src/main/java/fr/renewguard/component/BatteryGaugeController.java**

> package fr.renewguard.component;
>
> import javafx.animation.AnimationTimer;
>
> import javafx.fxml.FXML;
>
> import javafx.fxml.Initializable;
>
> import javafx.scene.canvas.Canvas;
>
> import javafx.scene.canvas.GraphicsContext;
>
> import javafx.scene.control.Label;
>
> import javafx.scene.paint.Color;
>
> import javafx.scene.shape.ArcType;
>
> import javafx.scene.shape.StrokeLineCap;
>
> import java.net.URL;
>
> import java.util.ResourceBundle;
>
> public class BatteryGaugeController implements Initializable {
>
> \@FXML private Canvas gaugeCanvas;
>
> \@FXML private Label percentLabel;
>
> \@FXML private Label etaLabel;
>
> \@FXML private Label trendLabel;
>
> private int displayedPercent = 0;
>
> private int targetPercent = 0;
>
> private AnimationTimer animator;
>
> private static final double TRACK_WIDTH = 9;
>
> private static final Color TRACK_COLOR = Color.web(\"#1E2A3A\");
>
> \@Override
>
> public void initialize(URL url, ResourceBundle rb) {
>
> drawArc(0);
>
> startAnimator();
>
> }
>
> // ── Public API ───────────────────────────────────────────────────
>
> public void setPercent(int percent) {
>
> this.targetPercent = Math.max(0, Math.min(100, percent));
>
> percentLabel.setText(percent + \"%\");
>
> }
>
> public void setEta(String eta) {
>
> etaLabel.setText(eta != null ? eta : \"---\");
>
> }
>
> public void setCharging(boolean charging, double rateKw) {
>
> if (charging) {
>
> trendLabel.setText(\"↑ En charge · +\" +
>
> String.format(java.util.Locale.FRANCE, \"%.1f\", rateKw) + \" kW\");
>
> trendLabel.setStyle(\"-fx-text-fill: #22D3A5; -fx-font-size: 11px;\");
>
> } else {
>
> trendLabel.setText(\"↓ Décharge\");
>
> trendLabel.setStyle(\"-fx-text-fill: #FFA53E; -fx-font-size: 11px;\");
>
> }
>
> }
>
> // ── Animation ────────────────────────────────────────────────────
>
> private void startAnimator() {
>
> animator = new AnimationTimer() {
>
> \@Override
>
> public void handle(long now) {
>
> if (displayedPercent != targetPercent) {
>
> int step = targetPercent \> displayedPercent ? 1 : -1;
>
> displayedPercent += step;
>
> drawArc(displayedPercent);
>
> }
>
> }
>
> };
>
> animator.start();
>
> }
>
> // ── Canvas drawing ───────────────────────────────────────────────
>
> private void drawArc(int percent) {
>
> double w = gaugeCanvas.getWidth();
>
> double h = gaugeCanvas.getHeight();
>
> double cx = w / 2.0;
>
> double cy = h / 2.0;
>
> double r = Math.min(w, h) / 2.0 - TRACK_WIDTH;
>
> GraphicsContext gc = gaugeCanvas.getGraphicsContext2D();
>
> gc.clearRect(0, 0, w, h);
>
> // Track (full circle)
>
> gc.setStroke(TRACK_COLOR);
>
> gc.setLineWidth(TRACK_WIDTH);
>
> gc.setLineCap(StrokeLineCap.ROUND);
>
> gc.strokeArc(cx - r, cy - r, r \* 2, r \* 2,
>
> 0, 360, ArcType.OPEN);
>
> // Filled arc
>
> double sweep = 360.0 \* percent / 100.0;
>
> Color arcColor = arcColorForPercent(percent);
>
> gc.setStroke(arcColor);
>
> gc.setLineWidth(TRACK_WIDTH);
>
> gc.setLineCap(StrokeLineCap.ROUND);
>
> gc.strokeArc(cx - r, cy - r, r \* 2, r \* 2,
>
> 90, -sweep, ArcType.OPEN);
>
> // Glow dot at arc tip
>
> if (percent \> 2) {
>
> double angleRad = Math.toRadians(90 - sweep);
>
> double dotX = cx + r \* Math.cos(angleRad);
>
> double dotY = cy - r \* Math.sin(angleRad);
>
> gc.setFill(arcColor);
>
> gc.fillOval(dotX - TRACK_WIDTH / 2.0, dotY - TRACK_WIDTH / 2.0,
>
> TRACK_WIDTH, TRACK_WIDTH);
>
> }
>
> // Update percent label colour
>
> String hex = toHex(arcColor);
>
> percentLabel.setStyle(
>
> \"-fx-font-family: \'JetBrains Mono\'; -fx-font-size: 22px;\" +
>
> \"-fx-font-weight: 700; -fx-text-fill: \" + hex + \";\");
>
> }
>
> private Color arcColorForPercent(int percent) {
>
> if (percent \<= 15) return Color.web(\"#FF4D4D\");
>
> if (percent \<= 30) return Color.web(\"#FFA53E\");
>
> return Color.web(\"#3B9DFF\");
>
> }
>
> private String toHex(Color c) {
>
> return String.format(\"#%02X%02X%02X\",
>
> (int) (c.getRed() \* 255),
>
> (int) (c.getGreen() \* 255),
>
> (int) (c.getBlue() \* 255));
>
> }
>
> }

**src/main/java/fr/renewguard/component/ChatBubbleController.java**

> package fr.renewguard.component;
>
> import fr.renewguard.util.NumberFormatter;
>
> import javafx.fxml.FXML;
>
> import javafx.fxml.Initializable;
>
> import javafx.scene.control.Label;
>
> import javafx.scene.layout.HBox;
>
> import javafx.scene.layout.VBox;
>
> import java.net.URL;
>
> import java.time.LocalDateTime;
>
> import java.util.ResourceBundle;
>
> public class ChatBubbleController implements Initializable {
>
> \@FXML private HBox bubbleRoot;
>
> \@FXML private VBox bubble;
>
> \@FXML private Label textLabel;
>
> \@FXML private Label timeLabel;
>
> \@Override
>
> public void initialize(URL url, ResourceBundle rb) {}
>
> public void bind(String text, boolean isUser, LocalDateTime timestamp) {
>
> textLabel.setText(text);
>
> timeLabel.setText(NumberFormatter.formatTimeOnly(timestamp));
>
> bubble.getStyleClass().add(isUser ? \"bubble-user\" : \"bubble-ai\");
>
> bubbleRoot.setStyle(isUser ? \"-fx-alignment: CENTER_RIGHT;\" : \"-fx-alignment: CENTER_LEFT;\");
>
> }
>
> }

**src/main/java/fr/renewguard/component/EquipmentRowController.java**

> package fr.renewguard.component;
>
> import fr.renewguard.model.dto.EquipmentDto;
>
> import fr.renewguard.model.enums.EquipmentStatus;
>
> import javafx.fxml.FXML;
>
> import javafx.fxml.Initializable;
>
> import org.controlsfx.control.ToggleSwitch;
>
> import java.net.URL;
>
> import java.util.ResourceBundle;
>
> import java.util.function.Consumer;
>
> public class EquipmentRowController implements Initializable {
>
> \@FXML private ToggleSwitch toggleSwitch;
>
> private Consumer\<Long\> onToggle;
>
> private long equipmentId;
>
> private boolean programmatic = false;
>
> \@Override
>
> public void initialize(URL url, ResourceBundle rb) {
>
> toggleSwitch.selectedProperty().addListener((obs, old, selected) -\> {
>
> if (!programmatic && onToggle != null) onToggle.accept(equipmentId);
>
> });
>
> }
>
> public void bind(EquipmentDto equipment, Consumer\<Long\> toggleCallback) {
>
> this.equipmentId = equipment.getId();
>
> this.onToggle = toggleCallback;
>
> programmatic = true;
>
> toggleSwitch.setSelected(equipment.getStatus() == EquipmentStatus.ON);
>
> programmatic = false;
>
> boolean locked = equipment.getStatus() == EquipmentStatus.AI_OFF \|\| equipment.getStatus() == EquipmentStatus.OFFLINE;
>
> toggleSwitch.setDisable(locked);
>
> toggleSwitch.setOpacity(locked ? 0.45 : 1.0);
>
> }
>
> }

**src/main/java/fr/renewguard/component/KpiCardController.java**

> package fr.renewguard.component;
>
> import javafx.animation.KeyFrame;
>
> import javafx.animation.Timeline;
>
> import javafx.fxml.FXML;
>
> import javafx.fxml.Initializable;
>
> import javafx.scene.control.Label;
>
> import javafx.scene.layout.HBox;
>
> import javafx.scene.layout.VBox;
>
> import javafx.util.Duration;
>
> import java.net.URL;
>
> import java.util.ResourceBundle;
>
> public class KpiCardController implements Initializable {
>
> \@FXML private VBox cardRoot;
>
> \@FXML private Label labelText;
>
> \@FXML private Label iconLabel;
>
> \@FXML private Label valueLabel;
>
> \@FXML private Label unitLabel;
>
> \@FXML private Label trendLabel;
>
> \@FXML private HBox valueRow;
>
> \@Override
>
> public void initialize(URL url, ResourceBundle rb) {}
>
> /\*\*
>
> \* Called once after FXML injection to configure the static card identity.
>
> \*
>
> \* \@param label card title
>
> \* \@param icon emoji / symbol shown top-right
>
> \* \@param color one of: GREEN, BLUE, AMBER, RED
>
> \*/
>
> public void configure(String label, String icon, String color) {
>
> labelText.setText(label);
>
> iconLabel.setText(icon);
>
> applyGlow(color);
>
> }
>
> /\*\*
>
> \* Updates the live value displayed on the card.
>
> \*
>
> \* \@param value formatted numeric string
>
> \* \@param unit unit suffix (may be empty)
>
> \* \@param trend subtitle / delta text
>
> \* \@param up true = green up, false = red down, null = neutral grey
>
> \*/
>
> public void setValue(String value, String unit, String trend, Boolean up) {
>
> animateValue(value);
>
> unitLabel.setText(unit);
>
> trendLabel.setText(trend);
>
> trendLabel.getStyleClass().removeAll(
>
> \"kpi-trend-up\", \"kpi-trend-down\", \"kpi-sub\");
>
> if (up == null) {
>
> trendLabel.getStyleClass().add(\"kpi-sub\");
>
> } else if (up) {
>
> trendLabel.getStyleClass().add(\"kpi-trend-up\");
>
> } else {
>
> trendLabel.getStyleClass().add(\"kpi-trend-down\");
>
> }
>
> }
>
> private void animateValue(String target) {
>
> valueLabel.setOpacity(0);
>
> valueLabel.setText(target);
>
> Timeline fade = new Timeline(
>
> new KeyFrame(Duration.ZERO,
>
> new javafx.animation.KeyValue(valueLabel.opacityProperty(), 0)),
>
> new KeyFrame(Duration.millis(350),
>
> new javafx.animation.KeyValue(valueLabel.opacityProperty(), 1,
>
> javafx.animation.Interpolator.EASE_OUT))
>
> );
>
> fade.play();
>
> }
>
> private void applyGlow(String color) {
>
> cardRoot.getStyleClass().removeAll(
>
> \"card-glow-green\", \"card-glow-blue\", \"card-glow-amber\", \"card-glow-red\");
>
> String iconStyle = switch (color) {
>
> case \"GREEN\" -\> { cardRoot.getStyleClass().add(\"card-glow-green\");
>
> yield \"-fx-text-fill: #22D3A5;\"; }
>
> case \"BLUE\" -\> { cardRoot.getStyleClass().add(\"card-glow-blue\");
>
> yield \"-fx-text-fill: #3B9DFF;\"; }
>
> case \"AMBER\" -\> { cardRoot.getStyleClass().add(\"card-glow-amber\");
>
> yield \"-fx-text-fill: #FFA53E;\"; }
>
> case \"RED\" -\> { cardRoot.getStyleClass().add(\"card-glow-red\");
>
> yield \"-fx-text-fill: #FF4D4D;\"; }
>
> default -\> \"-fx-text-fill: #8B93A7;\";
>
> };
>
> iconLabel.setStyle(iconStyle + \" -fx-font-size: 17px;\");
>
> }
>
> }

**src/main/java/fr/renewguard/component/PriorityKanbanCardController.java**

> package fr.renewguard.component;
>
> import fr.renewguard.model.dto.EquipmentDto;
>
> import fr.renewguard.model.enums.EquipmentStatus;
>
> import javafx.fxml.FXML;
>
> import javafx.fxml.Initializable;
>
> import javafx.scene.control.Label;
>
> import javafx.scene.input.ClipboardContent;
>
> import javafx.scene.input.Dragboard;
>
> import javafx.scene.input.TransferMode;
>
> import javafx.scene.layout.HBox;
>
> import javafx.scene.shape.Circle;
>
> import java.net.URL;
>
> import java.util.ResourceBundle;
>
> import java.util.function.Consumer;
>
> public class PriorityKanbanCardController implements Initializable {
>
> \@FXML private HBox cardRoot;
>
> \@FXML private Label iconLabel;
>
> \@FXML private Label nameLabel;
>
> \@FXML private Label powerLabel;
>
> \@FXML private Label statusDot;
>
> \@FXML private Circle statusCircle;
>
> private EquipmentDto equipment;
>
> private Consumer\<EquipmentDto\> onDragStart;
>
> \@Override
>
> public void initialize(URL url, ResourceBundle rb) {}
>
> public void bind(EquipmentDto eq, Consumer\<EquipmentDto\> dragStartCallback) {
>
> this.equipment = eq;
>
> this.onDragStart = dragStartCallback;
>
> iconLabel.setText(eq.getIcon() != null ? eq.getIcon() : \"device\");
>
> nameLabel.setText(eq.getName());
>
> powerLabel.setText(eq.getPowerWatts() + \" W\");
>
> applyStatus(eq.getStatus());
>
> setupDrag();
>
> }
>
> private void applyStatus(EquipmentStatus status) {
>
> statusCircle.setStyle(\"-fx-fill: \" + status.dotColor() + \";\");
>
> statusDot.setText(status.displayLabel());
>
> }
>
> private void setupDrag() {
>
> cardRoot.setOnDragDetected(e -\> {
>
> if (onDragStart != null) onDragStart.accept(equipment);
>
> Dragboard db = cardRoot.startDragAndDrop(TransferMode.MOVE);
>
> ClipboardContent content = new ClipboardContent();
>
> content.putString(String.valueOf(equipment.getId()));
>
> db.setContent(content);
>
> cardRoot.setOpacity(0.35);
>
> e.consume();
>
> });
>
> cardRoot.setOnDragDone(e -\> { cardRoot.setOpacity(1.0); e.consume(); });
>
> }
>
> }

**13. Vues FXML disponibles**

**src/main/resources/fr/renewguard/fxml/Ai.fxml**

> \<?xml version=\"1.0\" encoding=\"UTF-8\"?\>
>
> \<?import javafx.scene.layout.VBox?\>
>
> \<?import javafx.scene.layout.HBox?\>
>
> \<?import javafx.scene.layout.Region?\>
>
> \<?import javafx.scene.layout.StackPane?\>
>
> \<?import javafx.scene.control.Button?\>
>
> \<?import javafx.scene.control.Label?\>
>
> \<?import javafx.scene.control.TextField?\>
>
> \<?import javafx.scene.control.ScrollPane?\>
>
> \<?import javafx.scene.control.Tooltip?\>
>
> \<?import javafx.scene.chart.LineChart?\>
>
> \<?import javafx.scene.chart.CategoryAxis?\>
>
> \<?import javafx.scene.chart.NumberAxis?\>
>
> \<?import javafx.scene.shape.Circle?\>
>
> \<HBox xmlns:fx=\"http://javafx.com/fxml/1\"
>
> fx:controller=\"fr.renewguard.controller.AiController\"
>
> spacing=\"16\"
>
> style=\"-fx-padding: 24; -fx-background-color: transparent;\"\>
>
> \<VBox HBox.hgrow=\"ALWAYS\" spacing=\"14\" style=\"-fx-min-width: 0;\"\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"14\"\>
>
> \<Label fx:id=\"totalDecisionsLabel\" text=\"--- decisions aujourd\'hui\"
>
> style=\"-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #F5F7FA; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \<Label text=\"-\" style=\"-fx-text-fill: #2A3342;\"/\>
>
> \<Label fx:id=\"pendingDecisionsLabel\" text=\"--- en attente\"
>
> style=\"-fx-font-size: 12px; -fx-text-fill: #3B9DFF; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \<Region HBox.hgrow=\"ALWAYS\"/\>
>
> \<Button text=\"Actualiser\" onAction=\"#onRefresh\"
>
> style=\"-fx-background-color: rgba(255,255,255,0.04); -fx-border-color: #2A3342;
>
> -fx-border-radius: 8; -fx-background-radius: 8; -fx-border-width: 1;
>
> -fx-text-fill: #8B93A7; -fx-cursor: hand; -fx-font-size: 12px;\"/\>
>
> \</HBox\>
>
> \<VBox spacing=\"10\"
>
> style=\"-fx-background-color: rgba(21,27,38,0.75); -fx-border-color: rgba(59,157,255,0.28);
>
> -fx-border-radius: 16; -fx-background-radius: 16; -fx-border-width: 1; -fx-padding: 16;\"\>
>
> \<Label text=\"Prediction de consommation - 24h\"
>
> style=\"-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #F5F7FA; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \<LineChart fx:id=\"predictionChart\" prefHeight=\"188\" animated=\"false\" createSymbols=\"false\" legendVisible=\"true\"
>
> style=\"-fx-background-color: transparent; -fx-padding: 0;\"\>
>
> \<xAxis\>\<CategoryAxis tickLabelRotation=\"-35\" style=\"-fx-tick-label-fill: #8B93A7;\"/\>\</xAxis\>
>
> \<yAxis\>\<NumberAxis label=\"kWh\" style=\"-fx-tick-label-fill: #8B93A7;\"/\>\</yAxis\>
>
> \</LineChart\>
>
> \</VBox\>
>
> \<VBox VBox.vgrow=\"ALWAYS\" spacing=\"12\"
>
> style=\"-fx-background-color: rgba(21,27,38,0.75); -fx-border-color: #2A3342;
>
> -fx-border-radius: 16; -fx-background-radius: 16; -fx-border-width: 1; -fx-padding: 16;\"\>
>
> \<Label text=\"Decisions IA recentes\"
>
> style=\"-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #F5F7FA; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \<ScrollPane fitToWidth=\"true\" VBox.vgrow=\"ALWAYS\"
>
> style=\"-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;\"\>
>
> \<VBox fx:id=\"timelineContainer\" spacing=\"8\" style=\"-fx-padding: 0 4 0 0;\"/\>
>
> \</ScrollPane\>
>
> \</VBox\>
>
> \</VBox\>
>
> \<VBox prefWidth=\"380\" minWidth=\"320\" maxWidth=\"420\"
>
> style=\"-fx-background-color: rgba(21,27,38,0.75); -fx-border-color: rgba(34,211,165,0.28);
>
> -fx-border-radius: 16; -fx-background-radius: 16; -fx-border-width: 1;\"\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"10\"
>
> style=\"-fx-padding: 16 16 12 16; -fx-border-color: transparent transparent #1E2A3A transparent; -fx-border-width: 1;\"\>
>
> \<StackPane\>
>
> \<Circle radius=\"18\" style=\"-fx-fill: linear-gradient(to bottom right, #22D3A5, #3B9DFF);\"/\>
>
> \<Label text=\"IA\" style=\"-fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: #0A0E14;\"/\>
>
> \</StackPane\>
>
> \<VBox spacing=\"1\"\>
>
> \<Label text=\"Assistant RenewGuard AI\"
>
> style=\"-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #F5F7FA; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \<Label text=\"En ligne - Analyse continue\"
>
> style=\"-fx-font-size: 10px; -fx-text-fill: #8B93A7; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \</VBox\>
>
> \</HBox\>
>
> \<ScrollPane fitToWidth=\"true\" VBox.vgrow=\"ALWAYS\"
>
> style=\"-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;\"\>
>
> \<VBox fx:id=\"chatMessages\" spacing=\"10\" style=\"-fx-padding: 14;\"/\>
>
> \</ScrollPane\>
>
> \<HBox fx:id=\"typingIndicator\" alignment=\"CENTER_LEFT\" spacing=\"4\" visible=\"false\" managed=\"false\"
>
> style=\"-fx-padding: 4 18 4 18;\"\>
>
> \<Label text=\"L\'IA redige\...\" style=\"-fx-font-size: 11px; -fx-text-fill: #8B93A7;\"/\>
>
> \</HBox\>
>
> \<HBox spacing=\"8\" alignment=\"CENTER\"
>
> style=\"-fx-padding: 12 14 14 14; -fx-border-color: #1E2A3A transparent transparent transparent; -fx-border-width: 1;\"\>
>
> \<TextField fx:id=\"chatInputField\" promptText=\"Posez votre question a l\'IA\...\" HBox.hgrow=\"ALWAYS\"
>
> style=\"-fx-background-color: rgba(255,255,255,0.04); -fx-border-color: #2A3342;
>
> -fx-border-radius: 10; -fx-background-radius: 10; -fx-border-width: 1;
>
> -fx-text-fill: #F5F7FA; -fx-prompt-text-fill: #8B93A7;
>
> -fx-font-size: 13px; -fx-pref-height: 38; -fx-padding: 0 12 0 12;\"/\>
>
> \<Button fx:id=\"sendBtn\" text=\"Envoyer\" onAction=\"#onSend\"
>
> style=\"-fx-background-color: linear-gradient(to bottom right, #22D3A5, #1BB88E);
>
> -fx-background-radius: 10; -fx-border-color: transparent;
>
> -fx-font-size: 12px; -fx-font-weight: 700; -fx-text-fill: #0A0E14; -fx-cursor: hand;\"\>
>
> \<tooltip\>\<Tooltip text=\"Envoyer (Entree)\"/\>\</tooltip\>
>
> \</Button\>
>
> \</HBox\>
>
> \</VBox\>
>
> \</HBox\>

**src/main/resources/fr/renewguard/fxml/Auth.fxml**

> \<?xml version=\"1.0\" encoding=\"UTF-8\"?\>
>
> \<?import javafx.scene.layout.StackPane?\>
>
> \<?import javafx.scene.layout.VBox?\>
>
> \<?import javafx.scene.layout.HBox?\>
>
> \<?import javafx.scene.layout.Region?\>
>
> \<?import javafx.scene.control.Button?\>
>
> \<?import javafx.scene.control.CheckBox?\>
>
> \<?import javafx.scene.control.Label?\>
>
> \<?import javafx.scene.control.PasswordField?\>
>
> \<?import javafx.scene.control.TextField?\>
>
> \<?import javafx.scene.shape.Circle?\>
>
> \<?import javafx.geometry.Insets?\>
>
> \<StackPane xmlns:fx=\"http://javafx.com/fxml/1\"
>
> fx:controller=\"fr.renewguard.controller.AuthController\"
>
> style=\"-fx-background-color: #0A0E14;\"
>
> minWidth=\"600\" minHeight=\"480\"\>
>
> \<Region mouseTransparent=\"true\"
>
> prefWidth=\"600\" prefHeight=\"600\"
>
> style=\"-fx-background-color: radial-gradient(center 50% 50%, radius 50%, rgba(34,211,165,0.07) 0%, transparent 100%);
>
> -fx-background-radius: 300;\"/\>
>
> \<Region mouseTransparent=\"true\"
>
> prefWidth=\"420\" prefHeight=\"420\"
>
> style=\"-fx-background-color: radial-gradient(center 50% 50%, radius 50%, rgba(59,157,255,0.05) 0%, transparent 100%);
>
> -fx-background-radius: 210;\"
>
> StackPane.alignment=\"BOTTOM_RIGHT\"\>
>
> \<StackPane.margin\>
>
> \<Insets right=\"60\" bottom=\"40\"/\>
>
> \</StackPane.margin\>
>
> \</Region\>
>
> \<VBox alignment=\"CENTER_LEFT\"
>
> maxWidth=\"420\" prefWidth=\"420\"
>
> spacing=\"0\"
>
> style=\"-fx-background-color: rgba(15, 20, 32, 0.88);
>
> -fx-background-radius: 20;
>
> -fx-border-color: #2A3342;
>
> -fx-border-radius: 20;
>
> -fx-border-width: 1;
>
> -fx-padding: 40;
>
> -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.65), 48, 0, 0, 0);\"\>
>
> \<VBox alignment=\"CENTER\" spacing=\"10\" style=\"-fx-padding: 0 0 28 0;\"\>
>
> \<HBox alignment=\"CENTER\"
>
> style=\"-fx-background-color: linear-gradient(to bottom right, #22D3A5, #3B9DFF);
>
> -fx-background-radius: 14;
>
> -fx-pref-width: 56; -fx-pref-height: 56;
>
> -fx-min-width: 56; -fx-min-height: 56;
>
> -fx-effect: dropshadow(gaussian, rgba(34,211,165,0.3), 24, 0, 0, 0);\"
>
> maxWidth=\"56\"\>
>
> \<Label text=\"RG\" style=\"-fx-font-size: 20px; -fx-font-weight: 700; -fx-text-fill: #0A0E14;\"/\>
>
> \</HBox\>
>
> \<Label text=\"RenewGuard AI\"
>
> style=\"-fx-font-size: 22px; -fx-font-weight: 700; -fx-text-fill: #F5F7FA; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \<Label text=\"Pilotage energetique intelligent\"
>
> style=\"-fx-font-size: 13px; -fx-text-fill: #8B93A7; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \</VBox\>
>
> \<VBox spacing=\"6\" style=\"-fx-padding: 0 0 14 0;\"\>
>
> \<Label text=\"Adresse e-mail\"
>
> style=\"-fx-font-size: 12px; -fx-font-weight: 500; -fx-text-fill: #8B93A7; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \<TextField fx:id=\"emailField\" promptText=\"energie@entreprise.fr\" styleClass=\"text-field\"/\>
>
> \</VBox\>
>
> \<VBox spacing=\"6\" style=\"-fx-padding: 0 0 14 0;\"\>
>
> \<Label text=\"Mot de passe\"
>
> style=\"-fx-font-size: 12px; -fx-font-weight: 500; -fx-text-fill: #8B93A7; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \<PasswordField fx:id=\"passwordField\" promptText=\"Mot de passe\" styleClass=\"password-field\"/\>
>
> \</VBox\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"0\" style=\"-fx-padding: 0 0 18 0;\"\>
>
> \<CheckBox fx:id=\"rememberMeBox\" text=\"Rester connecte\" selected=\"true\"
>
> style=\"-fx-font-size: 12px; -fx-text-fill: #8B93A7; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \<Region HBox.hgrow=\"ALWAYS\"/\>
>
> \<Label text=\"Mot de passe oublie ?\"
>
> style=\"-fx-font-size: 12px; -fx-text-fill: #3B9DFF; -fx-cursor: hand; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \</HBox\>
>
> \<HBox fx:id=\"errorRow\" alignment=\"CENTER_LEFT\" spacing=\"8\" visible=\"false\" managed=\"false\"
>
> style=\"-fx-background-color: rgba(255,77,77,0.12);
>
> -fx-border-color: rgba(255,77,77,0.3);
>
> -fx-border-radius: 8; -fx-background-radius: 8;
>
> -fx-border-width: 1; -fx-padding: 8 12 8 12;\"\>
>
> \<HBox.margin\>\<Insets bottom=\"14\"/\>\</HBox.margin\>
>
> \<Label text=\"!\" style=\"-fx-font-size: 12px; -fx-text-fill: #FF4D4D; -fx-font-weight: 700;\"/\>
>
> \<Label fx:id=\"errorLabel\" text=\"\" wrapText=\"true\"
>
> style=\"-fx-font-size: 12px; -fx-text-fill: #FF4D4D; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \</HBox\>
>
> \<Button fx:id=\"submitBtn\" maxWidth=\"Infinity\" defaultButton=\"true\" onAction=\"#onSubmit\"
>
> style=\"-fx-background-color: linear-gradient(to bottom right, #22D3A5, #1BB88E);
>
> -fx-background-radius: 10; -fx-border-color: transparent;
>
> -fx-pref-height: 44; -fx-cursor: hand;
>
> -fx-effect: dropshadow(gaussian, rgba(34,211,165,0.28), 12, 0, 0, 2);\"\>
>
> \<graphic\>
>
> \<Label fx:id=\"submitBtnLabel\" text=\"Se connecter\"
>
> style=\"-fx-font-size: 14px; -fx-font-weight: 700; -fx-text-fill: #0A0E14; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \</graphic\>
>
> \</Button\>
>
> \<HBox alignment=\"CENTER\" spacing=\"6\" style=\"-fx-padding: 18 0 0 0;\"\>
>
> \<Label text=\"Connexion securisee - Chiffrement de bout en bout\"
>
> style=\"-fx-font-size: 11px; -fx-text-fill: #8B93A7; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \</HBox\>
>
> \</VBox\>
>
> \</StackPane\>

**src/main/resources/fr/renewguard/fxml/components/AiDecisionCard.fxml**

> \<?xml version=\"1.0\" encoding=\"UTF-8\"?\>
>
> \<?import javafx.scene.layout.\*?\>
>
> \<?import javafx.scene.control.\*?\>
>
> \<VBox xmlns:fx=\"http://javafx.com/fxml/1\"
>
> fx:id=\"cardRoot\"
>
> fx:controller=\"fr.renewguard.component.AiDecisionCardController\"
>
> styleClass=\"glass-card glass-hover card-glow-blue\"
>
> spacing=\"10\"
>
> style=\"-fx-padding: 20; -fx-border-color: rgba(59,157,255,0.3);
>
> -fx-cursor: hand;\"
>
> maxWidth=\"Infinity\"\>
>
> \<!\-- Header \--\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"8\"\>
>
> \<Label fx:id=\"titleLabel\" text=\"Décision IA récente\"
>
> style=\"-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #3B9DFF;\"
>
> HBox.hgrow=\"ALWAYS\"/\>
>
> \<Label fx:id=\"statusBadge\" text=\"EFFECTUÉ\" styleClass=\"badge-done\"/\>
>
> \</HBox\>
>
> \<!\-- Action \--\>
>
> \<Label fx:id=\"actionLabel\" text=\"Chargement...\"
>
> style=\"-fx-font-size: 13px; -fx-font-weight: 500; -fx-text-fill: #F5F7FA;\"
>
> wrapText=\"true\"/\>
>
> \<!\-- Reason \--\>
>
> \<Label fx:id=\"reasonLabel\" text=\"\"
>
> style=\"-fx-font-size: 12px; -fx-text-fill: #8B93A7; -fx-wrap-text: true;\"
>
> wrapText=\"true\"/\>
>
> \<!\-- Impact \--\>
>
> \<Label fx:id=\"impactLabel\" text=\"\"
>
> style=\"-fx-font-family: \'JetBrains Mono\'; -fx-font-size: 12px;
>
> -fx-font-weight: 600; -fx-text-fill: #22D3A5;\"/\>
>
> \<!\-- Footer row \--\>
>
> \<HBox fx:id=\"detailsRow\" alignment=\"CENTER_LEFT\" spacing=\"10\"\>
>
> \<Label fx:id=\"timestampLabel\" text=\"\"
>
> style=\"-fx-font-family: \'JetBrains Mono\'; -fx-font-size: 11px;
>
> -fx-text-fill: #8B93A7;\" HBox.hgrow=\"ALWAYS\"/\>
>
> \<Label text=\"Confiance :\"
>
> style=\"-fx-font-size: 11px; -fx-text-fill: #8B93A7;\"/\>
>
> \<ProgressBar fx:id=\"confidenceBar\" prefWidth=\"48\" prefHeight=\"5\"
>
> styleClass=\"confidence-bar\" progress=\"0\"/\>
>
> \<Label fx:id=\"confidenceLabel\" text=\"---%\"
>
> style=\"-fx-font-family: \'JetBrains Mono\'; -fx-font-size: 11px;
>
> -fx-font-weight: 700; -fx-text-fill: #22D3A5;\"/\>
>
> \</HBox\>
>
> \</VBox\>

**src/main/resources/fr/renewguard/fxml/components/AlertItem.fxml**

> \<?xml version=\"1.0\" encoding=\"UTF-8\"?\>
>
> \<?import javafx.scene.layout.HBox?\>
>
> \<?import javafx.scene.layout.VBox?\>
>
> \<?import javafx.scene.control.Button?\>
>
> \<?import javafx.scene.control.Label?\>
>
> \<?import javafx.scene.shape.Circle?\>
>
> \<HBox xmlns:fx=\"http://javafx.com/fxml/1\"
>
> fx:id=\"itemRoot\"
>
> fx:controller=\"fr.renewguard.component.AlertItemController\"
>
> alignment=\"TOP_LEFT\" spacing=\"10\" maxWidth=\"Infinity\"
>
> style=\"-fx-padding: 12 14 12 14;
>
> -fx-border-color: transparent transparent #1E2A3A transparent;
>
> -fx-border-width: 1;\"\>
>
> \<Circle fx:id=\"severityDot\" radius=\"4\" style=\"-fx-fill: #FF4D4D;\" translateY=\"6\"/\>
>
> \<VBox HBox.hgrow=\"ALWAYS\" spacing=\"3\" style=\"-fx-min-width: 0;\"\>
>
> \<Label fx:id=\"titleLabel\" text=\"---\" wrapText=\"true\"
>
> style=\"-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #FF4D4D; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \<Label fx:id=\"descLabel\" text=\"---\" wrapText=\"true\"
>
> style=\"-fx-font-size: 11px; -fx-text-fill: #8B93A7; -fx-line-spacing: 1.5; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \<Label fx:id=\"suggestionLabel\" text=\"\" wrapText=\"true\" visible=\"false\" managed=\"false\"
>
> style=\"-fx-font-size: 11px; -fx-text-fill: #3B9DFF; -fx-font-style: italic; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"10\" style=\"-fx-padding: 3 0 0 0;\"\>
>
> \<Label fx:id=\"timeLabel\" text=\"---\"
>
> style=\"-fx-font-family: \'JetBrains Mono\', monospace; -fx-font-size: 10px; -fx-text-fill: #8B93A7;\"/\>
>
> \<!\-- resolveBtn est cable en Java dans AlertItemController.bind(), pas via onAction FXML \--\>
>
> \<Button fx:id=\"resolveBtn\" text=\"Resoudre\"
>
> style=\"-fx-background-color: transparent;
>
> -fx-border-color: #2A3342; -fx-border-radius: 6; -fx-background-radius: 6;
>
> -fx-border-width: 1; -fx-text-fill: #8B93A7; -fx-font-size: 11px; -fx-cursor: hand;
>
> -fx-padding: 3 9 3 9; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \</HBox\>
>
> \</VBox\>
>
> \</HBox\>

**src/main/resources/fr/renewguard/fxml/components/BatteryGauge.fxml**

> \<?xml version=\"1.0\" encoding=\"UTF-8\"?\>
>
> \<?import javafx.scene.layout.\*?\>
>
> \<?import javafx.scene.control.\*?\>
>
> \<?import javafx.scene.canvas.\*?\>
>
> \<HBox xmlns:fx=\"http://javafx.com/fxml/1\"
>
> fx:controller=\"fr.renewguard.component.BatteryGaugeController\"
>
> styleClass=\"glass-card glass-hover\"
>
> alignment=\"CENTER_LEFT\"
>
> spacing=\"20\"
>
> style=\"-fx-padding: 20; -fx-cursor: hand;\"
>
> maxWidth=\"Infinity\"\>
>
> \<!\-- Radial arc canvas \--\>
>
> \<Canvas fx:id=\"gaugeCanvas\" width=\"92\" height=\"92\"/\>
>
> \<!\-- Text info \--\>
>
> \<VBox spacing=\"4\"\>
>
> \<Label text=\"Batterie de stockage\"
>
> style=\"-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #8B93A7;\"/\>
>
> \<Label fx:id=\"percentLabel\" text=\"---%\"
>
> style=\"-fx-font-family: \'JetBrains Mono\'; -fx-font-size: 22px;
>
> -fx-font-weight: 700; -fx-text-fill: #3B9DFF;\"/\>
>
> \<Label text=\"Autonomie estimée\"
>
> style=\"-fx-font-size: 12px; -fx-text-fill: #8B93A7;\"/\>
>
> \<Label fx:id=\"etaLabel\" text=\"---\"
>
> style=\"-fx-font-family: \'JetBrains Mono\'; -fx-font-size: 20px;
>
> -fx-font-weight: 700; -fx-text-fill: #F5F7FA;\"/\>
>
> \<Label fx:id=\"trendLabel\" text=\"\"
>
> style=\"-fx-font-size: 11px; -fx-text-fill: #22D3A5;\"/\>
>
> \</VBox\>
>
> \</HBox\>

**src/main/resources/fr/renewguard/fxml/components/ChatBubble.fxml**

> \<?xml version=\"1.0\" encoding=\"UTF-8\"?\>
>
> \<?import javafx.scene.layout.HBox?\>
>
> \<?import javafx.scene.layout.VBox?\>
>
> \<?import javafx.scene.control.Label?\>
>
> \<HBox xmlns:fx=\"http://javafx.com/fxml/1\"
>
> fx:id=\"bubbleRoot\"
>
> fx:controller=\"fr.renewguard.component.ChatBubbleController\"
>
> maxWidth=\"Infinity\" fillHeight=\"false\"
>
> style=\"-fx-alignment: CENTER_LEFT;\"\>
>
> \<VBox fx:id=\"bubble\" spacing=\"2\"
>
> style=\"-fx-background-color: rgba(21,27,38,0.92);
>
> -fx-border-color: #2A3342; -fx-border-radius: 16 16 16 4; -fx-background-radius: 16 16 16 4;
>
> -fx-border-width: 1; -fx-padding: 10 14 10 14; -fx-max-width: 300;\"\>
>
> \<Label fx:id=\"textLabel\" text=\"\" wrapText=\"true\"
>
> style=\"-fx-font-size: 13px; -fx-text-fill: #F5F7FA; -fx-line-spacing: 2; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \<Label fx:id=\"timeLabel\" text=\"---\"
>
> style=\"-fx-font-size: 10px; -fx-text-fill: #8B93A7; -fx-font-family: \'JetBrains Mono\', monospace; -fx-padding: 3 0 0 0;\"/\>
>
> \</VBox\>
>
> \</HBox\>

**src/main/resources/fr/renewguard/fxml/components/EquipmentRow.fxml**

> \<?xml version=\"1.0\" encoding=\"UTF-8\"?\>
>
> \<?import javafx.scene.layout.HBox?\>
>
> \<?import org.controlsfx.control.ToggleSwitch?\>
>
> \<HBox xmlns:fx=\"http://javafx.com/fxml/1\"
>
> fx:controller=\"fr.renewguard.component.EquipmentRowController\"
>
> alignment=\"CENTER\"
>
> style=\"-fx-padding: 0 8 0 8;\"\>
>
> \<ToggleSwitch fx:id=\"toggleSwitch\" style=\"-fx-selected-color: #22D3A5; -fx-unselected-color: #2A3342;\"/\>
>
> \</HBox\>

**src/main/resources/fr/renewguard/fxml/components/KpiCard.fxml**

> \<?xml version=\"1.0\" encoding=\"UTF-8\"?\>
>
> \<?import javafx.scene.layout.\*?\>
>
> \<?import javafx.scene.control.\*?\>
>
> \<VBox xmlns:fx=\"http://javafx.com/fxml/1\"
>
> fx:id=\"cardRoot\"
>
> fx:controller=\"fr.renewguard.component.KpiCardController\"
>
> styleClass=\"glass-card glass-hover\"
>
> spacing=\"4\"
>
> style=\"-fx-padding: 18 20 18 20; -fx-cursor: hand;\"
>
> maxWidth=\"Infinity\"\>
>
> \<!\-- Header row: label + icon \--\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"4\"\>
>
> \<Label fx:id=\"labelText\" text=\"---\" styleClass=\"kpi-label\" HBox.hgrow=\"ALWAYS\"/\>
>
> \<Label fx:id=\"iconLabel\" text=\"\" style=\"-fx-font-size: 17px;\"/\>
>
> \</HBox\>
>
> \<!\-- Value row \--\>
>
> \<HBox fx:id=\"valueRow\" alignment=\"BASELINE_LEFT\" spacing=\"6\"\>
>
> \<Label fx:id=\"valueLabel\" text=\"---\" styleClass=\"kpi-value\"/\>
>
> \<Label fx:id=\"unitLabel\" text=\"\" styleClass=\"kpi-unit\"/\>
>
> \</HBox\>
>
> \<!\-- Trend \--\>
>
> \<Label fx:id=\"trendLabel\" text=\"\" styleClass=\"kpi-sub\"/\>
>
> \</VBox\>

**src/main/resources/fr/renewguard/fxml/components/PriorityKanbanCard.fxml**

> \<?xml version=\"1.0\" encoding=\"UTF-8\"?\>
>
> \<?import javafx.scene.layout.HBox?\>
>
> \<?import javafx.scene.layout.VBox?\>
>
> \<?import javafx.scene.control.Label?\>
>
> \<?import javafx.scene.shape.Circle?\>
>
> \<HBox xmlns:fx=\"http://javafx.com/fxml/1\"
>
> fx:id=\"cardRoot\"
>
> fx:controller=\"fr.renewguard.component.PriorityKanbanCardController\"
>
> alignment=\"CENTER_LEFT\" spacing=\"10\" maxWidth=\"Infinity\"
>
> style=\"-fx-background-color: rgba(21,27,38,0.82);
>
> -fx-border-color: #2A3342; -fx-border-radius: 10; -fx-background-radius: 10;
>
> -fx-border-width: 1; -fx-padding: 10 12 10 12; -fx-cursor: open-hand;
>
> -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.22), 6, 0, 0, 2);\"\>
>
> \<Label text=\":::\" style=\"-fx-text-fill: #8B93A7; -fx-font-size: 12px; -fx-padding: 0 2 0 0;\"/\>
>
> \<Label fx:id=\"iconLabel\" text=\"device\" style=\"-fx-font-size: 12px; -fx-text-fill: #8B93A7;\"/\>
>
> \<VBox spacing=\"2\" HBox.hgrow=\"ALWAYS\"\>
>
> \<Label fx:id=\"nameLabel\" text=\"---\"
>
> style=\"-fx-font-size: 12px; -fx-font-weight: 500; -fx-text-fill: #F5F7FA; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \<Label fx:id=\"powerLabel\" text=\"--- W\"
>
> style=\"-fx-font-family: \'JetBrains Mono\', monospace; -fx-font-size: 11px; -fx-text-fill: #8B93A7;\"/\>
>
> \</VBox\>
>
> \<VBox alignment=\"CENTER\" spacing=\"3\"\>
>
> \<Circle fx:id=\"statusCircle\" radius=\"4\" style=\"-fx-fill: #22D3A5;\"/\>
>
> \<Label fx:id=\"statusDot\" text=\"Actif\"
>
> style=\"-fx-font-size: 10px; -fx-font-weight: 500; -fx-text-fill: #22D3A5; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \</VBox\>
>
> \</HBox\>

**src/main/resources/fr/renewguard/fxml/Dashboard.fxml**

> \<?xml version=\"1.0\" encoding=\"UTF-8\"?\>
>
> \<?import javafx.scene.layout.\*?\>
>
> \<?import javafx.scene.control.\*?\>
>
> \<?import javafx.scene.chart.\*?\>
>
> \<?import javafx.scene.shape.\*?\>
>
> \<VBox xmlns:fx=\"http://javafx.com/fxml/1\"
>
> fx:controller=\"fr.renewguard.controller.DashboardController\"
>
> styleClass=\"screen-root\"
>
> spacing=\"16\"
>
> style=\"-fx-padding: 24; -fx-background-color: transparent;\"\>
>
> \<!\-- ── Row 1 : Status bar ────────────────────────────────────── \--\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"10\"\>
>
> \<Circle fx:id=\"statusDot\" radius=\"4.5\"
>
> style=\"-fx-fill: #22D3A5;
>
> -fx-effect: dropshadow(gaussian,rgba(34,211,165,0.7),6,0,0,0);\"/\>
>
> \<Label text=\"Tour Centrale --- Paris 15e\"
>
> style=\"-fx-font-size: 13px; -fx-font-weight: 500; -fx-text-fill: #F5F7FA;\"/\>
>
> \<Label fx:id=\"statusBadge\" text=\"OPÉRATIONNEL\" styleClass=\"badge-success\"/\>
>
> \<Region HBox.hgrow=\"ALWAYS\"/\>
>
> \<Button fx:id=\"systemViewBtn\" text=\"Vue système\"
>
> styleClass=\"btn-secondary\" onAction=\"#onSystemViewToggle\"/\>
>
> \</HBox\>
>
> \<!\-- ── Row 2 : System view (hidden by default) ───────────────── \--\>
>
> \<VBox fx:id=\"systemViewPanel\" styleClass=\"glass-card\"
>
> visible=\"false\" managed=\"false\" spacing=\"0\"\>
>
> \<Label text=\"Architecture système --- Flux de données temps réel\"
>
> style=\"-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #3B9DFF;
>
> -fx-padding: 14 18 10 18;\"/\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"6\"
>
> style=\"-fx-padding: 0 18 16 18;\"\>
>
> \<VBox alignment=\"CENTER\" spacing=\"3\" styleClass=\"system-node\"\>
>
> \<Label text=\"☀️\" styleClass=\"system-node-icon\"/\>
>
> \<Label text=\"Panneaux solaires\" styleClass=\"system-node-label\"/\>
>
> \<Label text=\"48 panneaux · 18,4 kW\" styleClass=\"system-node-sub\"/\>
>
> \<Circle radius=\"3\" style=\"-fx-fill: #22D3A5;\"/\>
>
> \</VBox\>
>
> \<Label text=\"→\" styleClass=\"system-arrow\"/\>
>
> \<VBox alignment=\"CENTER\" spacing=\"3\" styleClass=\"system-node\"\>
>
> \<Label text=\"📡\" styleClass=\"system-node-icon\"/\>
>
> \<Label text=\"Capteurs IoT ESP32\" styleClass=\"system-node-label\"/\>
>
> \<Label text=\"12 nœuds · actifs\" styleClass=\"system-node-sub\"/\>
>
> \<Circle radius=\"3\" style=\"-fx-fill: #22D3A5;\"/\>
>
> \</VBox\>
>
> \<Label text=\"→\" styleClass=\"system-arrow\"/\>
>
> \<VBox alignment=\"CENTER\" spacing=\"3\" styleClass=\"system-node\"\>
>
> \<Label text=\"🔄\" styleClass=\"system-node-icon\"/\>
>
> \<Label text=\"Collecte temps réel\" styleClass=\"system-node-label\"/\>
>
> \<Label text=\"Latence 42 ms\" styleClass=\"system-node-sub\"/\>
>
> \<Circle radius=\"3\" style=\"-fx-fill: #22D3A5;\"/\>
>
> \</VBox\>
>
> \<Label text=\"→\" styleClass=\"system-arrow\"/\>
>
> \<VBox alignment=\"CENTER\" spacing=\"3\" styleClass=\"system-node\"\>
>
> \<Label text=\"☁️\" styleClass=\"system-node-icon\"/\>
>
> \<Label text=\"API REST FastAPI\" styleClass=\"system-node-label\"/\>
>
> \<Label text=\"Sync 99,8% uptime\" styleClass=\"system-node-sub\"/\>
>
> \<Circle radius=\"3\" style=\"-fx-fill: #22D3A5;\"/\>
>
> \</VBox\>
>
> \<Label text=\"→\" styleClass=\"system-arrow\"/\>
>
> \<VBox alignment=\"CENTER\" spacing=\"3\" styleClass=\"system-node\"\>
>
> \<Label text=\"🧠\" styleClass=\"system-node-icon\"/\>
>
> \<Label text=\"Machine Learning\" styleClass=\"system-node-label\"/\>
>
> \<Label text=\"Modèle v3.1 actif\" styleClass=\"system-node-sub\"/\>
>
> \<Circle radius=\"3\" style=\"-fx-fill: #3B9DFF;\"/\>
>
> \</VBox\>
>
> \<Label text=\"→\" styleClass=\"system-arrow\"/\>
>
> \<VBox alignment=\"CENTER\" spacing=\"3\" styleClass=\"system-node\"\>
>
> \<Label text=\"⚡\" styleClass=\"system-node-icon\"/\>
>
> \<Label text=\"Décisions IA\" styleClass=\"system-node-label\"/\>
>
> \<Label text=\"7 actions aujourd\'hui\" styleClass=\"system-node-sub\"/\>
>
> \<Circle radius=\"3\" style=\"-fx-fill: #3B9DFF;\"/\>
>
> \</VBox\>
>
> \<Label text=\"→\" styleClass=\"system-arrow\"/\>
>
> \<VBox alignment=\"CENTER\" spacing=\"3\" styleClass=\"system-node\"\>
>
> \<Label text=\"🔌\" styleClass=\"system-node-icon\"/\>
>
> \<Label text=\"Relais équipements\" styleClass=\"system-node-label\"/\>
>
> \<Label text=\"23 relais · 2 OFF\" styleClass=\"system-node-sub\"/\>
>
> \<Circle radius=\"3\" style=\"-fx-fill: #FFA53E;\"/\>
>
> \</VBox\>
>
> \</HBox\>
>
> \</VBox\>
>
> \<!\-- ── Row 3 : 4 KPI cards ───────────────────────────────────── \--\>
>
> \<HBox spacing=\"16\"\>
>
> \<StackPane fx:id=\"kpiSolarSlot\" HBox.hgrow=\"ALWAYS\" minHeight=\"110\"/\>
>
> \<StackPane fx:id=\"kpiConsumptionSlot\" HBox.hgrow=\"ALWAYS\" minHeight=\"110\"/\>
>
> \<StackPane fx:id=\"kpiScoreSlot\" HBox.hgrow=\"ALWAYS\" minHeight=\"110\"/\>
>
> \<StackPane fx:id=\"kpiEquipmentSlot\" HBox.hgrow=\"ALWAYS\" minHeight=\"110\"/\>
>
> \</HBox\>
>
> \<!\-- ── Row 4 : Battery / Network / AI decision ───────────────── \--\>
>
> \<HBox spacing=\"16\"\>
>
> \<!\-- Battery gauge component slot \--\>
>
> \<StackPane fx:id=\"batterySlot\" HBox.hgrow=\"ALWAYS\" minHeight=\"140\"/\>
>
> \<!\-- Network card (inline) \--\>
>
> \<VBox styleClass=\"glass-card\" HBox.hgrow=\"ALWAYS\"
>
> spacing=\"10\" style=\"-fx-padding: 20;\"\>
>
> \<Label text=\"État réseau électrique\" styleClass=\"card-title-muted\"/\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"8\"\>
>
> \<Circle fx:id=\"networkDot\" radius=\"5\"
>
> style=\"-fx-fill: #22D3A5;
>
> -fx-effect: dropshadow(gaussian,rgba(34,211,165,0.6),6,0,0,0);\"/\>
>
> \<Label fx:id=\"networkStatusLabel\" text=\"Réseau disponible\"
>
> style=\"-fx-font-size: 15px; -fx-font-weight: 700; -fx-text-fill: #22D3A5;\"/\>
>
> \</HBox\>
>
> \<GridPane hgap=\"20\" vgap=\"8\"\>
>
> \<Label text=\"Tension secteur\" styleClass=\"data-label\"
>
> GridPane.columnIndex=\"0\" GridPane.rowIndex=\"0\"/\>
>
> \<Label fx:id=\"networkVoltageLabel\" text=\"--- V\" styleClass=\"data-value-green\"
>
> GridPane.columnIndex=\"1\" GridPane.rowIndex=\"0\"/\>
>
> \<Label text=\"Fréquence\" styleClass=\"data-label\"
>
> GridPane.columnIndex=\"0\" GridPane.rowIndex=\"1\"/\>
>
> \<Label fx:id=\"networkFrequencyLabel\" text=\"--- Hz\" styleClass=\"data-value-green\"
>
> GridPane.columnIndex=\"1\" GridPane.rowIndex=\"1\"/\>
>
> \<Label text=\"Import réseau\" styleClass=\"data-label\"
>
> GridPane.columnIndex=\"0\" GridPane.rowIndex=\"2\"/\>
>
> \<Label fx:id=\"networkImportLabel\" text=\"--- kW\" styleClass=\"data-value-amber\"
>
> GridPane.columnIndex=\"1\" GridPane.rowIndex=\"2\"/\>
>
> \</GridPane\>
>
> \</VBox\>
>
> \<!\-- AI decision card component slot \--\>
>
> \<StackPane fx:id=\"aiDecisionSlot\" HBox.hgrow=\"ALWAYS\" minHeight=\"140\"/\>
>
> \</HBox\>
>
> \<!\-- ── Row 5 : Main chart + Donut ────────────────────────────── \--\>
>
> \<HBox spacing=\"16\" VBox.vgrow=\"ALWAYS\"\>
>
> \<!\-- Area chart \--\>
>
> \<VBox styleClass=\"glass-card\" HBox.hgrow=\"ALWAYS\" spacing=\"0\"
>
> style=\"-fx-padding: 20;\"\>
>
> \<HBox alignment=\"CENTER_LEFT\" style=\"-fx-padding: 0 0 14 0;\"\>
>
> \<Label text=\"Production vs Consommation\"
>
> styleClass=\"card-title\"/\>
>
> \<Region HBox.hgrow=\"ALWAYS\"/\>
>
> \<HBox spacing=\"4\"\>
>
> \<Button fx:id=\"btnDay\" text=\"Jour\" onAction=\"#onPeriodDay\"/\>
>
> \<Button fx:id=\"btnWeek\" text=\"Semaine\" onAction=\"#onPeriodWeek\"/\>
>
> \<Button fx:id=\"btnMonth\" text=\"Mois\" onAction=\"#onPeriodMonth\"/\>
>
> \</HBox\>
>
> \</HBox\>
>
> \<HBox spacing=\"16\" style=\"-fx-padding: 0 0 10 0;\"\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"6\"\>
>
> \<Region prefWidth=\"12\" prefHeight=\"3\"
>
> style=\"-fx-background-color: #22D3A5; -fx-background-radius: 2;\"/\>
>
> \<Label text=\"Production solaire\" styleClass=\"legend-label\"/\>
>
> \</HBox\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"6\"\>
>
> \<Region prefWidth=\"12\" prefHeight=\"3\"
>
> style=\"-fx-background-color: #3B9DFF; -fx-background-radius: 2;\"/\>
>
> \<Label text=\"Consommation\" styleClass=\"legend-label\"/\>
>
> \</HBox\>
>
> \</HBox\>
>
> \<AreaChart fx:id=\"mainChart\" VBox.vgrow=\"ALWAYS\"
>
> prefHeight=\"200\" animated=\"false\" legendVisible=\"false\"\>
>
> \<xAxis\>\<CategoryAxis/\>\</xAxis\>
>
> \<yAxis\>\<NumberAxis/\>\</yAxis\>
>
> \</AreaChart\>
>
> \</VBox\>
>
> \<!\-- Donut + summary \--\>
>
> \<VBox styleClass=\"glass-card\" prefWidth=\"300\" minWidth=\"260\" spacing=\"0\"
>
> style=\"-fx-padding: 20;\"\>
>
> \<Label text=\"Répartition énergétique\" styleClass=\"card-title\"
>
> style=\"-fx-padding: 0 0 14 0;\"/\>
>
> \<PieChart fx:id=\"pieChart\" prefHeight=\"150\"
>
> labelsVisible=\"false\" legendVisible=\"false\"
>
> animated=\"true\" VBox.vgrow=\"NEVER\"/\>
>
> \<VBox spacing=\"8\" style=\"-fx-padding: 12 0 0 0;\"\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"8\"\>
>
> \<Circle radius=\"5\" style=\"-fx-fill: #22D3A5;\"/\>
>
> \<Label text=\"Solaire\" styleClass=\"data-label\" HBox.hgrow=\"ALWAYS\"/\>
>
> \</HBox\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"8\"\>
>
> \<Circle radius=\"5\" style=\"-fx-fill: #3B9DFF;\"/\>
>
> \<Label text=\"Batterie\" styleClass=\"data-label\" HBox.hgrow=\"ALWAYS\"/\>
>
> \</HBox\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"8\"\>
>
> \<Circle radius=\"5\" style=\"-fx-fill: #FFA53E;\"/\>
>
> \<Label text=\"Réseau\" styleClass=\"data-label\" HBox.hgrow=\"ALWAYS\"/\>
>
> \</HBox\>
>
> \</VBox\>
>
> \<Separator styleClass=\"separator-dark\" style=\"-fx-padding: 12 0 4 0;\"/\>
>
> \<HBox style=\"-fx-padding: 12 0 0 0;\"\>
>
> \<VBox alignment=\"CENTER\" HBox.hgrow=\"ALWAYS\" spacing=\"3\"\>
>
> \<Label fx:id=\"energySavedLabel\" text=\"---\"
>
> style=\"-fx-font-family: \'JetBrains Mono\'; -fx-font-size: 18px;
>
> -fx-font-weight: 700; -fx-text-fill: #22D3A5;\"/\>
>
> \<Label text=\"kWh économisés\"
>
> style=\"-fx-font-size: 10px; -fx-text-fill: #8B93A7;\"/\>
>
> \</VBox\>
>
> \<Separator orientation=\"VERTICAL\" styleClass=\"separator-dark\"/\>
>
> \<VBox alignment=\"CENTER\" HBox.hgrow=\"ALWAYS\" spacing=\"3\"\>
>
> \<Label fx:id=\"co2AvoidedLabel\" text=\"---\"
>
> style=\"-fx-font-family: \'JetBrains Mono\'; -fx-font-size: 18px;
>
> -fx-font-weight: 700; -fx-text-fill: #22D3A5;\"/\>
>
> \<Label text=\"CO₂ évité\"
>
> style=\"-fx-font-size: 10px; -fx-text-fill: #8B93A7;\"/\>
>
> \</VBox\>
>
> \</HBox\>
>
> \</VBox\>
>
> \</HBox\>
>
> \</VBox\>

**src/main/resources/fr/renewguard/fxml/Equipment.fxml**

> \<?xml version=\"1.0\" encoding=\"UTF-8\"?\>
>
> \<?import javafx.scene.layout.VBox?\>
>
> \<?import javafx.scene.layout.HBox?\>
>
> \<?import javafx.scene.layout.Region?\>
>
> \<?import javafx.scene.control.Button?\>
>
> \<?import javafx.scene.control.Label?\>
>
> \<?import javafx.scene.control.TextField?\>
>
> \<?import javafx.scene.control.TableView?\>
>
> \<?import javafx.scene.control.TableColumn?\>
>
> \<?import javafx.scene.control.Tooltip?\>
>
> \<?import javafx.scene.control.Separator?\>
>
> \<VBox xmlns:fx=\"http://javafx.com/fxml/1\"
>
> fx:controller=\"fr.renewguard.controller.EquipmentController\"
>
> spacing=\"16\"
>
> style=\"-fx-padding: 24; -fx-background-color: transparent;\"\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"12\"\>
>
> \<Label fx:id=\"statsLabel\" text=\"--- equipements\"
>
> style=\"-fx-font-size: 13px; -fx-font-weight: 500; -fx-text-fill: #F5F7FA; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \<Region HBox.hgrow=\"ALWAYS\"/\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"0\" maxWidth=\"280\"
>
> style=\"-fx-background-color: rgba(255,255,255,0.04);
>
> -fx-border-color: #2A3342; -fx-border-radius: 8; -fx-background-radius: 8;
>
> -fx-border-width: 1; -fx-padding: 0 10 0 10;\"\>
>
> \<TextField fx:id=\"searchField\" promptText=\"Rechercher\...\" HBox.hgrow=\"ALWAYS\"
>
> style=\"-fx-background-color: transparent; -fx-border-color: transparent;
>
> -fx-text-fill: #F5F7FA; -fx-prompt-text-fill: #8B93A7;
>
> -fx-font-size: 12px; -fx-font-family: \'Inter\', sans-serif; -fx-padding: 8 0 8 0;\"/\>
>
> \</HBox\>
>
> \<Button fx:id=\"addBtn\" text=\"+ Ajouter\" onAction=\"#onAdd\"
>
> style=\"-fx-background-color: linear-gradient(to bottom right, #22D3A5, #1BB88E);
>
> -fx-background-radius: 8; -fx-border-color: transparent;
>
> -fx-text-fill: #0A0E14; -fx-font-size: 13px; -fx-font-weight: 700;
>
> -fx-cursor: hand; -fx-padding: 8 16 8 16; -fx-font-family: \'Inter\', sans-serif;\"\>
>
> \<tooltip\>\<Tooltip text=\"Ctrl+N\"/\>\</tooltip\>
>
> \</Button\>
>
> \<Button text=\"Actualiser\" onAction=\"#onRefresh\"
>
> style=\"-fx-background-color: rgba(255,255,255,0.04);
>
> -fx-border-color: #2A3342; -fx-border-radius: 8;
>
> -fx-background-radius: 8; -fx-border-width: 1;
>
> -fx-text-fill: #8B93A7; -fx-cursor: hand; -fx-font-size: 12px;\"\>
>
> \<tooltip\>\<Tooltip text=\"Actualiser (F5)\"/\>\</tooltip\>
>
> \</Button\>
>
> \</HBox\>
>
> \<HBox spacing=\"8\" alignment=\"CENTER_LEFT\"\>
>
> \<Button fx:id=\"chipAll\" text=\"Tous\" onAction=\"#onChipAll\" styleClass=\"chip-active\"/\>
>
> \<Button fx:id=\"chipCritical\" text=\"Critique\" onAction=\"#onChipCritical\" styleClass=\"chip\"/\>
>
> \<Button fx:id=\"chipImportant\" text=\"Important\" onAction=\"#onChipImportant\" styleClass=\"chip\"/\>
>
> \<Button fx:id=\"chipLow\" text=\"Non prioritaire\" onAction=\"#onChipLow\" styleClass=\"chip\"/\>
>
> \<Separator orientation=\"VERTICAL\" prefHeight=\"22\" style=\"-fx-padding: 0 2 0 2;\"/\>
>
> \<Button fx:id=\"chipActive\" text=\"Actifs\" onAction=\"#onChipActive\" styleClass=\"chip\"/\>
>
> \<Button fx:id=\"chipInactive\" text=\"Inactifs\" onAction=\"#onChipInactive\" styleClass=\"chip\"/\>
>
> \</HBox\>
>
> \<VBox styleClass=\"glass-card\" VBox.vgrow=\"ALWAYS\" style=\"-fx-background-radius: 16; -fx-padding: 0; -fx-border-radius: 16;\"\>
>
> \<TableView fx:id=\"table\" VBox.vgrow=\"ALWAYS\" styleClass=\"table-dark\"
>
> style=\"-fx-background-color: transparent; -fx-border-color: transparent;\"\>
>
> \<columnResizePolicy\>
>
> \<TableView fx:constant=\"CONSTRAINED_RESIZE_POLICY\"/\>
>
> \</columnResizePolicy\>
>
> \<placeholder\>
>
> \<Label text=\"Aucun equipement trouve.\" style=\"-fx-text-fill: #8B93A7; -fx-font-size: 13px;\"/\>
>
> \</placeholder\>
>
> \<columns\>
>
> \<TableColumn fx:id=\"colName\" text=\"Equipement\" prefWidth=\"220\" minWidth=\"160\"/\>
>
> \<TableColumn fx:id=\"colLocation\" text=\"Emplacement\" prefWidth=\"150\" minWidth=\"100\"/\>
>
> \<TableColumn fx:id=\"colPriority\" text=\"Priorite\" prefWidth=\"130\" minWidth=\"110\"/\>
>
> \<TableColumn fx:id=\"colPower\" text=\"Consommation\" prefWidth=\"120\" minWidth=\"90\"/\>
>
> \<TableColumn fx:id=\"colStatus\" text=\"Statut\" prefWidth=\"130\" minWidth=\"100\"/\>
>
> \<TableColumn fx:id=\"colActivity\" text=\"Derniere activite\" prefWidth=\"150\" minWidth=\"110\"/\>
>
> \<TableColumn fx:id=\"colToggle\" text=\"Controle\" prefWidth=\"80\" minWidth=\"70\" sortable=\"false\"/\>
>
> \</columns\>
>
> \</TableView\>
>
> \</VBox\>
>
> \</VBox\>

**src/main/resources/fr/renewguard/fxml/History.fxml**

> \<?xml version=\"1.0\" encoding=\"UTF-8\"?\>
>
> \<?import javafx.scene.layout.VBox?\>
>
> \<?import javafx.scene.layout.HBox?\>
>
> \<?import javafx.scene.layout.GridPane?\>
>
> \<?import javafx.scene.layout.Region?\>
>
> \<?import javafx.scene.layout.ColumnConstraints?\>
>
> \<?import javafx.scene.layout.RowConstraints?\>
>
> \<?import javafx.scene.control.Button?\>
>
> \<?import javafx.scene.control.Label?\>
>
> \<?import javafx.scene.control.ScrollPane?\>
>
> \<?import javafx.scene.chart.AreaChart?\>
>
> \<?import javafx.scene.chart.BarChart?\>
>
> \<?import javafx.scene.chart.CategoryAxis?\>
>
> \<?import javafx.scene.chart.NumberAxis?\>
>
> \<HBox xmlns:fx=\"http://javafx.com/fxml/1\"
>
> fx:controller=\"fr.renewguard.controller.HistoryController\"
>
> spacing=\"16\"
>
> style=\"-fx-padding: 24; -fx-background-color: transparent;\"\>
>
> \<VBox HBox.hgrow=\"ALWAYS\" spacing=\"14\" style=\"-fx-min-width: 0;\"\>
>
> \<HBox spacing=\"8\" alignment=\"CENTER_LEFT\"\>
>
> \<Button fx:id=\"chipDay\" text=\"Jour\" onAction=\"#onChipDay\" styleClass=\"chip\"/\>
>
> \<Button fx:id=\"chipWeek\" text=\"Semaine\" onAction=\"#onChipWeek\" styleClass=\"chip-active\"/\>
>
> \<Button fx:id=\"chipMonth\" text=\"Mois\" onAction=\"#onChipMonth\" styleClass=\"chip\"/\>
>
> \<Button fx:id=\"chipYear\" text=\"Annee\" onAction=\"#onChipYear\" styleClass=\"chip\"/\>
>
> \<Region HBox.hgrow=\"ALWAYS\"/\>
>
> \<Button fx:id=\"exportCsvBtn\" text=\"CSV\" onAction=\"#onExportCsv\"
>
> style=\"-fx-background-color: rgba(59,157,255,0.10); -fx-border-color: rgba(59,157,255,0.35);
>
> -fx-border-radius: 8; -fx-background-radius: 8; -fx-border-width: 1; -fx-text-fill: #3B9DFF;
>
> -fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 6 12 6 12;\"/\>
>
> \<Button fx:id=\"exportPdfBtn\" text=\"PDF\" onAction=\"#onExportPdf\"
>
> style=\"-fx-background-color: rgba(59,157,255,0.10); -fx-border-color: rgba(59,157,255,0.35);
>
> -fx-border-radius: 8; -fx-background-radius: 8; -fx-border-width: 1; -fx-text-fill: #3B9DFF;
>
> -fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 6 12 6 12;\"/\>
>
> \</HBox\>
>
> \<HBox spacing=\"12\"\>
>
> \<VBox HBox.hgrow=\"ALWAYS\" spacing=\"4\"
>
> style=\"-fx-background-color: rgba(21,27,38,0.75); -fx-border-color: rgba(34,211,165,0.28);
>
> -fx-border-radius: 14; -fx-background-radius: 14; -fx-border-width: 1; -fx-padding: 14;\"\>
>
> \<Label text=\"ENERGIE ECONOMISEE\" style=\"-fx-font-size: 10px; -fx-font-weight: 700; -fx-text-fill: #8B93A7;\"/\>
>
> \<Label fx:id=\"kpiSaved\" text=\"---\"
>
> style=\"-fx-font-size: 22px; -fx-font-weight: 700; -fx-text-fill: #22D3A5; -fx-font-family: \'JetBrains Mono\', monospace;\"/\>
>
> \</VBox\>
>
> \<VBox HBox.hgrow=\"ALWAYS\" spacing=\"4\"
>
> style=\"-fx-background-color: rgba(21,27,38,0.75); -fx-border-color: #2A3342;
>
> -fx-border-radius: 14; -fx-background-radius: 14; -fx-border-width: 1; -fx-padding: 14;\"\>
>
> \<Label text=\"CO2 EVITE\" style=\"-fx-font-size: 10px; -fx-font-weight: 700; -fx-text-fill: #8B93A7;\"/\>
>
> \<Label fx:id=\"kpiCo2\" text=\"---\"
>
> style=\"-fx-font-size: 22px; -fx-font-weight: 700; -fx-text-fill: #3B9DFF; -fx-font-family: \'JetBrains Mono\', monospace;\"/\>
>
> \</VBox\>
>
> \<VBox HBox.hgrow=\"ALWAYS\" spacing=\"4\"
>
> style=\"-fx-background-color: rgba(21,27,38,0.75); -fx-border-color: #2A3342;
>
> -fx-border-radius: 14; -fx-background-radius: 14; -fx-border-width: 1; -fx-padding: 14;\"\>
>
> \<Label text=\"COUVERTURE SOLAIRE\" style=\"-fx-font-size: 10px; -fx-font-weight: 700; -fx-text-fill: #8B93A7;\"/\>
>
> \<Label fx:id=\"kpiSolar\" text=\"---\"
>
> style=\"-fx-font-size: 22px; -fx-font-weight: 700; -fx-text-fill: #FFA53E; -fx-font-family: \'JetBrains Mono\', monospace;\"/\>
>
> \</VBox\>
>
> \<VBox HBox.hgrow=\"ALWAYS\" spacing=\"4\"
>
> style=\"-fx-background-color: rgba(21,27,38,0.75); -fx-border-color: rgba(34,211,165,0.28);
>
> -fx-border-radius: 14; -fx-background-radius: 14; -fx-border-width: 1; -fx-padding: 14;\"\>
>
> \<Label text=\"BENEFICE NET\" style=\"-fx-font-size: 10px; -fx-font-weight: 700; -fx-text-fill: #8B93A7;\"/\>
>
> \<Label fx:id=\"kpiNet\" text=\"---\"
>
> style=\"-fx-font-size: 22px; -fx-font-weight: 700; -fx-text-fill: #22D3A5; -fx-font-family: \'JetBrains Mono\', monospace;\"/\>
>
> \</VBox\>
>
> \</HBox\>
>
> \<GridPane hgap=\"14\" vgap=\"14\" VBox.vgrow=\"ALWAYS\"\>
>
> \<columnConstraints\>
>
> \<ColumnConstraints hgrow=\"ALWAYS\" percentWidth=\"50\"/\>
>
> \<ColumnConstraints hgrow=\"ALWAYS\" percentWidth=\"50\"/\>
>
> \</columnConstraints\>
>
> \<rowConstraints\>
>
> \<RowConstraints vgrow=\"ALWAYS\" percentHeight=\"50\"/\>
>
> \<RowConstraints vgrow=\"ALWAYS\" percentHeight=\"50\"/\>
>
> \</rowConstraints\>
>
> \<VBox GridPane.columnIndex=\"0\" GridPane.rowIndex=\"0\" spacing=\"8\"
>
> style=\"-fx-background-color: rgba(21,27,38,0.75); -fx-border-color: #2A3342;
>
> -fx-border-radius: 14; -fx-background-radius: 14; -fx-border-width: 1; -fx-padding: 14;\"\>
>
> \<Label text=\"Production solaire\" style=\"-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #22D3A5;\"/\>
>
> \<AreaChart fx:id=\"productionChart\" VBox.vgrow=\"ALWAYS\" animated=\"false\" createSymbols=\"false\" legendVisible=\"false\"
>
> style=\"-fx-background-color: transparent; -fx-padding: 0;\"\>
>
> \<xAxis\>\<CategoryAxis style=\"-fx-tick-label-fill: #8B93A7;\"/\>\</xAxis\>
>
> \<yAxis\>\<NumberAxis label=\"kWh\" style=\"-fx-tick-label-fill: #8B93A7;\"/\>\</yAxis\>
>
> \</AreaChart\>
>
> \</VBox\>
>
> \<VBox GridPane.columnIndex=\"1\" GridPane.rowIndex=\"0\" spacing=\"8\"
>
> style=\"-fx-background-color: rgba(21,27,38,0.75); -fx-border-color: #2A3342;
>
> -fx-border-radius: 14; -fx-background-radius: 14; -fx-border-width: 1; -fx-padding: 14;\"\>
>
> \<Label text=\"Production vs Consommation\" style=\"-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #F5F7FA;\"/\>
>
> \<AreaChart fx:id=\"consumptionChart\" VBox.vgrow=\"ALWAYS\" animated=\"false\" createSymbols=\"false\" legendVisible=\"true\"
>
> style=\"-fx-background-color: transparent; -fx-padding: 0;\"\>
>
> \<xAxis\>\<CategoryAxis style=\"-fx-tick-label-fill: #8B93A7;\"/\>\</xAxis\>
>
> \<yAxis\>\<NumberAxis label=\"kWh\" style=\"-fx-tick-label-fill: #8B93A7;\"/\>\</yAxis\>
>
> \</AreaChart\>
>
> \</VBox\>
>
> \<VBox GridPane.columnIndex=\"0\" GridPane.rowIndex=\"1\" spacing=\"8\"
>
> style=\"-fx-background-color: rgba(21,27,38,0.75); -fx-border-color: #2A3342;
>
> -fx-border-radius: 14; -fx-background-radius: 14; -fx-border-width: 1; -fx-padding: 14;\"\>
>
> \<Label text=\"Economies financieres\" style=\"-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #FFA53E;\"/\>
>
> \<BarChart fx:id=\"savingsChart\" VBox.vgrow=\"ALWAYS\" animated=\"false\" legendVisible=\"false\"
>
> style=\"-fx-background-color: transparent; -fx-padding: 0;\"\>
>
> \<xAxis\>\<CategoryAxis style=\"-fx-tick-label-fill: #8B93A7;\"/\>\</xAxis\>
>
> \<yAxis\>\<NumberAxis label=\"EUR\" style=\"-fx-tick-label-fill: #8B93A7;\"/\>\</yAxis\>
>
> \</BarChart\>
>
> \</VBox\>
>
> \<VBox GridPane.columnIndex=\"1\" GridPane.rowIndex=\"1\" spacing=\"8\"
>
> style=\"-fx-background-color: rgba(21,27,38,0.75); -fx-border-color: #2A3342;
>
> -fx-border-radius: 14; -fx-background-radius: 14; -fx-border-width: 1; -fx-padding: 14;\"\>
>
> \<Label text=\"CO2 evite\" style=\"-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #3B9DFF;\"/\>
>
> \<AreaChart fx:id=\"co2Chart\" VBox.vgrow=\"ALWAYS\" animated=\"false\" createSymbols=\"false\" legendVisible=\"false\"
>
> style=\"-fx-background-color: transparent; -fx-padding: 0;\"\>
>
> \<xAxis\>\<CategoryAxis style=\"-fx-tick-label-fill: #8B93A7;\"/\>\</xAxis\>
>
> \<yAxis\>\<NumberAxis label=\"kg CO2\" style=\"-fx-tick-label-fill: #8B93A7;\"/\>\</yAxis\>
>
> \</AreaChart\>
>
> \</VBox\>
>
> \</GridPane\>
>
> \</VBox\>
>
> \<VBox prefWidth=\"300\" minWidth=\"260\" maxWidth=\"340\"
>
> style=\"-fx-background-color: rgba(21,27,38,0.75); -fx-border-color: #2A3342;
>
> -fx-border-radius: 16; -fx-background-radius: 16; -fx-border-width: 1;\"\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"8\"
>
> style=\"-fx-padding: 14 16 12 16; -fx-border-color: transparent transparent #1E2A3A transparent; -fx-border-width: 1;\"\>
>
> \<Label fx:id=\"alertsCountLabel\" text=\"--- alertes\"
>
> style=\"-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #F5F7FA;\"/\>
>
> \</HBox\>
>
> \<ScrollPane fitToWidth=\"true\" VBox.vgrow=\"ALWAYS\"
>
> style=\"-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;\"\>
>
> \<VBox fx:id=\"alertsList\" spacing=\"0\"/\>
>
> \</ScrollPane\>
>
> \</VBox\>
>
> \</HBox\>

**src/main/resources/fr/renewguard/fxml/Main.fxml**

> \<?xml version=\"1.0\" encoding=\"UTF-8\"?\>
>
> \<?import javafx.scene.layout.\*?\>
>
> \<?import javafx.scene.control.\*?\>
>
> \<BorderPane xmlns:fx=\"http://javafx.com/fxml/1\"
>
> fx:id=\"rootPane\"
>
> fx:controller=\"fr.renewguard.controller.MainController\"
>
> style=\"-fx-background-color: #0A0E14;\"\>
>
> \<left\>
>
> \<VBox fx:id=\"sidebarContainer\"
>
> style=\"-fx-background-color: #070A0F; -fx-border-color: #1E2A3A; -fx-border-width: 0 1 0 0;\"/\>
>
> \</left\>
>
> \<center\>
>
> \<VBox\>
>
> \<VBox fx:id=\"emergencyBanner\" visible=\"false\" managed=\"false\"
>
> style=\"-fx-background-color: rgba(255,77,77,0.14); -fx-border-color: rgba(255,77,77,0.4); -fx-border-width: 0 0 1 0;\"\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"12\" style=\"-fx-padding: 8 24 8 24;\"\>
>
> \<Label text=\"MODE URGENCE ENERGETIQUE ACTIF\"
>
> style=\"-fx-text-fill: #FF4D4D; -fx-font-weight: 700; -fx-font-size: 12px;\"/\>
>
> \<Label text=\"- Delestage automatique Niveaux 2-3 active.\"
>
> style=\"-fx-text-fill: #8B93A7; -fx-font-size: 12px;\"/\>
>
> \<Region HBox.hgrow=\"ALWAYS\"/\>
>
> \<Button text=\"Desactiver (Ctrl+E)\" onAction=\"#onDisableEmergency\"
>
> style=\"-fx-background-color: rgba(255,77,77,0.2); -fx-border-color: rgba(255,77,77,0.4);
>
> -fx-border-radius: 6; -fx-background-radius: 6; -fx-border-width: 1;
>
> -fx-text-fill: #FF4D4D; -fx-font-size: 11px; -fx-font-weight: 600;
>
> -fx-cursor: hand; -fx-padding: 4 12 4 12;\"/\>
>
> \</HBox\>
>
> \</VBox\>
>
> \<HBox fx:id=\"topbarContainer\" prefHeight=\"56\" minHeight=\"56\" maxHeight=\"56\"
>
> style=\"-fx-background-color: rgba(7,10,15,0.95); -fx-border-color: #1E2A3A; -fx-border-width: 0 0 1 0;\"/\>
>
> \<StackPane fx:id=\"contentArea\" VBox.vgrow=\"ALWAYS\" style=\"-fx-background-color: #0A0E14;\"/\>
>
> \</VBox\>
>
> \</center\>
>
> \</BorderPane\>

**src/main/resources/fr/renewguard/fxml/Priorities.fxml**

> \<?xml version=\"1.0\" encoding=\"UTF-8\"?\>
>
> \<?import javafx.scene.layout.VBox?\>
>
> \<?import javafx.scene.layout.HBox?\>
>
> \<?import javafx.scene.layout.Region?\>
>
> \<?import javafx.scene.control.Button?\>
>
> \<?import javafx.scene.control.Label?\>
>
> \<?import javafx.scene.control.Separator?\>
>
> \<?import javafx.scene.control.Tooltip?\>
>
> \<VBox xmlns:fx=\"http://javafx.com/fxml/1\"
>
> fx:controller=\"fr.renewguard.controller.PrioritiesController\"
>
> spacing=\"14\"
>
> style=\"-fx-padding: 24; -fx-background-color: transparent;\"\>
>
> \<HBox fx:id=\"modifiedBanner\" alignment=\"CENTER_LEFT\" spacing=\"10\" visible=\"false\" managed=\"false\"
>
> style=\"-fx-background-color: rgba(255,165,62,0.11); -fx-border-color: rgba(255,165,62,0.3);
>
> -fx-border-radius: 10; -fx-background-radius: 10; -fx-border-width: 1; -fx-padding: 10 16 10 16;\"\>
>
> \<Label text=\"!\" style=\"-fx-font-size: 14px; -fx-text-fill: #FFA53E; -fx-font-weight: 700;\"/\>
>
> \<Label text=\"Modifications non enregistrees\" HBox.hgrow=\"ALWAYS\"
>
> style=\"-fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #FFA53E; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \<Button text=\"Enregistrer (Ctrl+S)\" onAction=\"#onSave\"
>
> style=\"-fx-background-color: #FFA53E; -fx-border-color: transparent;
>
> -fx-border-radius: 7; -fx-background-radius: 7;
>
> -fx-text-fill: #0A0E14; -fx-font-size: 12px; -fx-font-weight: 700;
>
> -fx-cursor: hand; -fx-padding: 5 12 5 12;\"/\>
>
> \</HBox\>
>
> \<HBox fx:id=\"savedBanner\" alignment=\"CENTER_LEFT\" spacing=\"10\" visible=\"false\" managed=\"false\"
>
> style=\"-fx-background-color: rgba(34,211,165,0.09); -fx-border-color: rgba(34,211,165,0.27);
>
> -fx-border-radius: 10; -fx-background-radius: 10; -fx-border-width: 1; -fx-padding: 10 16 10 16;\"\>
>
> \<Label text=\"OK\" style=\"-fx-font-size: 12px; -fx-text-fill: #22D3A5; -fx-font-weight: 700;\"/\>
>
> \<Label text=\"Priorites enregistrees - decisions IA mises a jour.\"
>
> style=\"-fx-font-size: 12px; -fx-font-weight: 500; -fx-text-fill: #22D3A5; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \</HBox\>
>
> \<HBox spacing=\"14\" VBox.vgrow=\"ALWAYS\"\>
>
> \<VBox fx:id=\"dropZone1\" spacing=\"10\" HBox.hgrow=\"ALWAYS\"
>
> style=\"-fx-background-color: rgba(255,77,77,0.06); -fx-border-color: rgba(255,77,77,0.22);
>
> -fx-border-radius: 16; -fx-background-radius: 16; -fx-border-width: 1; -fx-padding: 16;\"\>
>
> \<VBox spacing=\"3\"\>
>
> \<Label text=\"Niveau 1 - Critique\" style=\"-fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: #FF4D4D;\"/\>
>
> \<Label text=\"Toujours alimente\" style=\"-fx-font-size: 11px; -fx-text-fill: #8B93A7;\"/\>
>
> \<Label fx:id=\"col1Stats\" text=\"--- W - 0 equipements\"
>
> style=\"-fx-font-size: 11px; -fx-font-weight: 500; -fx-text-fill: #FF4D4D; -fx-font-family: \'JetBrains Mono\', monospace;\"/\>
>
> \</VBox\>
>
> \<Separator style=\"-fx-border-color: rgba(255,77,77,0.3);\"/\>
>
> \<VBox fx:id=\"col1Cards\" spacing=\"8\" VBox.vgrow=\"ALWAYS\"/\>
>
> \</VBox\>
>
> \<VBox fx:id=\"dropZone2\" spacing=\"10\" HBox.hgrow=\"ALWAYS\"
>
> style=\"-fx-background-color: rgba(255,165,62,0.06); -fx-border-color: rgba(255,165,62,0.22);
>
> -fx-border-radius: 16; -fx-background-radius: 16; -fx-border-width: 1; -fx-padding: 16;\"\>
>
> \<VBox spacing=\"3\"\>
>
> \<Label text=\"Niveau 2 - Important\" style=\"-fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: #FFA53E;\"/\>
>
> \<Label text=\"Selon energie disponible\" style=\"-fx-font-size: 11px; -fx-text-fill: #8B93A7;\"/\>
>
> \<Label fx:id=\"col2Stats\" text=\"--- W - 0 equipements\"
>
> style=\"-fx-font-size: 11px; -fx-font-weight: 500; -fx-text-fill: #FFA53E; -fx-font-family: \'JetBrains Mono\', monospace;\"/\>
>
> \</VBox\>
>
> \<Separator style=\"-fx-border-color: rgba(255,165,62,0.3);\"/\>
>
> \<VBox fx:id=\"col2Cards\" spacing=\"8\" VBox.vgrow=\"ALWAYS\"/\>
>
> \</VBox\>
>
> \<VBox fx:id=\"dropZone3\" spacing=\"10\" HBox.hgrow=\"ALWAYS\"
>
> style=\"-fx-background-color: rgba(139,147,167,0.06); -fx-border-color: rgba(139,147,167,0.18);
>
> -fx-border-radius: 16; -fx-background-radius: 16; -fx-border-width: 1; -fx-padding: 16;\"\>
>
> \<VBox spacing=\"3\"\>
>
> \<Label text=\"Niveau 3 - Non prioritaire\" style=\"-fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: #8B93A7;\"/\>
>
> \<Label text=\"Coupe en cas de penurie\" style=\"-fx-font-size: 11px; -fx-text-fill: #8B93A7;\"/\>
>
> \<Label fx:id=\"col3Stats\" text=\"--- W - 0 equipements\"
>
> style=\"-fx-font-size: 11px; -fx-font-weight: 500; -fx-text-fill: #8B93A7; -fx-font-family: \'JetBrains Mono\', monospace;\"/\>
>
> \</VBox\>
>
> \<Separator style=\"-fx-border-color: rgba(139,147,167,0.2);\"/\>
>
> \<VBox fx:id=\"col3Cards\" spacing=\"8\" VBox.vgrow=\"ALWAYS\"/\>
>
> \</VBox\>
>
> \<VBox fx:id=\"rulesContainer\" prefWidth=\"310\" minWidth=\"270\" spacing=\"12\"
>
> style=\"-fx-background-color: rgba(21,27,38,0.75); -fx-border-color: #2A3342;
>
> -fx-border-radius: 16; -fx-background-radius: 16; -fx-border-width: 1; -fx-padding: 18;\"\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"8\"\>
>
> \<Label text=\"Regles automatiques IA\" HBox.hgrow=\"ALWAYS\"
>
> style=\"-fx-font-size: 13px; -fx-font-weight: 600; -fx-text-fill: #F5F7FA; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \<Button text=\"+ Regle\" onAction=\"#onAddRule\"
>
> style=\"-fx-background-color: rgba(34,211,165,0.10); -fx-border-color: rgba(34,211,165,0.3);
>
> -fx-border-radius: 8; -fx-background-radius: 8; -fx-border-width: 1; -fx-text-fill: #22D3A5;
>
> -fx-font-size: 11px; -fx-font-weight: 600; -fx-cursor: hand; -fx-padding: 4 10 4 10;\"/\>
>
> \</HBox\>
>
> \<VBox fx:id=\"rulesList\" spacing=\"8\"/\>
>
> \</VBox\>
>
> \</HBox\>
>
> \<HBox alignment=\"CENTER_RIGHT\" spacing=\"10\"\>
>
> \<Button text=\"Regles\" onAction=\"#onToggleRulesPanel\"
>
> style=\"-fx-background-color: rgba(59,157,255,0.10); -fx-border-color: rgba(59,157,255,0.3);
>
> -fx-border-radius: 8; -fx-background-radius: 8; -fx-border-width: 1; -fx-text-fill: #3B9DFF;
>
> -fx-font-size: 12px; -fx-cursor: hand; -fx-padding: 7 14 7 14;\"/\>
>
> \<Region HBox.hgrow=\"ALWAYS\"/\>
>
> \<Label text=\"Glisser-deposer pour reorganiser\" style=\"-fx-font-size: 11px; -fx-text-fill: #8B93A7;\"/\>
>
> \<Button text=\"Enregistrer\" onAction=\"#onSave\"
>
> style=\"-fx-background-color: linear-gradient(to bottom right, #22D3A5, #1BB88E);
>
> -fx-background-radius: 8; -fx-border-color: transparent;
>
> -fx-text-fill: #0A0E14; -fx-font-size: 13px; -fx-font-weight: 700;
>
> -fx-cursor: hand; -fx-padding: 8 18 8 18;\"\>
>
> \<tooltip\>\<Tooltip text=\"Ctrl+S\"/\>\</tooltip\>
>
> \</Button\>
>
> \</HBox\>
>
> \</VBox\>

**src/main/resources/fr/renewguard/fxml/Sidebar.fxml**

> \<?xml version=\"1.0\" encoding=\"UTF-8\"?\>
>
> \<?import javafx.scene.layout.VBox?\>
>
> \<?import javafx.scene.layout.HBox?\>
>
> \<?import javafx.scene.layout.Region?\>
>
> \<?import javafx.scene.control.Button?\>
>
> \<?import javafx.scene.control.Label?\>
>
> \<?import javafx.scene.shape.Circle?\>
>
> \<VBox xmlns:fx=\"http://javafx.com/fxml/1\"
>
> fx:id=\"sidebarRoot\"
>
> fx:controller=\"fr.renewguard.controller.SidebarController\"
>
> prefWidth=\"240\" minWidth=\"240\" maxWidth=\"240\"
>
> style=\"-fx-background-color: #070A0F;
>
> -fx-border-color: transparent #1E2A3A transparent transparent;
>
> -fx-border-width: 1;\"\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"10\" style=\"-fx-padding: 20 16 16 16;\"\>
>
> \<HBox alignment=\"CENTER\"
>
> style=\"-fx-background-color: linear-gradient(to bottom right, #22D3A5, #3B9DFF);
>
> -fx-background-radius: 10;
>
> -fx-pref-width: 34; -fx-pref-height: 34; -fx-min-width: 34; -fx-min-height: 34;
>
> -fx-effect: dropshadow(gaussian, rgba(34,211,165,0.35), 10, 0, 0, 0);\"\>
>
> \<Label text=\"RG\" style=\"-fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: #0A0E14;\"/\>
>
> \</HBox\>
>
> \<VBox spacing=\"1\"\>
>
> \<Label text=\"RenewGuard\"
>
> style=\"-fx-font-size: 13px; -fx-font-weight: 700; -fx-text-fill: #F5F7FA; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \<Label text=\"AI Platform\"
>
> style=\"-fx-font-size: 10px; -fx-text-fill: #22D3A5; -fx-font-family: \'JetBrains Mono\', monospace;\"/\>
>
> \</VBox\>
>
> \</HBox\>
>
> \<Region prefHeight=\"1\" maxHeight=\"1\" style=\"-fx-background-color: #1E2A3A;\" VBox.vgrow=\"NEVER\"/\>
>
> \<!\-- Navigation : les boutons sont cables en Java (SidebarController.initialize) via setOnAction,
>
> PAS via onAction FXML - ne pas ajouter onAction=\"#onNavXxx\" ici, ces methodes n\'existent pas. \--\>
>
> \<VBox spacing=\"2\" style=\"-fx-padding: 12 10 12 10;\" VBox.vgrow=\"ALWAYS\"\>
>
> \<Button fx:id=\"navDashboard\" text=\" Tableau de bord\" styleClass=\"sidebar-item-active\" maxWidth=\"Infinity\"/\>
>
> \<Button fx:id=\"navEquipment\" text=\" Equipements\" styleClass=\"sidebar-item\" maxWidth=\"Infinity\"/\>
>
> \<Button fx:id=\"navPriorities\" text=\" Priorites\" styleClass=\"sidebar-item\" maxWidth=\"Infinity\"/\>
>
> \<Button fx:id=\"navAi\" text=\" Intelligence IA\" styleClass=\"sidebar-item\" maxWidth=\"Infinity\"/\>
>
> \<Button fx:id=\"navHistory\" text=\" Historique\" styleClass=\"sidebar-item\" maxWidth=\"Infinity\"/\>
>
> \</VBox\>
>
> \<Region VBox.vgrow=\"ALWAYS\"/\>
>
> \<Region prefHeight=\"1\" maxHeight=\"1\" style=\"-fx-background-color: #1E2A3A;\"/\>
>
> \<VBox spacing=\"6\" style=\"-fx-padding: 14 16 18 16;\"\>
>
> \<HBox alignment=\"CENTER_LEFT\" spacing=\"8\"\>
>
> \<Circle fx:id=\"statusDot\" radius=\"4\"
>
> style=\"-fx-fill: #22D3A5; -fx-effect: dropshadow(gaussian, #22D3A5, 4, 0, 0, 0);\"/\>
>
> \<Label text=\"Systeme connecte\"
>
> style=\"-fx-font-size: 11px; -fx-font-weight: 600; -fx-text-fill: #22D3A5; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \</HBox\>
>
> \<Label fx:id=\"siteNameLabel\" text=\"Site principal\"
>
> style=\"-fx-font-size: 11px; -fx-text-fill: #8B93A7; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \</VBox\>
>
> \</VBox\>

**src/main/resources/fr/renewguard/fxml/Topbar.fxml**

> \<?xml version=\"1.0\" encoding=\"UTF-8\"?\>
>
> \<?import javafx.scene.layout.HBox?\>
>
> \<?import javafx.scene.layout.StackPane?\>
>
> \<?import javafx.scene.layout.VBox?\>
>
> \<?import javafx.scene.layout.Region?\>
>
> \<?import javafx.scene.control.Button?\>
>
> \<?import javafx.scene.control.Label?\>
>
> \<?import javafx.scene.control.TextField?\>
>
> \<?import javafx.scene.control.Tooltip?\>
>
> \<?import javafx.scene.shape.Circle?\>
>
> \<?import javafx.geometry.Insets?\>
>
> \<HBox xmlns:fx=\"http://javafx.com/fxml/1\"
>
> fx:controller=\"fr.renewguard.controller.TopbarController\"
>
> alignment=\"CENTER_LEFT\"
>
> prefHeight=\"56\" minHeight=\"56\" maxHeight=\"56\"
>
> spacing=\"12\"
>
> style=\"-fx-background-color: rgba(7, 10, 15, 0.92);
>
> -fx-border-color: transparent transparent #1E2A3A transparent;
>
> -fx-border-width: 1;
>
> -fx-padding: 0 20 0 20;\"\>
>
> \<Label fx:id=\"screenTitleLabel\" text=\"Tableau de bord\"
>
> style=\"-fx-font-size: 15px; -fx-font-weight: 700; -fx-text-fill: #F5F7FA; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \<Region HBox.hgrow=\"ALWAYS\"/\>
>
> \<StackPane maxWidth=\"280\" prefWidth=\"240\"\>
>
> \<TextField fx:id=\"searchField\" promptText=\"Rechercher\... Ctrl+K\"
>
> style=\"-fx-background-color: rgba(255,255,255,0.04);
>
> -fx-border-color: #2A3342; -fx-border-radius: 8; -fx-background-radius: 8;
>
> -fx-border-width: 1; -fx-text-fill: #F5F7FA; -fx-prompt-text-fill: #8B93A7;
>
> -fx-font-size: 12px; -fx-font-family: \'Inter\', sans-serif;
>
> -fx-padding: 7 36 7 12;\"/\>
>
> \</StackPane\>
>
> \<Button fx:id=\"emergencyBtn\" text=\"Urgence\" styleClass=\"btn-emergency\" onAction=\"#onToggleEmergency\"\>
>
> \<tooltip\>\<Tooltip text=\"Activer / desactiver le mode urgence (Ctrl+E)\"/\>\</tooltip\>
>
> \</Button\>
>
> \<Label fx:id=\"clockLabel\" text=\"00:00\"
>
> style=\"-fx-font-family: \'JetBrains Mono\', monospace; -fx-font-size: 12px; -fx-text-fill: #8B93A7;\"/\>
>
> \<StackPane\>
>
> \<Button fx:id=\"notifBtn\" text=\"Alertes\" styleClass=\"icon-btn\" onAction=\"#onToggleNotifications\"/\>
>
> \<Circle fx:id=\"notifDot\" radius=\"4\" visible=\"false\" style=\"-fx-fill: #FF4D4D;\" StackPane.alignment=\"TOP_RIGHT\"\>
>
> \<StackPane.margin\>\<Insets top=\"2\" right=\"2\"/\>\</StackPane.margin\>
>
> \</Circle\>
>
> \</StackPane\>
>
> \<StackPane onMouseClicked=\"#onUserMenu\" style=\"-fx-cursor: hand;\"\>
>
> \<Circle radius=\"18\"
>
> style=\"-fx-fill: linear-gradient(to bottom right, #22D3A5, #3B9DFF);
>
> -fx-effect: dropshadow(gaussian, rgba(34,211,165,0.28), 8, 0, 0, 0);\"/\>
>
> \<Label fx:id=\"userInitials\" text=\"---\"
>
> style=\"-fx-font-size: 11px; -fx-font-weight: 700; -fx-text-fill: #0A0E14; -fx-font-family: \'Inter\', sans-serif;\"/\>
>
> \</StackPane\>
>
> \</HBox\>

**14. Styles CSS**

**src/main/resources/fr/renewguard/css/base.css**

> .root {
>
> -fx-background-color: #0A0E14;
>
> -fx-font-family: \"Inter\";
>
> -fx-font-size: 13px;
>
> -fx-text-fill: #F5F7FA;
>
> -fx-focus-color: #3B9DFF;
>
> -fx-faint-focus-color: transparent;
>
> }
>
> /\* ── Button reset ──────────────────────────────────────────────────── \*/
>
> .button {
>
> -fx-background-color: transparent;
>
> -fx-background-radius: 0;
>
> -fx-border-color: transparent;
>
> -fx-border-width: 0;
>
> -fx-padding: 0;
>
> -fx-focus-traversable: true;
>
> }
>
> .button:focused {
>
> -fx-effect: dropshadow(gaussian, rgba(59, 157, 255, 0.55), 6, 0, 0, 0);
>
> }
>
> /\* ── TextField / PasswordField reset ───────────────────────────────── \*/
>
> .text-input {
>
> -fx-background-insets: 0;
>
> -fx-padding: 0;
>
> }
>
> .text-input:focused {
>
> -fx-background-insets: 0;
>
> }
>
> /\* ── TableView reset ────────────────────────────────────────────────── \*/
>
> .table-view {
>
> -fx-background-color: transparent;
>
> -fx-table-cell-border-color: #1E2A3A;
>
> -fx-border-color: transparent;
>
> -fx-border-width: 0;
>
> -fx-background-insets: 0;
>
> -fx-padding: 0;
>
> }
>
> .table-view .corner {
>
> -fx-background-color: transparent;
>
> }
>
> .table-view .column-header-background {
>
> -fx-background-color: rgba(7, 10, 15, 0.62);
>
> -fx-background-insets: 0;
>
> -fx-padding: 0;
>
> }
>
> .table-view .column-header {
>
> -fx-background-color: transparent;
>
> -fx-border-color: transparent transparent #1E2A3A transparent;
>
> -fx-border-width: 1;
>
> -fx-padding: 0;
>
> }
>
> .table-view .column-header .label {
>
> -fx-text-fill: #8B93A7;
>
> -fx-font-size: 10px;
>
> -fx-font-weight: 700;
>
> -fx-padding: 12 16 12 16;
>
> -fx-alignment: CENTER_LEFT;
>
> }
>
> .table-view .filler {
>
> -fx-background-color: rgba(7, 10, 15, 0.62);
>
> -fx-border-color: transparent transparent #1E2A3A transparent;
>
> -fx-border-width: 1;
>
> }
>
> .table-row-cell {
>
> -fx-background-color: transparent;
>
> -fx-border-color: transparent transparent #1E2A3A transparent;
>
> -fx-border-width: 1;
>
> -fx-cell-size: 50px;
>
> -fx-text-fill: #F5F7FA;
>
> -fx-padding: 0;
>
> }
>
> .table-row-cell:hover {
>
> -fx-background-color: rgba(255, 255, 255, 0.025);
>
> }
>
> .table-row-cell:selected {
>
> -fx-background-color: rgba(34, 211, 165, 0.07);
>
> }
>
> .table-row-cell:selected .label {
>
> -fx-text-fill: #F5F7FA;
>
> }
>
> .table-row-cell:empty {
>
> -fx-background-color: transparent;
>
> -fx-border-color: transparent;
>
> }
>
> .table-cell {
>
> -fx-text-fill: #F5F7FA;
>
> -fx-font-size: 13px;
>
> -fx-padding: 0 16 0 16;
>
> -fx-alignment: CENTER_LEFT;
>
> -fx-border-color: transparent;
>
> -fx-border-width: 0;
>
> }
>
> /\* ── ScrollPane reset ───────────────────────────────────────────────── \*/
>
> .scroll-pane {
>
> -fx-background-color: transparent;
>
> -fx-background: transparent;
>
> -fx-border-color: transparent;
>
> -fx-border-width: 0;
>
> -fx-padding: 0;
>
> }
>
> .scroll-pane .viewport {
>
> -fx-background-color: transparent;
>
> }
>
> .scroll-pane .content {
>
> -fx-background-color: transparent;
>
> }
>
> /\* ── Scrollbar ──────────────────────────────────────────────────────── \*/
>
> .scroll-bar:vertical {
>
> -fx-pref-width: 4;
>
> -fx-background-color: transparent;
>
> -fx-background-radius: 2;
>
> -fx-padding: 0;
>
> }
>
> .scroll-bar:horizontal {
>
> -fx-pref-height: 4;
>
> -fx-background-color: transparent;
>
> -fx-background-radius: 2;
>
> -fx-padding: 0;
>
> }
>
> .scroll-bar .track {
>
> -fx-background-color: transparent;
>
> -fx-border-color: transparent;
>
> }
>
> .scroll-bar .track-background {
>
> -fx-background-color: transparent;
>
> }
>
> .scroll-bar .thumb {
>
> -fx-background-color: #2A3342;
>
> -fx-background-radius: 2;
>
> -fx-border-color: transparent;
>
> }
>
> .scroll-bar .thumb:hover {
>
> -fx-background-color: #3B4A5E;
>
> }
>
> .scroll-bar .thumb:pressed {
>
> -fx-background-color: #22D3A5;
>
> }
>
> .scroll-bar .increment-button,
>
> .scroll-bar .decrement-button {
>
> -fx-background-color: transparent;
>
> -fx-border-color: transparent;
>
> -fx-pref-width: 0;
>
> -fx-pref-height: 0;
>
> -fx-padding: 0;
>
> }
>
> .scroll-bar .increment-arrow,
>
> .scroll-bar .decrement-arrow {
>
> -fx-shape: \"\";
>
> -fx-pref-width: 0;
>
> -fx-pref-height: 0;
>
> }
>
> /\* ── ListView reset ─────────────────────────────────────────────────── \*/
>
> .list-view {
>
> -fx-background-color: transparent;
>
> -fx-border-color: transparent;
>
> -fx-border-width: 0;
>
> -fx-padding: 0;
>
> }
>
> .list-cell {
>
> -fx-background-color: transparent;
>
> -fx-border-color: transparent transparent #1E2A3A transparent;
>
> -fx-border-width: 1;
>
> -fx-padding: 0;
>
> -fx-text-fill: #F5F7FA;
>
> }
>
> .list-cell:hover {
>
> -fx-background-color: rgba(255, 255, 255, 0.025);
>
> }
>
> .list-cell:selected {
>
> -fx-background-color: rgba(34, 211, 165, 0.07);
>
> -fx-text-fill: #F5F7FA;
>
> }
>
> .list-cell:empty {
>
> -fx-background-color: transparent;
>
> -fx-border-color: transparent;
>
> }
>
> /\* ── Separator ──────────────────────────────────────────────────────── \*/
>
> .separator .line {
>
> -fx-border-color: #1E2A3A;
>
> -fx-border-width: 1 0 0 0;
>
> }
>
> /\* ── SplitPane ──────────────────────────────────────────────────────── \*/
>
> .split-pane {
>
> -fx-background-color: transparent;
>
> -fx-padding: 0;
>
> }
>
> .split-pane-divider {
>
> -fx-background-color: #1E2A3A;
>
> -fx-pref-width: 1;
>
> -fx-pref-height: 1;
>
> -fx-padding: 0;
>
> }
>
> .split-pane:horizontal \> .split-pane-divider {
>
> -fx-pref-width: 1;
>
> }
>
> .split-pane:vertical \> .split-pane-divider {
>
> -fx-pref-height: 1;
>
> }
>
> /\* ── Tooltip ────────────────────────────────────────────────────────── \*/
>
> .tooltip {
>
> -fx-background-color: #0F1420;
>
> -fx-background-radius: 8;
>
> -fx-border-color: #2A3342;
>
> -fx-border-radius: 8;
>
> -fx-border-width: 1;
>
> -fx-text-fill: #F5F7FA;
>
> -fx-font-size: 11px;
>
> -fx-font-family: \"Inter\";
>
> -fx-padding: 5 10 5 10;
>
> -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.52), 10, 0, 0, 3);
>
> }
>
> /\* ── ContextMenu ────────────────────────────────────────────────────── \*/
>
> .context-menu {
>
> -fx-background-color: #0F1420;
>
> -fx-background-radius: 10;
>
> -fx-border-color: #2A3342;
>
> -fx-border-radius: 10;
>
> -fx-border-width: 1;
>
> -fx-padding: 4;
>
> -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.62), 20, 0, 0, 6);
>
> }
>
> .context-menu .menu-item {
>
> -fx-background-color: transparent;
>
> -fx-background-radius: 7;
>
> -fx-padding: 7 14 7 14;
>
> }
>
> .context-menu .menu-item .label {
>
> -fx-text-fill: #F5F7FA;
>
> -fx-font-size: 12px;
>
> -fx-font-family: \"Inter\";
>
> }
>
> .context-menu .menu-item:hover {
>
> -fx-background-color: rgba(255, 255, 255, 0.05);
>
> }
>
> .context-menu .menu-item:focused {
>
> -fx-background-color: rgba(255, 255, 255, 0.05);
>
> }
>
> .context-menu .separator .line {
>
> -fx-border-color: #1E2A3A;
>
> -fx-border-width: 1 0 0 0;
>
> }
>
> /\* ── ProgressBar ────────────────────────────────────────────────────── \*/
>
> .progress-bar .track {
>
> -fx-background-color: #1E2A3A;
>
> -fx-background-radius: 3;
>
> -fx-border-color: transparent;
>
> }
>
> .progress-bar .bar {
>
> -fx-background-color: #22D3A5;
>
> -fx-background-radius: 3;
>
> -fx-border-color: transparent;
>
> -fx-background-insets: 0;
>
> }
>
> /\* ── MenuButton / ComboBox reset ────────────────────────────────────── \*/
>
> .combo-box {
>
> -fx-background-color: rgba(255, 255, 255, 0.04);
>
> -fx-background-radius: 8;
>
> -fx-border-color: #2A3342;
>
> -fx-border-radius: 8;
>
> -fx-border-width: 1;
>
> -fx-text-fill: #F5F7FA;
>
> -fx-font-size: 13px;
>
> -fx-padding: 0;
>
> }
>
> .combo-box .list-cell {
>
> -fx-text-fill: #F5F7FA;
>
> -fx-background-color: transparent;
>
> -fx-border-color: transparent;
>
> -fx-padding: 7 10 7 10;
>
> }
>
> .combo-box-popup .list-view {
>
> -fx-background-color: #0F1420;
>
> -fx-border-color: #2A3342;
>
> -fx-border-width: 1;
>
> -fx-background-radius: 8;
>
> -fx-border-radius: 8;
>
> -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.55), 16, 0, 0, 4);
>
> }
>
> .combo-box-popup .list-cell:hover {
>
> -fx-background-color: rgba(255, 255, 255, 0.05);
>
> }
>
> .combo-box-popup .list-cell:selected {
>
> -fx-background-color: rgba(34, 211, 165, 0.10);
>
> -fx-text-fill: #22D3A5;
>
> }
>
> /\* ── Chart base ─────────────────────────────────────────────────────── \*/
>
> .chart {
>
> -fx-background-color: transparent;
>
> -fx-padding: 0;
>
> }
>
> .chart-plot-background {
>
> -fx-background-color: transparent;
>
> }
>
> .chart-horizontal-grid-lines {
>
> -fx-stroke: rgba(42, 51, 66, 0.5);
>
> -fx-stroke-dash-array: 4 4;
>
> }
>
> .chart-vertical-grid-lines {
>
> -fx-stroke: transparent;
>
> }
>
> .chart-alternative-row-fill {
>
> -fx-fill: transparent;
>
> }
>
> .chart-alternative-column-fill {
>
> -fx-fill: transparent;
>
> }
>
> .axis {
>
> -fx-tick-label-fill: #8B93A7;
>
> -fx-tick-mark-visible: false;
>
> -fx-minor-tick-visible: false;
>
> }
>
> .axis-label {
>
> -fx-text-fill: #8B93A7;
>
> -fx-font-size: 10px;
>
> }
>
> .chart-legend {
>
> -fx-background-color: transparent;
>
> -fx-padding: 4;
>
> }
>
> .chart-legend-item {
>
> -fx-text-fill: #8B93A7;
>
> -fx-font-size: 11px;
>
> }

**src/main/resources/fr/renewguard/css/components.css**

> /\* ── KPI card hover ─────────────────────────────────────────────────── \*/
>
> .glass-hover {
>
> -fx-cursor: hand;
>
> -fx-transition: -fx-background-color 150ms;
>
> }
>
> .glass-hover:hover {
>
> -fx-background-color: rgba(255,255,255,0.03);
>
> -fx-border-color: #3B4A5E;
>
> }
>
> /\* ── Legend label ───────────────────────────────────────────────────── \*/
>
> .legend-label {
>
> -fx-font-size: 12px;
>
> -fx-text-fill: #8B93A7;
>
> }
>
> /\* ── AI decision badges ─────────────────────────────────────────────── \*/
>
> .badge-done {
>
> -fx-background-color: rgba(34,211,165,0.12);
>
> -fx-border-color: rgba(34,211,165,0.3);
>
> -fx-border-radius: 20;
>
> -fx-background-radius: 20;
>
> -fx-border-width: 1;
>
> -fx-text-fill: #22D3A5;
>
> -fx-font-size: 10px;
>
> -fx-font-weight: 700;
>
> -fx-padding: 2 8 2 8;
>
> -fx-letter-spacing: 0.04em;
>
> }
>
> .badge-pending {
>
> -fx-background-color: rgba(255,165,62,0.12);
>
> -fx-border-color: rgba(255,165,62,0.3);
>
> -fx-border-radius: 20;
>
> -fx-background-radius: 20;
>
> -fx-border-width: 1;
>
> -fx-text-fill: #FFA53E;
>
> -fx-font-size: 10px;
>
> -fx-font-weight: 700;
>
> -fx-padding: 2 8 2 8;
>
> -fx-letter-spacing: 0.04em;
>
> }
>
> /\* ── Confidence bar ─────────────────────────────────────────────────── \*/
>
> .confidence-bar .bar {
>
> -fx-background-color: #22D3A5;
>
> -fx-background-radius: 2;
>
> -fx-padding: 0;
>
> }
>
> .confidence-bar .track {
>
> -fx-background-color: #1E2A3A;
>
> -fx-background-radius: 2;
>
> }
>
> .confidence-bar {
>
> -fx-pref-height: 5;
>
> -fx-max-height: 5;
>
> -fx-background-insets: 0;
>
> -fx-padding: 0;
>
> }
>
> /\* ── Area chart fills ───────────────────────────────────────────────── \*/
>
> .chart-dark .default-color0.chart-series-area-fill {
>
> -fx-fill: linear-gradient(to bottom,
>
> rgba(34,211,165,0.28) 0%, rgba(34,211,165,0.0) 100%);
>
> }
>
> .chart-dark .default-color1.chart-series-area-fill {
>
> -fx-fill: linear-gradient(to bottom,
>
> rgba(59,157,255,0.22) 0%, rgba(59,157,255,0.0) 100%);
>
> }
>
> .chart-dark .default-color0.chart-series-area-line {
>
> -fx-stroke: #22D3A5;
>
> -fx-stroke-width: 2;
>
> }
>
> .chart-dark .default-color1.chart-series-area-line {
>
> -fx-stroke: #3B9DFF;
>
> -fx-stroke-width: 2;
>
> }
>
> .chart-dark .chart-plot-background {
>
> -fx-background-color: transparent;
>
> }
>
> .chart-dark .chart-vertical-grid-lines {
>
> -fx-stroke: transparent;
>
> }
>
> .chart-dark .chart-horizontal-grid-lines {
>
> -fx-stroke: rgba(42,51,66,0.45);
>
> -fx-stroke-dash-array: 4 4;
>
> }
>
> .chart-dark .axis {
>
> -fx-tick-label-fill: #8B93A7;
>
> -fx-minor-tick-visible: false;
>
> }
>
> .chart-dark .axis-label {
>
> -fx-text-fill: #8B93A7;
>
> }
>
> .chart-dark .chart-legend {
>
> -fx-background-color: transparent;
>
> }
>
> .chart-dark .chart-legend-item {
>
> -fx-text-fill: #8B93A7;
>
> -fx-font-size: 11px;
>
> }
>
> /\* ── Pie/Donut chart ────────────────────────────────────────────────── \*/
>
> .pie-chart .chart-plot-background {
>
> -fx-background-color: transparent;
>
> }
>
> .pie-chart .chart-content {
>
> -fx-background-color: transparent;
>
> }

**src/main/resources/fr/renewguard/css/theme-dark.css**

> .screen-root {
>
> -fx-background-color: transparent;
>
> -fx-padding: 0;
>
> }
>
> .glass-card {
>
> -fx-background-color: rgba(21, 27, 38, 0.75);
>
> -fx-background-radius: 16;
>
> -fx-border-color: #2A3342;
>
> -fx-border-radius: 16;
>
> -fx-border-width: 1;
>
> -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.45), 18, 0, 0, 4);
>
> }
>
> .glass-card:hover {
>
> -fx-background-color: rgba(21, 27, 38, 0.88);
>
> -fx-border-color: #3B4A5E;
>
> }
>
> .card-glow-green {
>
> -fx-background-color: rgba(21, 27, 38, 0.75);
>
> -fx-background-radius: 16;
>
> -fx-border-color: rgba(34, 211, 165, 0.32);
>
> -fx-border-radius: 16;
>
> -fx-border-width: 1;
>
> -fx-effect: dropshadow(gaussian, rgba(34, 211, 165, 0.14), 22, 0, 0, 0);
>
> }
>
> .card-glow-blue {
>
> -fx-background-color: rgba(21, 27, 38, 0.75);
>
> -fx-background-radius: 16;
>
> -fx-border-color: rgba(59, 157, 255, 0.32);
>
> -fx-border-radius: 16;
>
> -fx-border-width: 1;
>
> -fx-effect: dropshadow(gaussian, rgba(59, 157, 255, 0.14), 22, 0, 0, 0);
>
> }
>
> .card-glow-amber {
>
> -fx-background-color: rgba(21, 27, 38, 0.75);
>
> -fx-background-radius: 16;
>
> -fx-border-color: rgba(255, 165, 62, 0.32);
>
> -fx-border-radius: 16;
>
> -fx-border-width: 1;
>
> -fx-effect: dropshadow(gaussian, rgba(255, 165, 62, 0.14), 22, 0, 0, 0);
>
> }
>
> .card-glow-red {
>
> -fx-background-color: rgba(21, 27, 38, 0.75);
>
> -fx-background-radius: 16;
>
> -fx-border-color: rgba(255, 77, 77, 0.32);
>
> -fx-border-radius: 16;
>
> -fx-border-width: 1;
>
> -fx-effect: dropshadow(gaussian, rgba(255, 77, 77, 0.14), 22, 0, 0, 0);
>
> }
>
> .kpi-label {
>
> -fx-font-size: 11px;
>
> -fx-font-weight: 600;
>
> -fx-text-fill: #8B93A7;
>
> }
>
> .kpi-value {
>
> -fx-font-family: \"JetBrains Mono\";
>
> -fx-font-size: 24px;
>
> -fx-font-weight: 700;
>
> -fx-text-fill: #F5F7FA;
>
> }
>
> .kpi-unit {
>
> -fx-font-family: \"JetBrains Mono\";
>
> -fx-font-size: 13px;
>
> -fx-font-weight: 400;
>
> -fx-text-fill: #8B93A7;
>
> }
>
> .kpi-sub {
>
> -fx-font-size: 11px;
>
> -fx-text-fill: #8B93A7;
>
> }
>
> .kpi-trend-up {
>
> -fx-font-size: 11px;
>
> -fx-font-weight: 600;
>
> -fx-text-fill: #22D3A5;
>
> }
>
> .kpi-trend-down {
>
> -fx-font-size: 11px;
>
> -fx-font-weight: 600;
>
> -fx-text-fill: #FF4D4D;
>
> }
>
> .sidebar-item {
>
> -fx-background-color: transparent;
>
> -fx-background-radius: 10;
>
> -fx-border-color: transparent;
>
> -fx-border-width: 0;
>
> -fx-padding: 10 12 10 12;
>
> -fx-text-fill: #8B93A7;
>
> -fx-font-size: 13px;
>
> -fx-font-weight: 500;
>
> -fx-alignment: CENTER_LEFT;
>
> -fx-graphic-text-gap: 10;
>
> -fx-cursor: hand;
>
> }
>
> .sidebar-item:hover {
>
> -fx-background-color: rgba(255, 255, 255, 0.04);
>
> -fx-text-fill: #F5F7FA;
>
> }
>
> .sidebar-item-active {
>
> -fx-background-color: rgba(34, 211, 165, 0.10);
>
> -fx-background-radius: 10;
>
> -fx-border-color: transparent;
>
> -fx-border-width: 0;
>
> -fx-padding: 10 12 10 12;
>
> -fx-text-fill: #22D3A5;
>
> -fx-font-size: 13px;
>
> -fx-font-weight: 600;
>
> -fx-alignment: CENTER_LEFT;
>
> -fx-graphic-text-gap: 10;
>
> -fx-cursor: hand;
>
> }
>
> .btn-primary {
>
> -fx-background-color: linear-gradient(to bottom right, #22D3A5, #1BB88E);
>
> -fx-background-radius: 10;
>
> -fx-border-color: transparent;
>
> -fx-border-width: 0;
>
> -fx-text-fill: #0A0E14;
>
> -fx-font-size: 13px;
>
> -fx-font-weight: 700;
>
> -fx-cursor: hand;
>
> -fx-padding: 9 18 9 18;
>
> -fx-effect: dropshadow(gaussian, rgba(34, 211, 165, 0.28), 10, 0, 0, 2);
>
> }
>
> .btn-primary:hover {
>
> -fx-background-color: linear-gradient(to bottom right, #28EABB, #22D3A5);
>
> -fx-effect: dropshadow(gaussian, rgba(34, 211, 165, 0.42), 14, 0, 0, 3);
>
> }
>
> .btn-primary:pressed {
>
> -fx-background-color: #1BB88E;
>
> -fx-effect: none;
>
> }
>
> .btn-primary:disabled {
>
> -fx-background-color: rgba(34, 211, 165, 0.38);
>
> -fx-cursor: default;
>
> }
>
> .btn-emergency {
>
> -fx-background-color: rgba(255, 77, 77, 0.14);
>
> -fx-background-radius: 8;
>
> -fx-border-color: rgba(255, 77, 77, 0.42);
>
> -fx-border-radius: 8;
>
> -fx-border-width: 1;
>
> -fx-text-fill: #FF4D4D;
>
> -fx-font-size: 12px;
>
> -fx-font-weight: 700;
>
> -fx-cursor: hand;
>
> -fx-padding: 6 14 6 14;
>
> }
>
> .btn-emergency:hover {
>
> -fx-background-color: rgba(255, 77, 77, 0.26);
>
> -fx-border-color: rgba(255, 77, 77, 0.65);
>
> }
>
> .btn-emergency-active {
>
> -fx-background-color: #FF4D4D;
>
> -fx-background-radius: 8;
>
> -fx-border-color: #FF4D4D;
>
> -fx-border-radius: 8;
>
> -fx-border-width: 1;
>
> -fx-text-fill: #FFFFFF;
>
> -fx-font-size: 12px;
>
> -fx-font-weight: 700;
>
> -fx-cursor: hand;
>
> -fx-padding: 6 14 6 14;
>
> -fx-effect: dropshadow(gaussian, rgba(255, 77, 77, 0.48), 12, 0, 0, 0);
>
> }
>
> .btn-secondary {
>
> -fx-background-color: rgba(59, 157, 255, 0.10);
>
> -fx-background-radius: 8;
>
> -fx-border-color: rgba(59, 157, 255, 0.36);
>
> -fx-border-radius: 8;
>
> -fx-border-width: 1;
>
> -fx-text-fill: #3B9DFF;
>
> -fx-font-size: 13px;
>
> -fx-font-weight: 600;
>
> -fx-cursor: hand;
>
> -fx-padding: 7 16 7 16;
>
> }
>
> .btn-secondary:hover {
>
> -fx-background-color: rgba(59, 157, 255, 0.20);
>
> -fx-border-color: rgba(59, 157, 255, 0.58);
>
> }
>
> .btn-danger {
>
> -fx-background-color: rgba(255, 77, 77, 0.12);
>
> -fx-background-radius: 8;
>
> -fx-border-color: rgba(255, 77, 77, 0.38);
>
> -fx-border-radius: 8;
>
> -fx-border-width: 1;
>
> -fx-text-fill: #FF4D4D;
>
> -fx-font-size: 13px;
>
> -fx-font-weight: 700;
>
> -fx-cursor: hand;
>
> -fx-padding: 7 16 7 16;
>
> }
>
> .btn-danger:hover {
>
> -fx-background-color: rgba(255, 77, 77, 0.24);
>
> -fx-border-color: rgba(255, 77, 77, 0.62);
>
> }
>
> .icon-btn {
>
> -fx-background-color: rgba(255, 255, 255, 0.04);
>
> -fx-background-radius: 8;
>
> -fx-border-color: #2A3342;
>
> -fx-border-radius: 8;
>
> -fx-border-width: 1;
>
> -fx-pref-width: 32;
>
> -fx-pref-height: 32;
>
> -fx-min-width: 32;
>
> -fx-min-height: 32;
>
> -fx-max-width: 32;
>
> -fx-max-height: 32;
>
> -fx-text-fill: #8B93A7;
>
> -fx-font-size: 14px;
>
> -fx-cursor: hand;
>
> -fx-padding: 0;
>
> }
>
> .icon-btn:hover {
>
> -fx-background-color: rgba(255, 255, 255, 0.08);
>
> -fx-text-fill: #F5F7FA;
>
> }
>
> .text-field {
>
> -fx-background-color: rgba(255, 255, 255, 0.04);
>
> -fx-background-radius: 10;
>
> -fx-border-color: #2A3342;
>
> -fx-border-radius: 10;
>
> -fx-border-width: 1;
>
> -fx-text-fill: #F5F7FA;
>
> -fx-prompt-text-fill: #8B93A7;
>
> -fx-font-size: 13px;
>
> -fx-padding: 10 12 10 12;
>
> -fx-highlight-fill: rgba(34, 211, 165, 0.25);
>
> -fx-highlight-text-fill: #F5F7FA;
>
> }
>
> .text-field:focused {
>
> -fx-border-color: #22D3A5;
>
> -fx-background-color: rgba(34, 211, 165, 0.04);
>
> -fx-effect: dropshadow(gaussian, rgba(34, 211, 165, 0.16), 8, 0, 0, 0);
>
> }
>
> .text-field:disabled {
>
> -fx-opacity: 0.45;
>
> }
>
> .password-field {
>
> -fx-background-color: rgba(255, 255, 255, 0.04);
>
> -fx-background-radius: 10;
>
> -fx-border-color: #2A3342;
>
> -fx-border-radius: 10;
>
> -fx-border-width: 1;
>
> -fx-text-fill: #F5F7FA;
>
> -fx-prompt-text-fill: #8B93A7;
>
> -fx-font-size: 13px;
>
> -fx-padding: 10 12 10 12;
>
> -fx-highlight-fill: rgba(34, 211, 165, 0.25);
>
> -fx-highlight-text-fill: #F5F7FA;
>
> }
>
> .password-field:focused {
>
> -fx-border-color: #22D3A5;
>
> -fx-background-color: rgba(34, 211, 165, 0.04);
>
> -fx-effect: dropshadow(gaussian, rgba(34, 211, 165, 0.16), 8, 0, 0, 0);
>
> }
>
> .check-box {
>
> -fx-text-fill: #8B93A7;
>
> -fx-font-size: 12px;
>
> }
>
> .check-box .box {
>
> -fx-background-color: rgba(255, 255, 255, 0.04);
>
> -fx-background-radius: 5;
>
> -fx-border-color: #2A3342;
>
> -fx-border-radius: 5;
>
> -fx-border-width: 1;
>
> -fx-pref-width: 15;
>
> -fx-pref-height: 15;
>
> }
>
> .check-box:selected .box {
>
> -fx-background-color: #22D3A5;
>
> -fx-border-color: #22D3A5;
>
> }
>
> .check-box:selected .mark {
>
> -fx-background-color: #0A0E14;
>
> }
>
> .check-box:focused .box {
>
> -fx-border-color: #22D3A5;
>
> }
>
> .card-title {
>
> -fx-font-size: 13px;
>
> -fx-font-weight: 600;
>
> -fx-text-fill: #F5F7FA;
>
> }
>
> .card-title-muted {
>
> -fx-font-size: 12px;
>
> -fx-font-weight: 500;
>
> -fx-text-fill: #8B93A7;
>
> }
>
> .data-label {
>
> -fx-font-size: 11px;
>
> -fx-font-weight: 500;
>
> -fx-text-fill: #8B93A7;
>
> }
>
> .data-value-green {
>
> -fx-font-family: \"JetBrains Mono\";
>
> -fx-font-size: 13px;
>
> -fx-font-weight: 600;
>
> -fx-text-fill: #22D3A5;
>
> }
>
> .data-value-amber {
>
> -fx-font-family: \"JetBrains Mono\";
>
> -fx-font-size: 13px;
>
> -fx-font-weight: 600;
>
> -fx-text-fill: #FFA53E;
>
> }
>
> .separator-dark .line {
>
> -fx-border-color: #1E2A3A;
>
> -fx-border-width: 1 0 0 0;
>
> }
>
> .system-node {
>
> -fx-background-color: rgba(21, 27, 38, 0.90);
>
> -fx-background-radius: 12;
>
> -fx-border-color: #2A3342;
>
> -fx-border-radius: 12;
>
> -fx-border-width: 1;
>
> -fx-padding: 12 16 12 16;
>
> -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.30), 8, 0, 0, 2);
>
> }
>
> .system-node-icon {
>
> -fx-font-size: 20px;
>
> }
>
> .system-node-label {
>
> -fx-font-size: 12px;
>
> -fx-font-weight: 600;
>
> -fx-text-fill: #F5F7FA;
>
> }
>
> .system-node-sub {
>
> -fx-font-family: \"JetBrains Mono\";
>
> -fx-font-size: 11px;
>
> -fx-text-fill: #8B93A7;
>
> }
>
> .system-arrow {
>
> -fx-text-fill: #2A3342;
>
> -fx-font-size: 18px;
>
> }
>
> .badge-success {
>
> -fx-background-color: rgba(34, 211, 165, 0.13);
>
> -fx-background-radius: 12;
>
> -fx-border-color: rgba(34, 211, 165, 0.32);
>
> -fx-border-radius: 12;
>
> -fx-border-width: 1;
>
> -fx-padding: 2 8 2 8;
>
> -fx-text-fill: #22D3A5;
>
> -fx-font-size: 10px;
>
> -fx-font-weight: 700;
>
> }
>
> .badge-critical {
>
> -fx-background-color: rgba(255, 77, 77, 0.13);
>
> -fx-background-radius: 12;
>
> -fx-border-color: rgba(255, 77, 77, 0.32);
>
> -fx-border-radius: 12;
>
> -fx-border-width: 1;
>
> -fx-padding: 2 8 2 8;
>
> -fx-text-fill: #FF4D4D;
>
> -fx-font-size: 10px;
>
> -fx-font-weight: 700;
>
> }
>
> .badge-warning {
>
> -fx-background-color: rgba(255, 165, 62, 0.13);
>
> -fx-background-radius: 12;
>
> -fx-border-color: rgba(255, 165, 62, 0.32);
>
> -fx-border-radius: 12;
>
> -fx-border-width: 1;
>
> -fx-padding: 2 8 2 8;
>
> -fx-text-fill: #FFA53E;
>
> -fx-font-size: 10px;
>
> -fx-font-weight: 700;
>
> }
>
> .badge-info {
>
> -fx-background-color: rgba(59, 157, 255, 0.13);
>
> -fx-background-radius: 12;
>
> -fx-border-color: rgba(59, 157, 255, 0.32);
>
> -fx-border-radius: 12;
>
> -fx-border-width: 1;
>
> -fx-padding: 2 8 2 8;
>
> -fx-text-fill: #3B9DFF;
>
> -fx-font-size: 10px;
>
> -fx-font-weight: 700;
>
> }
>
> .priority-critical {
>
> -fx-background-color: rgba(255, 77, 77, 0.13);
>
> -fx-background-radius: 20;
>
> -fx-border-color: rgba(255, 77, 77, 0.34);
>
> -fx-border-radius: 20;
>
> -fx-border-width: 1;
>
> -fx-padding: 3 10 3 10;
>
> -fx-text-fill: #FF4D4D;
>
> -fx-font-size: 11px;
>
> -fx-font-weight: 700;
>
> }
>
> .priority-important {
>
> -fx-background-color: rgba(255, 165, 62, 0.13);
>
> -fx-background-radius: 20;
>
> -fx-border-color: rgba(255, 165, 62, 0.34);
>
> -fx-border-radius: 20;
>
> -fx-border-width: 1;
>
> -fx-padding: 3 10 3 10;
>
> -fx-text-fill: #FFA53E;
>
> -fx-font-size: 11px;
>
> -fx-font-weight: 700;
>
> }
>
> .priority-low {
>
> -fx-background-color: rgba(139, 147, 167, 0.12);
>
> -fx-background-radius: 20;
>
> -fx-border-color: rgba(139, 147, 167, 0.30);
>
> -fx-border-radius: 20;
>
> -fx-border-width: 1;
>
> -fx-padding: 3 10 3 10;
>
> -fx-text-fill: #8B93A7;
>
> -fx-font-size: 11px;
>
> -fx-font-weight: 700;
>
> }
>
> .status-on {
>
> -fx-text-fill: #22D3A5;
>
> -fx-font-size: 12px;
>
> -fx-font-weight: 500;
>
> }
>
> .status-off {
>
> -fx-text-fill: #8B93A7;
>
> -fx-font-size: 12px;
>
> -fx-font-weight: 500;
>
> }
>
> .status-ai-off {
>
> -fx-text-fill: #3B9DFF;
>
> -fx-font-size: 12px;
>
> -fx-font-weight: 500;
>
> }
>
> .status-offline {
>
> -fx-text-fill: #FF4D4D;
>
> -fx-font-size: 12px;
>
> -fx-font-weight: 500;
>
> }
>
> .alert-critical {
>
> -fx-border-color: transparent transparent transparent rgba(255, 77, 77, 0.60);
>
> -fx-border-width: 0 0 0 3;
>
> -fx-background-color: rgba(255, 77, 77, 0.06);
>
> -fx-background-radius: 0 8 8 0;
>
> -fx-padding: 10 12 10 12;
>
> }
>
> .alert-warning {
>
> -fx-border-color: transparent transparent transparent rgba(255, 165, 62, 0.60);
>
> -fx-border-width: 0 0 0 3;
>
> -fx-background-color: rgba(255, 165, 62, 0.06);
>
> -fx-background-radius: 0 8 8 0;
>
> -fx-padding: 10 12 10 12;
>
> }
>
> .alert-info {
>
> -fx-border-color: transparent transparent transparent rgba(59, 157, 255, 0.60);
>
> -fx-border-width: 0 0 0 3;
>
> -fx-background-color: rgba(59, 157, 255, 0.06);
>
> -fx-background-radius: 0 8 8 0;
>
> -fx-padding: 10 12 10 12;
>
> }
>
> .bubble-user {
>
> -fx-background-color: rgba(34, 211, 165, 0.15);
>
> -fx-background-radius: 16 16 4 16;
>
> -fx-border-color: rgba(34, 211, 165, 0.26);
>
> -fx-border-radius: 16 16 4 16;
>
> -fx-border-width: 1;
>
> -fx-padding: 10 14 10 14;
>
> -fx-max-width: 280;
>
> }
>
> .bubble-user .label {
>
> -fx-text-fill: #F5F7FA;
>
> -fx-font-size: 13px;
>
> -fx-wrap-text: true;
>
> -fx-line-spacing: 2;
>
> }
>
> .bubble-ai {
>
> -fx-background-color: rgba(21, 27, 38, 0.92);
>
> -fx-background-radius: 16 16 16 4;
>
> -fx-border-color: #2A3342;
>
> -fx-border-radius: 16 16 16 4;
>
> -fx-border-width: 1;
>
> -fx-padding: 10 14 10 14;
>
> -fx-max-width: 300;
>
> }
>
> .bubble-ai .label {
>
> -fx-text-fill: #F5F7FA;
>
> -fx-font-size: 13px;
>
> -fx-wrap-text: true;
>
> -fx-line-spacing: 2;
>
> }
>
> .timeline-item {
>
> -fx-background-color: rgba(21, 27, 38, 0.62);
>
> -fx-background-radius: 0 8 8 0;
>
> -fx-border-color: transparent transparent transparent #3B9DFF;
>
> -fx-border-width: 0 0 0 3;
>
> -fx-padding: 10 12 10 12;
>
> }
>
> .timeline-item-done {
>
> -fx-background-color: rgba(21, 27, 38, 0.62);
>
> -fx-background-radius: 0 8 8 0;
>
> -fx-border-color: transparent transparent transparent #22D3A5;
>
> -fx-border-width: 0 0 0 3;
>
> -fx-padding: 10 12 10 12;
>
> }
>
> .emergency {
>
> -fx-border-color: rgba(255, 77, 77, 0.55);
>
> -fx-border-width: 2;
>
> }
>
> .period-btn {
>
> -fx-background-color: rgba(255, 255, 255, 0.04);
>
> -fx-background-radius: 20;
>
> -fx-border-color: #2A3342;
>
> -fx-border-radius: 20;
>
> -fx-border-width: 1;
>
> -fx-text-fill: #8B93A7;
>
> -fx-font-size: 12px;
>
> -fx-cursor: hand;
>
> -fx-padding: 5 14 5 14;
>
> }
>
> .period-btn:hover {
>
> -fx-background-color: rgba(255, 255, 255, 0.07);
>
> -fx-text-fill: #F5F7FA;
>
> }
>
> .period-btn-active {
>
> -fx-background-color: rgba(34, 211, 165, 0.14);
>
> -fx-background-radius: 20;
>
> -fx-border-color: rgba(34, 211, 165, 0.42);
>
> -fx-border-radius: 20;
>
> -fx-border-width: 1;
>
> -fx-text-fill: #22D3A5;
>
> -fx-font-size: 12px;
>
> -fx-font-weight: 600;
>
> -fx-cursor: hand;
>
> -fx-padding: 5 14 5 14;
>
> }

**15. Fichiers complémentaires à créer**

Le projet est complet à 100% sur la partie visuelle et logique (16/16 vues FXML, 9/9 contrôleurs, 8/8 ViewModels, 3/3 fichiers CSS). Il ne reste que 2 fichiers de configuration pure, sans dimension visuelle, à écrire avant une première compilation Maven complète :

  ------------------------------------------- ---------------------------------------------------------------------------------------
  **Fichier**                                 **Rôle**

  application.properties                      api.base-url=http://localhost:8000, timeouts, cle de persistance du JWT

  config/AppConfig.java                       Chargeur type de application.properties (Properties Java, aucune dependance visuelle)

  src/test/\.../DashboardViewModelTest.java   Test JUnit 5 + Mockito du ViewModel Dashboard

  src/test/\.../EquipmentViewModelTest.java   Test du filtrage et du toggle d\'equipement

  src/test/\.../AuthViewModelTest.java        Test du flux de connexion (succes/echec)

  src/test/\.../ApiClientTest.java            Test des methodes HTTP generiques (mock OkHttp)
  ------------------------------------------- ---------------------------------------------------------------------------------------
