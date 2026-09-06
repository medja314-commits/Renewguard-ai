package fr.renewguard.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

public class TopbarController implements Initializable {

    @FXML private Button globalSearchBtn;
    @FXML private Button notifBtn;
    @FXML private Label notifBadge;
    @FXML private Button userMenuBtn;
    @FXML private Label userNameLabel;
    @FXML private Label userRoleLabel;
    @FXML private Label breadcrumbRoot;
    @FXML private Label breadcrumbPage;

    private Runnable onLogoutHandler;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize topbar
    }

    public void setOnLogout(Runnable handler) {
        this.onLogoutHandler = handler;
    }

    public void setTitle(String title) {
        if (breadcrumbPage != null) {
            breadcrumbPage.setText(title);
        }
    }

    @FXML
    private void onGlobalSearch() {
        // TODO: Implement global search
    }

    @FXML
    private void onToggleNotifications() {
        // TODO: Implement notifications toggle
    }

    @FXML
    private void onUserMenu() {
        // TODO: Implement user menu
    }
}
