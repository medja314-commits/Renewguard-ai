package fr.renewguard.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.api.FxRobot;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import fr.renewguard.model.dto.EquipmentDto;
import fr.renewguard.model.enums.PriorityLevel;
import fr.renewguard.model.enums.EquipmentStatus;
import fr.renewguard.viewmodel.PrioritiesViewModel;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for Kanban validation scenarios in PrioritiesController.
 *
 * Tested scenarios:
 * 1. Vider Niveau 1 (cannot empty critical level)
 * 2. Déplacer vers Niveau 1 >80% (warning when critical > 80%)
 * 3. Saut direct 1→3 (warning when jumping from critical to low)
 * 4. Dismiss warning (click × to dismiss warning banner)
 * 5. Warnings successifs (multiple warnings in sequence)
 */
public class PrioritiesControllerIntegrationTest extends ApplicationTest {

    private PrioritiesController controller;
    private PrioritiesViewModel viewModel;
    private Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/renewguard/fxml/Priorities.fxml"));
        javafx.scene.Parent root = loader.load();
        controller = loader.getController();
        viewModel = controller.vm;

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Priorities - Integration Test");
        stage.show();
    }

    @BeforeEach
    public void setup() {
        // Clear all levels
        viewModel.getLevel1().clear();
        viewModel.getLevel2().clear();
        viewModel.getLevel3().clear();

        // Setup initial state: Level1 has 1 equipment (critical)
        EquipmentDto equipment1 = createEquipment(1L, "Equipment 1", 1000, PriorityLevel.CRITICAL);
        viewModel.getLevel1().add(equipment1);
    }

    /**
     * Scenario 1: Vider Niveau 1
     * Validation: Cannot move the only critical equipment to another level
     */
    @Test
    public void testCannotEmptyLevel1() {
        EquipmentDto singleCritical = viewModel.getLevel1().get(0);

        // Try to move to level 3
        viewModel.moveEquipment(singleCritical, 3, 0);

        // Verify: Equipment stays in level 1, warning should appear
        assertTrue(viewModel.getLevel1().contains(singleCritical),
            "Equipment should remain in Level 1 when it's the only critical");
        assertTrue(viewModel.warningVisibleProperty().get(),
            "Warning should be visible when trying to empty Level 1");
        assertTrue(viewModel.warningMessageProperty().get().contains("ne peut pas être vidé"),
            "Warning message should mention Level 1 cannot be emptied");
    }

    /**
     * Scenario 2: Déplacer vers Niveau 1 >80%
     * When moving equipment to level 1, if total power > 80%, show warning
     */
    @Test
    public void testWarningWhenCriticalPowerExceeds80Percent() {
        // Add equipments: Level 1 = 1000W, Level 2 = 500W, Level 3 = 500W (total 2000W)
        EquipmentDto eq2 = createEquipment(2L, "Equipment 2", 500, PriorityLevel.IMPORTANT);
        EquipmentDto eq3 = createEquipment(3L, "Equipment 3", 500, PriorityLevel.LOW);
        viewModel.getLevel2().add(eq2);
        viewModel.getLevel3().add(eq3);

        // Try to move eq2 (500W) to level 1
        // This would make level 1 = 1500W out of 2000W = 75% (< 80%, no warning yet)
        viewModel.moveEquipment(eq2, 1, 0);
        assertFalse(viewModel.warningVisibleProperty().get(),
            "No warning at 75% power in Level 1");

        // Now add eq3 (500W) to level 1
        // This would make level 1 = 2000W out of 2000W = 100% (> 80%, should warn)
        viewModel.moveEquipment(eq3, 1, 0);
        assertTrue(viewModel.warningVisibleProperty().get(),
            "Warning should appear when Level 1 power > 80%");
    }

    /**
     * Scenario 3: Saut direct 1→3
     * Moving directly from Level 1 (critical) to Level 3 (low) should warn
     */
    @Test
    public void testWarningWhenJumpingFrom1To3() {
        EquipmentDto critical = viewModel.getLevel1().get(0);

        // Add another equipment to level 2 so level 1 isn't empty
        EquipmentDto eq2 = createEquipment(2L, "Equipment 2", 500, PriorityLevel.CRITICAL);
        viewModel.getLevel1().add(eq2);

        // Try to move critical equipment directly to level 3 (skipping level 2)
        viewModel.moveEquipment(critical, 3, 0);

        // Verify: Warning should be shown
        assertTrue(viewModel.warningVisibleProperty().get(),
            "Warning should appear when jumping from Level 1 directly to Level 3");
        assertTrue(viewModel.warningMessageProperty().get().contains("retirez"),
            "Warning should mention removing from critical protection");
    }

    /**
     * Scenario 4: Dismiss warning
     * Click × button to dismiss the warning banner
     */
    @Test
    public void testDismissWarning() {
        EquipmentDto critical = viewModel.getLevel1().get(0);
        EquipmentDto eq2 = createEquipment(2L, "Equipment 2", 500, PriorityLevel.CRITICAL);
        viewModel.getLevel1().add(eq2);

        // Trigger warning
        viewModel.moveEquipment(critical, 3, 0);
        assertTrue(viewModel.warningVisibleProperty().get(), "Warning should be visible");

        // Dismiss warning via ViewModel (simulating click on × button)
        viewModel.dismissWarning();

        // Verify: Warning is dismissed
        assertFalse(viewModel.warningVisibleProperty().get(),
            "Warning should be dismissed after clicking ×");
    }

    /**
     * Scenario 5: Warnings successifs
     * Multiple warnings in sequence should be handled correctly
     */
    @Test
    public void testMultipleWarningsInSequence() {
        // Setup: Level 1 = 1 eq, Level 2 = 1 eq, Level 3 = 0
        EquipmentDto eq1 = viewModel.getLevel1().get(0);
        EquipmentDto eq2 = createEquipment(2L, "Equipment 2", 500, PriorityLevel.IMPORTANT);
        viewModel.getLevel2().add(eq2);

        // Warning 1: Try to jump 1→3
        viewModel.moveEquipment(eq1, 3, 0);
        assertTrue(viewModel.warningVisibleProperty().get(), "First warning should appear");
        String firstWarning = viewModel.warningMessageProperty().get();

        // Dismiss first warning
        viewModel.dismissWarning();
        assertFalse(viewModel.warningVisibleProperty().get(), "First warning dismissed");

        // Warning 2: Try to empty level 1 (now with only eq2 left in it... actually moved eq1)
        // Let's move eq2 to level 1 to create >80% warning
        EquipmentDto eq3 = createEquipment(3L, "Equipment 3", 1500, PriorityLevel.LOW);
        viewModel.getLevel3().add(eq3);

        // Move eq3 to level 1 (would exceed 80%)
        viewModel.moveEquipment(eq3, 1, 0);
        assertTrue(viewModel.warningVisibleProperty().get(), "Second warning should appear");
        String secondWarning = viewModel.warningMessageProperty().get();

        // Verify: Different warnings
        assertNotEquals(firstWarning, secondWarning,
            "Different scenarios should produce different warning messages");
    }

    /**
     * Helper method to create an EquipmentDto
     */
    private EquipmentDto createEquipment(long id, String name, int powerWatts, PriorityLevel priority) {
        EquipmentDto dto = new EquipmentDto();
        dto.setId(id);
        dto.setName(name);
        dto.setPowerWatts(powerWatts);
        dto.setPriority(priority);
        dto.setStatus(EquipmentStatus.ON);
        dto.setLocation("Test Location");
        return dto;
    }
}
