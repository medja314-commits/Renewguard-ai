package fr.renewguard.component;
 
import fr.renewguard.model.dto.EquipmentDto;
import fr.renewguard.model.enums.EquipmentStatus;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
 
public class PriorityKanbanCardController implements Initializable {
    @FXML private HBox cardRoot;
    @FXML private Label iconLabel;
    @FXML private Label nameLabel;
    @FXML private Label powerLabel;
    @FXML private Label statusDot;
    @FXML private Circle statusCircle;
 
    private EquipmentDto equipment;
    private Consumer<EquipmentDto> onDragStart;
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
 
    public void bind(EquipmentDto eq, Consumer<EquipmentDto> dragStartCallback) {
        this.equipment = eq;
        this.onDragStart = dragStartCallback;
        iconLabel.setText(eq.getIcon() != null ? eq.getIcon() : "device");
        nameLabel.setText(eq.getName());
        powerLabel.setText(eq.getPowerWatts() + " W");
        applyStatus(eq.getStatus());
        setupDrag();
    }
 
    private void applyStatus(EquipmentStatus status) {
        statusCircle.setStyle("-fx-fill: " + status.dotColor() + ";");
        statusDot.setText(status.displayLabel());
    }
 
    private void setupDrag() {
        cardRoot.setOnDragDetected(e -> {
            if (onDragStart != null) onDragStart.accept(equipment);
            Dragboard db = cardRoot.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(equipment.getId()));
            db.setContent(content);
            cardRoot.setOpacity(0.35);
            e.consume();
        });
        cardRoot.setOnDragDone(e -> { cardRoot.setOpacity(1.0); e.consume(); });
    }
}