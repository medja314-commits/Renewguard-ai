package fr.renewguard.component;
 
import fr.renewguard.model.dto.EquipmentDto;
import fr.renewguard.model.enums.EquipmentStatus;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.controlsfx.control.ToggleSwitch;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
 
public class EquipmentRowController implements Initializable {
    @FXML private ToggleSwitch toggleSwitch;
    private Consumer<Long> onToggle;
    private long equipmentId;
    private boolean programmatic = false;
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        toggleSwitch.selectedProperty().addListener((obs, old, selected) -> {
            if (!programmatic && onToggle != null) onToggle.accept(equipmentId);
        });
    }
 
    public void bind(EquipmentDto equipment, Consumer<Long> toggleCallback) {
        this.equipmentId = equipment.getId();
        this.onToggle = toggleCallback;
        programmatic = true;
        toggleSwitch.setSelected(equipment.getStatus() == EquipmentStatus.ON);
        programmatic = false;
        boolean locked = equipment.getStatus() == EquipmentStatus.AI_OFF || equipment.getStatus() == EquipmentStatus.OFFLINE;
        toggleSwitch.setDisable(locked);
        toggleSwitch.setOpacity(locked ? 0.45 : 1.0);
    }
}