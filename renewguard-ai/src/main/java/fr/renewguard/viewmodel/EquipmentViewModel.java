package fr.renewguard.viewmodel;
 
import fr.renewguard.model.dto.EquipmentDto;
import fr.renewguard.model.enums.EquipmentStatus;
import fr.renewguard.model.enums.PriorityLevel;
import fr.renewguard.service.EquipmentService;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import java.util.function.Predicate;
 
public class EquipmentViewModel {
    private final EquipmentService service = EquipmentService.getInstance();
    private final ObservableList<EquipmentDto> allEquipments = FXCollections.observableArrayList();
    private final FilteredList<EquipmentDto> filteredEquipments = new FilteredList<>(allEquipments, e -> true);
    private final StringProperty searchQuery = new SimpleStringProperty("");
    private final StringProperty activeFilter = new SimpleStringProperty("ALL");
    private final BooleanProperty loading = new SimpleBooleanProperty(false);
    private final StringProperty errorMessage = new SimpleStringProperty(null);
    private final IntegerProperty totalActive = new SimpleIntegerProperty(0);
    private final IntegerProperty totalPowerW = new SimpleIntegerProperty(0);
 
    public EquipmentViewModel() {
        searchQuery.addListener((o, old, val) -> applyFilter());
        activeFilter.addListener((o, old, val) -> applyFilter());
    }
 
    public void refresh() {
        loading.set(true);
        service.getAll()
            .thenAcceptAsync(list -> {
                allEquipments.setAll(list);
                recalcStats();
                loading.set(false);
            }, Platform::runLater)
            .exceptionally(ex -> { Platform.runLater(() -> { errorMessage.set(ex.getMessage()); loading.set(false); }); return null; });
    }
 
    public void toggleStatus(long id) {
        allEquipments.stream().filter(e -> e.getId() == id).findFirst().ifPresent(eq -> {
            EquipmentStatus next = eq.getStatus() == EquipmentStatus.ON ? EquipmentStatus.OFF : EquipmentStatus.ON;
            eq.setStatus(next);
            int idx = allEquipments.indexOf(eq);
            allEquipments.set(idx, eq);
            recalcStats();
            service.setStatus(id, next).exceptionally(ex -> null);
        });
    }
 
    public void setFilter(String filter) { activeFilter.set(filter); }
 
    private void applyFilter() {
        String query = searchQuery.get() == null ? "" : searchQuery.get().toLowerCase();
        filteredEquipments.setPredicate(buildPredicate(query, activeFilter.get()));
    }
 
    private Predicate<EquipmentDto> buildPredicate(String query, String filter) {
        Predicate<EquipmentDto> textMatch = e -> query.isBlank()
            || e.getName().toLowerCase().contains(query)
            || (e.getLocation() != null && e.getLocation().toLowerCase().contains(query));
        Predicate<EquipmentDto> filterMatch = switch (filter) {
            case "CRITICAL" -> e -> e.getPriority() == PriorityLevel.CRITICAL;
            case "IMPORTANT" -> e -> e.getPriority() == PriorityLevel.IMPORTANT;
            case "LOW" -> e -> e.getPriority() == PriorityLevel.LOW;
            case "ACTIVE" -> e -> e.getStatus() == EquipmentStatus.ON;
            case "INACTIVE" -> e -> e.getStatus() != EquipmentStatus.ON;
            default -> e -> true;
        };
        return textMatch.and(filterMatch);
    }
 
    private void recalcStats() {
        int active = (int) allEquipments.stream().filter(e -> e.getStatus() == EquipmentStatus.ON).count();
        int power = allEquipments.stream().filter(e -> e.getStatus() == EquipmentStatus.ON)
            .mapToInt(EquipmentDto::getPowerWatts).sum();
        totalActive.set(active); totalPowerW.set(power);
    }
 
    public ObservableList<EquipmentDto> getFilteredEquipments() { return filteredEquipments; }
    public StringProperty searchQueryProperty() { return searchQuery; }
    public BooleanProperty loadingProperty() { return loading; }
    public IntegerProperty totalActiveProperty() { return totalActive; }
    public IntegerProperty totalPowerWProperty() { return totalPowerW; }
    public int getTotalCount() { return allEquipments.size(); }
    public int getTotalActive() { return totalActive.get(); }
    public int getTotalPowerW() { return totalPowerW.get(); }
}