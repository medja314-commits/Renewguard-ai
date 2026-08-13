package fr.renewguard.viewmodel;
 
import fr.renewguard.model.dto.AuthResponseDto;
import fr.renewguard.service.ApiClient;
import fr.renewguard.viewmodel.shared.SessionViewModel;
import javafx.application.Platform;
import javafx.beans.property.*;
 
public class AuthViewModel {
    private final StringProperty email = new SimpleStringProperty("");
    private final StringProperty password = new SimpleStringProperty("");
    private final BooleanProperty rememberMe = new SimpleBooleanProperty(true);
    private final BooleanProperty loading = new SimpleBooleanProperty(false);
    private final StringProperty errorMessage = new SimpleStringProperty(null);
    private Runnable onSuccess;
 
    public void setOnSuccess(Runnable callback) { this.onSuccess = callback; }
 
    public void login() {
        if (email.get().isBlank() || password.get().isBlank()) {
            errorMessage.set("Veuillez renseigner tous les champs.");
            return;
        }
        errorMessage.set(null);
        loading.set(true);
        var body = new LoginRequest(email.get(), password.get());
        ApiClient.getInstance().post("/auth/token", body, AuthResponseDto.class)
            .thenAcceptAsync(this::handleSuccess, Platform::runLater)
            .exceptionally(ex -> {
                Platform.runLater(() -> {
                    loading.set(false);
                    errorMessage.set("Identifiants incorrects ou serveur indisponible.");
                });
                return null;
            });
    }
 
    private void handleSuccess(AuthResponseDto dto) {
        SessionViewModel.getInstance().authenticate(
            dto.getAccessToken(), dto.getUsername(), dto.getSiteName(), rememberMe.get());
        loading.set(false);
        if (onSuccess != null) onSuccess.run();
    }
 
    public String getEmail() { return email.get(); }
    public StringProperty emailProperty() { return email; }
    public String getPassword() { return password.get(); }
    public StringProperty passwordProperty() { return password; }
    public boolean isRememberMe() { return rememberMe.get(); }
    public BooleanProperty rememberMeProperty() { return rememberMe; }
    public boolean isLoading() { return loading.get(); }
    public BooleanProperty loadingProperty() { return loading; }
    public String getErrorMessage() { return errorMessage.get(); }
    public StringProperty errorMessageProperty() { return errorMessage; }
 
    public record LoginRequest(String username, String password) {}
}
