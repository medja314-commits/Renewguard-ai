package fr.renewguard;
 
import fr.renewguard.navigation.SceneManager;
import fr.renewguard.util.PollingScheduler;
import fr.renewguard.util.TokenStore;
import javafx.application.Application;
import javafx.stage.Stage;
 
public class RenewGuardApp extends Application {
    @Override
    public void start(Stage stage) {
        SceneManager.init(stage);
        if (TokenStore.getInstance().get() != null) {
            SceneManager.navigate("main");
        } else {
            SceneManager.navigate("auth");
        }
    }
 
    @Override
    public void stop() {
        PollingScheduler.getInstance().shutdown();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}