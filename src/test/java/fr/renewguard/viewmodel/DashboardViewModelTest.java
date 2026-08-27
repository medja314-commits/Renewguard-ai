package fr.renewguard.viewmodel;

import fr.renewguard.model.dto.AiDecisionDto;
import fr.renewguard.model.dto.EnergySnapshotDto;
import fr.renewguard.model.dto.HistoryPointDto;
import fr.renewguard.service.AiService;
import fr.renewguard.service.EnergyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DashboardViewModelTest {

	@Mock
	private EnergyService energyService;

	@Mock
	private AiService aiService;

	private DashboardViewModel viewModel;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		viewModel = new DashboardViewModel(energyService, aiService);
	}

	@Test
	void testInitialValues() {
		assertEquals(0.0, viewModel.getSolarProduction());
		assertEquals(0.0, viewModel.getConsumption());
		assertEquals(0, viewModel.getAiScore());
		assertEquals(0, viewModel.getBatteryPercent());
		assertTrue(viewModel.isGridAvailable());
		assertEquals(0.0, viewModel.getSolarMix());
		assertEquals(0.0, viewModel.getBatteryMix());
		assertEquals(0.0, viewModel.getGridMix());
		assertNull(viewModel.errorProperty().get());
	}

	@Test
	void testRefreshSnapshotSuccess() throws Exception {
		EnergySnapshotDto snapshot = createMockSnapshot(
			5.2, 3.8, 85, 5, 8, 75, "2h 30m", true, 2.5,
			true, 230.0, 50.0, 0.5, 12.3, 4.5, 60.0, 20.0, 20.0
		);

		when(energyService.getSnapshot()).thenReturn(CompletableFuture.completedFuture(snapshot));

		viewModel.refreshSnapshot();
		Thread.sleep(100);

		assertEquals(5.2, viewModel.getSolarProduction());
		assertEquals(3.8, viewModel.getConsumption());
		assertEquals(85, viewModel.getAiScore());
		assertEquals(75, viewModel.getBatteryPercent());
		assertEquals("5 / 8", viewModel.equipmentCountProperty().get());
		assertEquals("2h 30m", viewModel.batteryEtaProperty().get());
		assertTrue(viewModel.batteryChargingProperty().get());
		assertTrue(viewModel.isGridAvailable());
		assertEquals(60.0, viewModel.getSolarMix());
		assertEquals(20.0, viewModel.getBatteryMix());
		assertEquals(20.0, viewModel.getGridMix());

		verify(energyService).getSnapshot();
	}

	@Test
	void testRefreshSnapshotError() throws Exception {
		Exception testError = new RuntimeException("API Error");
		when(energyService.getSnapshot()).thenReturn(CompletableFuture.failedFuture(testError));

		viewModel.refreshSnapshot();
		Thread.sleep(100);

		assertEquals("API Error", viewModel.errorProperty().get());
		verify(energyService).getSnapshot();
	}

	@Test
	void testRefreshChartSuccess() throws Exception {
		List<HistoryPointDto> points = List.of(
			createMockHistoryPoint("09:00", 2.5, 3.0),
			createMockHistoryPoint("10:00", 3.2, 2.8),
			createMockHistoryPoint("11:00", 4.1, 2.6)
		);

		when(energyService.getHistory("day"))
			.thenReturn(CompletableFuture.completedFuture(points));

		viewModel.selectedPeriodProperty().set("day");
		viewModel.refreshChart();
		Thread.sleep(100);

		assertEquals(3, viewModel.getChartData().size());
		assertEquals("09:00", viewModel.getChartData().get(0).getLabel());

		verify(energyService).getHistory("day");
	}

	@Test
	void testRefreshChartError() throws Exception {
		Exception testError = new RuntimeException("Chart API Error");
		when(energyService.getHistory("week"))
			.thenReturn(CompletableFuture.failedFuture(testError));

		viewModel.selectedPeriodProperty().set("week");
		viewModel.refreshChart();
		Thread.sleep(100);

		assertEquals("Chart API Error", viewModel.errorProperty().get());
	}

	@Test
	void testRefreshLastDecisionSuccess() throws Exception {
		AiDecisionDto decision = createMockDecision(1, "Reduce consumption", 92);

		when(aiService.getDecisions(1))
			.thenReturn(CompletableFuture.completedFuture(List.of(decision)));

		viewModel.refreshLastDecision();
		Thread.sleep(100);

		assertNotNull(viewModel.lastDecisionProperty().get());
		assertEquals(1, viewModel.lastDecisionProperty().get().getId());
		assertEquals("Reduce consumption", viewModel.lastDecisionProperty().get().getAction());
	}

	@Test
	void testRefreshLastDecisionEmpty() throws Exception {
		when(aiService.getDecisions(1))
			.thenReturn(CompletableFuture.completedFuture(List.of()));

		viewModel.refreshLastDecision();
		Thread.sleep(100);

		assertNull(viewModel.lastDecisionProperty().get());
	}

	@Test
	void testChangePeriod() {
		List<HistoryPointDto> points = List.of(
			createMockHistoryPoint("Mon", 3.0, 2.5),
			createMockHistoryPoint("Tue", 3.5, 2.3)
		);

		when(energyService.getHistory("week"))
			.thenReturn(CompletableFuture.completedFuture(points));

		viewModel.changePeriod("week");
		assertEquals("week", viewModel.selectedPeriodProperty().get());

		verify(energyService).getHistory("week");
	}

	@Test
	void testToggleSystemView() {
		assertFalse(viewModel.systemViewVisibleProperty().get());

		viewModel.toggleSystemView();
		assertTrue(viewModel.systemViewVisibleProperty().get());

		viewModel.toggleSystemView();
		assertFalse(viewModel.systemViewVisibleProperty().get());
	}

	@Test
	void testErrorMessageClearing() throws Exception {
		viewModel.errorProperty().set("Previous error");

		EnergySnapshotDto snapshot = createMockSnapshot(
			1.0, 1.0, 50, 1, 1, 50, null, false, 0,
			true, 0, 0, 0, 0, 0, 0, 0, 0
		);

		when(energyService.getSnapshot()).thenReturn(CompletableFuture.completedFuture(snapshot));

		viewModel.refreshSnapshot();
		Thread.sleep(100);

		assertNull(viewModel.errorProperty().get());
	}

	private EnergySnapshotDto createMockSnapshot(
		double solarProduction, double consumption, int aiScore,
		int activeEquipmentCount, int totalEquipmentCount,
		int batteryPercent, String batteryEta, boolean batteryCharging, double batteryChargeRateKw,
		boolean gridAvailable, double gridVoltage, double gridFrequency, double gridImport,
		double energySaved, double co2Avoided,
		double solarPercent, double batteryPercent2, double gridPercent) {

		EnergySnapshotDto snapshot = new EnergySnapshotDto();

		try {
			setFieldViaReflection(snapshot, "solarProduction", solarProduction);
			setFieldViaReflection(snapshot, "consumption", consumption);
			setFieldViaReflection(snapshot, "aiScore", aiScore);
			setFieldViaReflection(snapshot, "activeEquipmentCount", activeEquipmentCount);
			setFieldViaReflection(snapshot, "totalEquipmentCount", totalEquipmentCount);
			setFieldViaReflection(snapshot, "batteryPercent", batteryPercent);
			setFieldViaReflection(snapshot, "batteryEta", batteryEta);
			setFieldViaReflection(snapshot, "batteryCharging", batteryCharging);
			setFieldViaReflection(snapshot, "batteryChargeRateKw", batteryChargeRateKw);
			setFieldViaReflection(snapshot, "gridAvailable", gridAvailable);
			setFieldViaReflection(snapshot, "gridVoltage", gridVoltage);
			setFieldViaReflection(snapshot, "gridFrequency", gridFrequency);
			setFieldViaReflection(snapshot, "gridImport", gridImport);
			setFieldViaReflection(snapshot, "energySaved", energySaved);
			setFieldViaReflection(snapshot, "co2Avoided", co2Avoided);

			EnergySnapshotDto.EnergyMixDto energyMix = new EnergySnapshotDto.EnergyMixDto();
			setFieldViaReflection(energyMix, "solarPercent", solarPercent);
			setFieldViaReflection(energyMix, "batteryPercent", batteryPercent2);
			setFieldViaReflection(energyMix, "gridPercent", gridPercent);
			setFieldViaReflection(snapshot, "energyMix", energyMix);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create mock snapshot", e);
		}

		return snapshot;
	}

	private HistoryPointDto createMockHistoryPoint(String label, double production, double consumption) {
		HistoryPointDto dto = new HistoryPointDto();
		try {
			setFieldViaReflection(dto, "label", label);
			setFieldViaReflection(dto, "production", production);
			setFieldViaReflection(dto, "consumption", consumption);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create mock history point", e);
		}
		return dto;
	}

	private AiDecisionDto createMockDecision(long id, String action, int confidencePercent) {
		AiDecisionDto dto = new AiDecisionDto();
		try {
			setFieldViaReflection(dto, "id", id);
			setFieldViaReflection(dto, "action", action);
			setFieldViaReflection(dto, "confidencePercent", confidencePercent);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create mock decision", e);
		}
		return dto;
	}

	private void setFieldViaReflection(Object obj, String fieldName, Object value) throws Exception {
		var field = obj.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(obj, value);
	}

}
