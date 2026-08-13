package fr.renewguard.util;
 
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
 
public final class FxmlLoader {
    private FxmlLoader() {}
 
    public static Parent load(String fxmlPath) {
        return loadWithResult(fxmlPath).root();
    }
 
    public static <T> Result<T> loadWithResult(String fxmlPath) {
        URL url = Objects.requireNonNull(FxmlLoader.class.getResource(fxmlPath), "FXML not found: " + fxmlPath);
        FXMLLoader loader = new FXMLLoader(url);
        try {
            Parent root = loader.load();
            T controller = loader.getController();
            return new Result<>(root, controller);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML: " + fxmlPath, e);
        }
    }
 
    public static <T> Result<T> loadWithController(String fxmlPath, T controller) {
        URL url = Objects.requireNonNull(FxmlLoader.class.getResource(fxmlPath), "FXML not found: " + fxmlPath);
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController(controller);
        try {
            Parent root = loader.load();
            return new Result<>(root, controller);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML: " + fxmlPath, e);
        }
    }
 
    public record Result<T>(Parent root, T controller) {}
}