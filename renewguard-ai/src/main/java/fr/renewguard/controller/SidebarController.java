package fr.renewguard.controller;
 
import fr.renewguard.viewmodel.shared.SessionViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;
 
public class SidebarController implements Initializable {
    @FXML private VBox sidebarRoot;
    @FXML private Label siteNameLabel;
    @FXML private Button navDashboard;
    @FXML private Button navEquipment;
    @FXML private Button navPriorities;
    @FXML private Button navAi;
    @FXML private Button navHistory;
 
    private Consumer<String> navigationCallback;
    private final SessionViewModel session = SessionViewModel.getInstance();
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        siteNameLabel.textProperty().bind(session.siteNameProperty());
        setActive("dashboard");
        navDashboard.setOnAction(e -> navigate("dashboard"));
        navEquipment.setOnAction(e -> navigate("equipment"));
        navPriorities.setOnAction(e -> navigate("priorities"));
        navAi.setOnAction(e -> navigate("ai"));
        navHistory.setOnAction(e -> navigate("history"));
    }
 
    public void setNavigationCallback(Consumer<String> callback) { this.navigationCallback = callback; }
    private void navigate(String screen) { if (navigationCallback != null) navigationCallback.accept(screen); }
 
    public void setActive(String screen) {
        Map<String, Button> buttons = Map.of(
            "dashboard", navDashboard, "equipment", navEquipment,
            "priorities", navPriorities, "ai", navAi, "history", navHistory);
        buttons.forEach((key, btn) -> {
            btn.getStyleClass().removeAll("sidebar-item-active");
            btn.getStyleClass().add("sidebar-item");
        });
        Button active = buttons.get(screen);
        if (active != null) {
            active.getStyleClass().remove("sidebar-item");
            active.getStyleClass().add("sidebar-item-active");
        }
    }
}