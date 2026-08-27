package fr.renewguard.viewmodel.shared;

import fr.renewguard.model.dto.AlertDto;

import fr.renewguard.model.enums.AlertSeverity;

import fr.renewguard.service.HistoryService;

import javafx.application.Platform;

import javafx.beans.property.*;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;

import javafx.collections.ListChangeListener;

public class NotificationViewModel {

private static NotificationViewModel INSTANCE;

public static NotificationViewModel getInstance() {

if (INSTANCE == null) INSTANCE = new NotificationViewModel();

return INSTANCE;

}

private final HistoryService service = HistoryService.getInstance();

private final ObservableList<AlertDto> notifications = FXCollections.observableArrayList();

private final IntegerProperty unreadCount = new SimpleIntegerProperty(0);

private final BooleanProperty hasUnread = new SimpleBooleanProperty(false);

private NotificationViewModel() {

notifications.addListener((ListChangeListener<AlertDto>) c -> {

long count = notifications.stream()

.filter(n -> !n.isResolved() && n.getSeverity() != AlertSeverity.INFO)

.count();

unreadCount.set((int) count);

hasUnread.set(count > 0);

});

}

public void fetchAlerts() {

service.getAlerts(AlertSeverity.CRITICAL.name())

.thenAcceptAsync(notifications::setAll, Platform::runLater)

.exceptionally(ex -> null);

}

public void addLocal(AlertDto alert) { Platform.runLater(() -> notifications.add(0, alert)); }

public void markAllRead() {

notifications.forEach(n -> n.setResolved(true));

unreadCount.set(0); hasUnread.set(false);

}

public ObservableList<AlertDto> getNotifications() { return notifications; }

public IntegerProperty unreadCountProperty() { return unreadCount; }

public BooleanProperty hasUnreadProperty() { return hasUnread; }

}
