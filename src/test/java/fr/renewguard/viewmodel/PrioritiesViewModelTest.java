package fr.renewguard.viewmodel;

import fr.renewguard.model.dto.EquipmentDto;
import fr.renewguard.model.enums.PriorityLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PrioritiesViewModel - Kanban Validation Rules")
class PrioritiesViewModelTest {

    private PrioritiesViewModel viewModel;

    @BeforeEach
    void setUp() {
        viewModel = new PrioritiesViewModel();
    }

    // ────────────────────────────────────────────────────────────
    // TEST 1: RÈGLE 1 - Vider Niveau 1 complètement = BLOQUÉ
    // ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Scenario 1: Moving last critical equipment to Level 2 should be BLOCKED")
    void testBlockEmptyingLevel1() {
        // Setup: 1 equipment in Level 1
        EquipmentDto critical = new EquipmentDto();
        critical.setId(1L);
        critical.setName("Système de Refroidissement");
        critical.setPowerWatts(5000);
        critical.setPriority(PriorityLevel.CRITICAL);

        viewModel.getLevel1().add(critical);

        // Action: Try to move it to Level 2 (empty Level 1)
        viewModel.moveEquipment(critical, 2, 0);

        // Assert: Move should be blocked
        assertTrue(viewModel.warningVisibleProperty().get(),
                "warningVisible should be true when blocking move");
        assertTrue(viewModel.warningMessageProperty().get().contains("ne peut pas être vidé"),
                "Warning message should explain Niveau 1 cannot be emptied");
        assertTrue(viewModel.getLevel1().contains(critical),
                "Critical equipment should still be in Level 1 (move blocked)");
        assertEquals(1, viewModel.getLevel1().size(),
                "Level 1 should still have 1 equipment");
        assertFalse(viewModel.modifiedProperty().get(),
                "modified flag should remain false (no change applied)");
    }

    // ────────────────────────────────────────────────────────────
    // TEST 2: DÉPLACEMENT NORMAL (Level 2 → Level 1) = ACCEPTÉ
    // ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Scenario 2: Normal move from Level 2 to Level 1 should succeed without warning")
    void testNormalMove() {
        // Setup: 1 critical in L1, 1 important in L2, extra in L3 to keep % low
        // L1: 50W, L2: 30W, L3: 200W = 280W total
        // Moving 30W from L2 to L1: (50+30)/280 = 28% < 80% ✓
        EquipmentDto critical = new EquipmentDto();
        critical.setId(1L);
        critical.setName("Critique");
        critical.setPowerWatts(50);
        critical.setPriority(PriorityLevel.CRITICAL);

        EquipmentDto important = new EquipmentDto();
        important.setId(2L);
        important.setName("Important");
        important.setPowerWatts(30);
        important.setPriority(PriorityLevel.IMPORTANT);

        EquipmentDto lowPriority = new EquipmentDto();
        lowPriority.setId(3L);
        lowPriority.setName("Non-Prioritaire");
        lowPriority.setPowerWatts(200);
        lowPriority.setPriority(PriorityLevel.LOW);

        viewModel.getLevel1().add(critical);
        viewModel.getLevel2().add(important);
        viewModel.getLevel3().add(lowPriority);

        // Reset warning (ensure clean state)
        viewModel.dismissWarning();

        // Action: Move important from Level 2 to Level 1
        viewModel.moveEquipment(important, 1, 1);

        // Assert: Move should succeed, no warning
        assertFalse(viewModel.warningVisibleProperty().get(),
                "warningVisible should be false for normal move");
        assertTrue(viewModel.getLevel1().contains(important),
                "Important equipment should be in Level 1 after move");
        assertFalse(viewModel.getLevel2().contains(important),
                "Important equipment should be removed from Level 2");
        assertTrue(viewModel.modifiedProperty().get(),
                "modified flag should be true (change applied)");
        assertEquals(PriorityLevel.CRITICAL, important.getPriority(),
                "Equipment priority should be updated to CRITICAL");
    }

    // ────────────────────────────────────────────────────────────
    // TEST 3: RÈGLE 2 - >80% puissance en Level 1 = AVERTISSEMENT
    // ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Scenario 3: Adding equipment that would exceed 80% power in Level 1 should warn (non-blocking)")
    void testLevel1PowerWarning() {
        // Setup: Create scenario where adding to L1 exceeds 80%
        // L1: 160W, L2: 40W, L3: 0W = 200W total
        // Adding 40W to L1: (160 + 40) / 200 = 200 / 200 = 100% > 80% ✓
        EquipmentDto heavy1 = new EquipmentDto();
        heavy1.setId(1L);
        heavy1.setName("Équipement Lourd 1");
        heavy1.setPowerWatts(160);
        heavy1.setPriority(PriorityLevel.CRITICAL);

        EquipmentDto toMove = new EquipmentDto();
        toMove.setId(2L);
        toMove.setName("À déplacer");
        toMove.setPowerWatts(40);
        toMove.setPriority(PriorityLevel.IMPORTANT);

        viewModel.getLevel1().add(heavy1);
        viewModel.getLevel2().add(toMove);

        // Reset warning
        viewModel.dismissWarning();

        // Action: Move toMove from Level 2 to Level 1
        // L1 will have: 160 + 40 = 200W, Total = 200W, Percentage = 100% > 80%
        viewModel.moveEquipment(toMove, 1, 0);

        // Assert: Move should succeed BUT with warning
        assertTrue(viewModel.warningVisibleProperty().get(),
                "warningVisible should be true (>80% warning)");
        assertTrue(viewModel.warningMessageProperty().get().contains("majorité"),
                "Warning message should mention majorité de vos équipements");
        assertTrue(viewModel.getLevel1().contains(toMove),
                "Equipment should be added to Level 1 despite warning (non-blocking)");
        assertTrue(viewModel.modifiedProperty().get(),
                "modified flag should be true (move applied)");
        assertEquals(PriorityLevel.CRITICAL, toMove.getPriority(),
                "Equipment priority should be updated to CRITICAL");
    }

