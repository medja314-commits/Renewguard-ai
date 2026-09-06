package fr.renewguard.component;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

public class SkeletonCardController implements Initializable {

    @FXML private VBox skeletonRoot;
    @FXML private Region shimmerBar1;
    @FXML private Region shimmerBar2;
    @FXML private Region shimmerBar3;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize skeleton card with shimmer animation
    }
}
