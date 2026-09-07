package fr.renewguard.controller;

import fr.renewguard.component.NotificationPopoverController;
import fr.renewguard.util.FxmlLoader;
import fr.renewguard.viewmodel.shared.SessionViewModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.util.Duration;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TopbarController implements Initializable {

    @FXML private Label screenTitleLabel;
    @FXML private TextField searchField;
    @FXML private Button emergencyBtn;
    @FXML private Label clockLabel;
    @FXML private Label userInitials;
    @FXML private javafx.scene.shape.Circle notifDot;
    @FXML private Label breadcrumbRoot;
    @FXML private Label breadcrumbPage;
    @FXML private Button globalSearchBtn;
    @FXML private Button notifBtn;
    @FXML private Label notifBadge;
    @FXML private StackPane notifPopoverAnchor;

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final SessionViewModel session = SessionViewModel.getInstance();
    private Runnable onLogout;
    private Popup notificationPopup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bindEmergencyButton();
        startClock();
        bindUserInitials();
    }

    public void setOnLogout(Runnable callback) {
        this.onLogout = callback;
    }

    public void setTitle(String title) {
        screenTitleLabel.setText(title);
        if (breadcrumbPage != null) {
            breadcrumbPage.setText(title);
        }
    }

    private void bindEmergencyButton() {
        emergencyBtn.textProperty().bind(
            Bindings.when(session.emergencyModeProperty())
                .then("URGENCE ACTIVE")
                .otherwise("Mode urgence")
        );
        emergencyBtn.getStyleClass().add("btn-emergency");
    }

    @FXML
    private void onToggleEmergency() {
        session.toggleEmergency();
    }

    @FXML
    private void onGlobalSearch() {
        searchField.setVisible(true);
        searchField.setManaged(true);
        searchField.requestFocus();
    }

    @FXML
    private void onToggleNotifications() {
        fr.renewguard.viewmodel.shared.NotificationViewModel notif =
            fr.renewguard.viewmodel.shared.NotificationViewModel.getInstance();
        notif.fetchAlerts();

        if (notificationPopup == null || !notificationPopup.isShowing()) {
            FxmlLoader.Result<NotificationPopoverController> result =
                FxmlLoader.loadWithResult("/fr/renewguard/fxml/components/NotificationPopover.fxml");
            Parent popoverRoot = result.root();

            notificationPopup = new Popup();
            notificationPopup.getContent().add(popoverRoot);
            notificationPopup.setAutoHide(true);

            javafx.geometry.Bounds bounds = notifPopoverAnchor.localToScreen(notifPopoverAnchor.getBoundsInLocal());
            notificationPopup.show(notifPopoverAnchor.getScene().getWindow(),
                bounds.getCenterX() - 170,
                bounds.getCenterY() + 20);
        } else {
            notificationPopup.hide();
        }
    }

    @FXML
    private void onUserMenu() {
        ContextMenu menu = new ContextMenu();
        MenuItem profile = new MenuItem("Profil");
        MenuItem settings = new MenuItem("Parametres");
        MenuItem logout = new MenuItem("Deconnexion");
        logout.setOnAction(e -> {
            if (onLogout != null) onLogout.run();
        });
        menu.getItems().addAll(profile, settings, new SeparatorMenuItem(), logout);
        menu.show(userInitials, javafx.geometry.Side.BOTTOM, 0, 8);
    }

    private void startClock() {
        clockLabel.setText(LocalTime.now().format(TIME_FMT));
        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1),
            e -> clockLabel.setText(LocalTime.now().format(TIME_FMT))));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();
    }

    private void bindUserInitials() {
        session.usernameProperty().addListener((obs, old, name) ->
            userInitials.setText(initials(name)));
        userInitials.setText(initials(session.getUsername()));
    }

    private String initials(String name) {
        if (name == null || name.isBlank()) return "?";
        String[] parts = name.trim().split("\\s+");
        if (parts.length >= 2)
            return ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
        return name.substring(0, Math.min(2, name.length())).toUpperCase();
    }
}
