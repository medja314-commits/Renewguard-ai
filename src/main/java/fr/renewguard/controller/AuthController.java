package fr.renewguard.controller;

import fr.renewguard.navigation.SceneManager;

import fr.renewguard.viewmodel.AuthViewModel;

import javafx.beans.binding.Bindings;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.control.*;

import javafx.scene.layout.HBox;

import java.net.URL;

import java.util.ResourceBundle;

public class AuthController implements Initializable {

@FXML private TextField emailField;

@FXML private PasswordField passwordField;

@FXML private CheckBox rememberMeBox;

@FXML private Button submitBtn;

@FXML private Label errorLabel;

@FXML private HBox errorRow;

@FXML private Label submitBtnLabel;

private final AuthViewModel vm = new AuthViewModel();

@Override

public void initialize(URL url, ResourceBundle rb) {

emailField.textProperty().bindBidirectional(vm.emailProperty());

passwordField.textProperty().bindBidirectional(vm.passwordProperty());

rememberMeBox.selectedProperty().bindBidirectional(vm.rememberMeProperty());

submitBtn.disableProperty().bind(vm.loadingProperty());

submitBtnLabel.textProperty().bind(

Bindings.when(vm.loadingProperty()).then("Connexion en cours...").otherwise("Se connecter"));

errorRow.visibleProperty().bind(vm.errorMessageProperty().isNotNull());

errorRow.managedProperty().bind(vm.errorMessageProperty().isNotNull());

errorLabel.textProperty().bind(vm.errorMessageProperty());

vm.setOnSuccess(() -> SceneManager.navigate("main"));

passwordField.setOnAction(e -> vm.login());

emailField.setOnAction(e -> passwordField.requestFocus());

}

@FXML private void onSubmit() { vm.login(); }

}
