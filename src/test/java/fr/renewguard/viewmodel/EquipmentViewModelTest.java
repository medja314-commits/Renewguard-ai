package fr.renewguard.viewmodel;

import fr.renewguard.model.dto.EquipmentDto;
import fr.renewguard.model.enums.EquipmentStatus;
import fr.renewguard.model.enums.PriorityLevel;
import fr.renewguard.service.EquipmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipmentViewModelTest {

	@Mock
	private EquipmentService equipmentService;

	private EquipmentViewModel viewModel;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		viewModel = new EquipmentViewModel(equipmentService);
	}

	@Test
	void testInitialState() {
		assertEquals("", viewModel.searchQueryProperty().get());
		assertFalse(viewModel.loadingProperty().get());
		assertEquals(0, viewModel.getTotalCount());
		assertEquals(0, viewModel.getTotalActive());
		assertEquals(0, viewModel.getTotalPowerW());
	}

	@Test
	void testRefreshEquipmentsSuccess() throws Exception {
		List<EquipmentDto> equipments = List.of(
			createEquipment(1, "Pompe A", EquipmentStatus.ON, 500, PriorityLevel.CRITICAL),
			createEquipment(2, "Chauffage", EquipmentStatus.OFF, 2000, PriorityLevel.IMPORTANT),
			createEquipment(3, "Ventilation", EquipmentStatus.ON, 300, PriorityLevel.LOW)
		);

		when(equipmentService.getAll()).thenReturn(CompletableFuture.completedFuture(equipments));

		viewModel.refresh();
		Thread.sleep(100);

		assertEquals(3, viewModel.getTotalCount());
		assertEquals(2, viewModel.getTotalActive());
		assertEquals(800, viewModel.getTotalPowerW());
		assertFalse(viewModel.loadingProperty().get());

		verify(equipmentService).getAll();
	}

	@Test
	void testRefreshEquipmentsError() throws Exception {
		Exception testError = new RuntimeException("Service Error");
		when(equipmentService.getAll()).thenReturn(CompletableFuture.failedFuture(testError));

		viewModel.refresh();
		Thread.sleep(100);

		assertFalse(viewModel.loadingProperty().get());
		verify(equipmentService).getAll();
	}

	@Test
	void testToggleStatus() throws Exception {
		List<EquipmentDto> equipments = List.of(
			createEquipment(1, "Device", EquipmentStatus.ON, 500, PriorityLevel.CRITICAL)
		);

		when(equipmentService.getAll()).thenReturn(CompletableFuture.completedFuture(equipments));
		when(equipmentService.setStatus(1L, EquipmentStatus.OFF))
			.thenReturn(CompletableFuture.completedFuture(
				createEquipment(1, "Device", EquipmentStatus.OFF, 500, PriorityLevel.CRITICAL)
			));

		viewModel.refresh();
		Thread.sleep(100);

		assertEquals(1, viewModel.getTotalActive());

		viewModel.toggleStatus(1L);
		Thread.sleep(100);

		assertEquals(0, viewModel.getTotalActive());
		assertEquals(0, viewModel.getTotalPowerW());

		verify(equipmentService).setStatus(1L, EquipmentStatus.OFF);
	}

	@Test
	void testFilterByStatus() throws Exception {
		List<EquipmentDto> equipments = List.of(
			createEquipment(1, "Pompe A", EquipmentStatus.ON, 500, PriorityLevel.CRITICAL),
			createEquipment(2, "Chauffage", EquipmentStatus.OFF, 2000, PriorityLevel.IMPORTANT),
			createEquipment(3, "Ventilation", EquipmentStatus.ON, 300, PriorityLevel.LOW)
		);

		when(equipmentService.getAll()).thenReturn(CompletableFuture.completedFuture(equipments));

		viewModel.refresh();
		Thread.sleep(100);

		viewModel.setFilter("ACTIVE");
		assertEquals(2, viewModel.getFilteredEquipments().size());

		viewModel.setFilter("INACTIVE");
		assertEquals(1, viewModel.getFilteredEquipments().size());

		viewModel.setFilter("ALL");
		assertEquals(3, viewModel.getFilteredEquipments().size());
	}

	@Test
	void testFilterByPriority() throws Exception {
		List<EquipmentDto> equipments = List.of(
			createEquipment(1, "Critical Device", EquipmentStatus.ON, 500, PriorityLevel.CRITICAL),
			createEquipment(2, "Important Device", EquipmentStatus.ON, 2000, PriorityLevel.IMPORTANT),
			createEquipment(3, "Low Priority", EquipmentStatus.ON, 300, PriorityLevel.LOW)
		);

		when(equipmentService.getAll()).thenReturn(CompletableFuture.completedFuture(equipments));

		viewModel.refresh();
		Thread.sleep(100);

		viewModel.setFilter("CRITICAL");
		assertEquals(1, viewModel.getFilteredEquipments().size());
		assertEquals("Critical Device", viewModel.getFilteredEquipments().get(0).getName());

		viewModel.setFilter("IMPORTANT");
		assertEquals(1, viewModel.getFilteredEquipments().size());
		assertEquals("Important Device", viewModel.getFilteredEquipments().get(0).getName());

		viewModel.setFilter("LOW");
		assertEquals(1, viewModel.getFilteredEquipments().size());
		assertEquals("Low Priority", viewModel.getFilteredEquipments().get(0).getName());
	}

	@Test
	void testSearchByName() throws Exception {
		List<EquipmentDto> equipments = List.of(
			createEquipment(1, "Pompe Solaire", EquipmentStatus.ON, 500, PriorityLevel.CRITICAL),
			createEquipment(2, "Chauffage Electrique", EquipmentStatus.OFF, 2000, PriorityLevel.IMPORTANT),
			createEquipment(3, "Pompe Eau", EquipmentStatus.ON, 300, PriorityLevel.LOW)
		);

		when(equipmentService.getAll()).thenReturn(CompletableFuture.completedFuture(equipments));

		viewModel.refresh();
		Thread.sleep(100);

		viewModel.searchQueryProperty().set("Pompe");
		assertEquals(2, viewModel.getFilteredEquipments().size());

		viewModel.searchQueryProperty().set("Electrique");
		assertEquals(1, viewModel.getFilteredEquipments().size());

		viewModel.searchQueryProperty().set("");
		assertEquals(3, viewModel.getFilteredEquipments().size());
	}

	@Test
	void testSearchByLocation() throws Exception {
		EquipmentDto eq1 = createEquipment(1, "Device A", EquipmentStatus.ON, 500, PriorityLevel.CRITICAL);
		setFieldViaReflection(eq1, "location", "Garage");

		EquipmentDto eq2 = createEquipment(2, "Device B", EquipmentStatus.ON, 2000, PriorityLevel.IMPORTANT);
		setFieldViaReflection(eq2, "location", "Cuisine");

		List<EquipmentDto> equipments = List.of(eq1, eq2);

		when(equipmentService.getAll()).thenReturn(CompletableFuture.completedFuture(equipments));

		viewModel.refresh();
		Thread.sleep(100);

		viewModel.searchQueryProperty().set("Garage");
		assertEquals(1, viewModel.getFilteredEquipments().size());
		assertEquals("Device A", viewModel.getFilteredEquipments().get(0).getName());

		viewModel.searchQueryProperty().set("Cuisine");
		assertEquals(1, viewModel.getFilteredEquipments().size());
		assertEquals("Device B", viewModel.getFilteredEquipments().get(0).getName());
	}

	@Test
	void testEmptyFilterResult() throws Exception {
		List<EquipmentDto> equipments = List.of(
			createEquipment(1, "Device", EquipmentStatus.ON, 500, PriorityLevel.LOW)
		);

		when(equipmentService.getAll()).thenReturn(CompletableFuture.completedFuture(equipments));

		viewModel.refresh();
		Thread.sleep(100);

		viewModel.setFilter("CRITICAL");
		assertEquals(0, viewModel.getFilteredEquipments().size());

		viewModel.setFilter("ALL");
		viewModel.searchQueryProperty().set("NonExistent");
		assertEquals(0, viewModel.getFilteredEquipments().size());
	}

	@Test
	void testToggleStatusError() throws Exception {
		List<EquipmentDto> equipments = List.of(
			createEquipment(1, "Device", EquipmentStatus.ON, 500, PriorityLevel.CRITICAL)
		);

		when(equipmentService.getAll()).thenReturn(CompletableFuture.completedFuture(equipments));
		when(equipmentService.setStatus(1L, EquipmentStatus.OFF))
			.thenReturn(CompletableFuture.failedFuture(new RuntimeException("API Error")));

		viewModel.refresh();
		Thread.sleep(100);

		viewModel.toggleStatus(1L);
		Thread.sleep(100);

		verify(equipmentService).setStatus(1L, EquipmentStatus.OFF);
	}

	@Test
	void testLoadingState() throws Exception {
		CompletableFuture<List<EquipmentDto>> futureList = new CompletableFuture<>();

		when(equipmentService.getAll()).thenReturn(futureList);

		viewModel.refresh();
		assertTrue(viewModel.loadingProperty().get());

		futureList.complete(List.of());
		Thread.sleep(100);

		assertFalse(viewModel.loadingProperty().get());
	}

	private EquipmentDto createEquipment(long id, String name, EquipmentStatus status,
										   int powerWatts, PriorityLevel priority) {
		EquipmentDto dto = new EquipmentDto();
		try {
			setFieldViaReflection(dto, "id", id);
			setFieldViaReflection(dto, "name", name);
			setFieldViaReflection(dto, "status", status);
			setFieldViaReflection(dto, "powerWatts", powerWatts);
			setFieldViaReflection(dto, "priority", priority);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create equipment", e);
		}
		return dto;
	}

	private void setFieldViaReflection(Object obj, String fieldName, Object value) throws Exception {
		var field = obj.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(obj, value);
	}

}