    // ────────────────────────────────────────────────────────────
    // TEST 4: RÈGLE 3 - Saut direct Level 1 → Level 3 = AVERTISSEMENT
    // ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Scenario 4: Direct jump from Level 1 to Level 3 should trigger warning (non-blocking)")
    void testLevel1To3DirectJumpWarning() {
        // Setup: 2 critical equipments in Level 1
        EquipmentDto critical1 = new EquipmentDto();
        critical1.setId(1L);
        critical1.setName("Critique A");
        critical1.setPowerWatts(1000);
        critical1.setPriority(PriorityLevel.CRITICAL);

        EquipmentDto critical2 = new EquipmentDto();
        critical2.setId(2L);
        critical2.setName("Critique B");
        critical2.setPowerWatts(1000);
        critical2.setPriority(PriorityLevel.CRITICAL);

        viewModel.getLevel1().addAll(critical1, critical2);

        // Reset warning
        viewModel.dismissWarning();

        // Action: Move critical1 directly from Level 1 to Level 3
        viewModel.moveEquipment(critical1, 3, 0);

        // Assert: Move should succeed with warning
        assertTrue(viewModel.warningVisibleProperty().get(),
                "warningVisible should be true (direct jump warning)");
        assertTrue(viewModel.warningMessageProperty().get().contains("protection critique"),
                "Warning message should mention retrait de la protection critique");
        assertTrue(viewModel.warningMessageProperty().get().contains("Critique A"),
                "Warning message should include equipment name");
        assertTrue(viewModel.getLevel3().contains(critical1),
                "Equipment should be in Level 3 (move applied, non-blocking)");
        assertFalse(viewModel.getLevel1().contains(critical1),
                "Equipment should be removed from Level 1");
        assertTrue(viewModel.modifiedProperty().get(),
                "modified flag should be true (move applied)");
        assertEquals(PriorityLevel.LOW, critical1.getPriority(),
                "Equipment priority should be updated to LOW");
    }

    // ────────────────────────────────────────────────────────────
    // TEST 5: dismissWarning() remet warningVisible à false
    // ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Scenario 5: dismissWarning() should clear warning without side effects")
    void testDismissWarning() {
        // Setup: Set a warning manually (simulating a warning state)
        viewModel.warningVisibleProperty().set(true);
        viewModel.warningMessageProperty().set("Test warning message");

        // Store initial state for comparison
        int level1SizeBefore = viewModel.getLevel1().size();
        boolean modifiedBefore = viewModel.modifiedProperty().get();

        // Action: Dismiss the warning
        viewModel.dismissWarning();

        // Assert: Only warningVisible is affected
        assertFalse(viewModel.warningVisibleProperty().get(),
                "warningVisible should be false after dismissWarning()");
        assertEquals("Test warning message", viewModel.warningMessageProperty().get(),
                "warningMessage should NOT be cleared (only visibility cleared)");
        assertEquals(level1SizeBefore, viewModel.getLevel1().size(),
                "Level 1 should not be modified");
        assertEquals(modifiedBefore, viewModel.modifiedProperty().get(),
                "modified flag should not change");
    }

    // ────────────────────────────────────────────────────────────
    // ADDITIONAL EDGE CASES
    // ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Edge Case: Multiple equipments in Level 1, removing one should succeed")
    void testMultipleEquipmentsInLevel1() {
        EquipmentDto eq1 = new EquipmentDto();
        eq1.setId(1L);
        eq1.setName("Équipement 1");
        eq1.setPowerWatts(1000);
        eq1.setPriority(PriorityLevel.CRITICAL);

        EquipmentDto eq2 = new EquipmentDto();
        eq2.setId(2L);
        eq2.setName("Équipement 2");
        eq2.setPowerWatts(1000);
        eq2.setPriority(PriorityLevel.CRITICAL);

        viewModel.getLevel1().addAll(eq1, eq2);
        viewModel.dismissWarning();

        // Action: Move eq1 to Level 2
        viewModel.moveEquipment(eq1, 2, 0);

        // Assert: Move succeeds (Level 1 still has eq2)
        assertFalse(viewModel.warningVisibleProperty().get(),
                "Should not warn when Level 1 still has other equipments");
        assertTrue(viewModel.getLevel2().contains(eq1),
                "Equipment should be in Level 2");
        assertFalse(viewModel.getLevel1().contains(eq1),
                "Equipment should be removed from Level 1");
        assertTrue(viewModel.getLevel1().contains(eq2),
                "Other equipment should remain in Level 1");
    }

    @Test
    @DisplayName("Edge Case: Moving within same level should not trigger any rule")
    void testMoveWithinSameLevel() {
        EquipmentDto eq1 = new EquipmentDto();
        eq1.setId(1L);
        eq1.setName("Équipement 1");
        eq1.setPriority(PriorityLevel.CRITICAL);

        viewModel.getLevel1().add(eq1);
        viewModel.dismissWarning();

        // Action: Move within Level 1 (same level)
        viewModel.moveEquipment(eq1, 1, 0);

        // Assert: No warning (same level is no-op)
        assertFalse(viewModel.warningVisibleProperty().get(),
                "warningVisible should be false for same-level move");
        assertTrue(viewModel.getLevel1().contains(eq1),
                "Equipment should still be in Level 1");
    }
}
