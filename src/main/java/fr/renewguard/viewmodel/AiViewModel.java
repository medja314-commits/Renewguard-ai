package fr.renewguard.viewmodel;

import fr.renewguard.model.dto.AiDecisionDto;

import fr.renewguard.model.dto.PredictionDto;

import fr.renewguard.service.AiService;

import javafx.application.Platform;

import javafx.beans.property.*;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;

import java.time.LocalDateTime;

import java.util.function.Consumer;

public class AiViewModel {

private final AiService service = AiService.getInstance();

private final ObservableList<PredictionDto> predictions = FXCollections.observableArrayList();

private final ObservableList<AiDecisionDto> decisions = FXCollections.observableArrayList();

public record ChatMessage(String text, boolean isUser, LocalDateTime timestamp) {}

private final ObservableList<ChatMessage> chatMessages = FXCollections.observableArrayList();

private final StringProperty chatInput = new SimpleStringProperty("");

private final BooleanProperty chatLoading = new SimpleBooleanProperty(false);

private final IntegerProperty totalDecisions = new SimpleIntegerProperty(0);

private final IntegerProperty pendingDecisions = new SimpleIntegerProperty(0);

private Consumer<ChatMessage> onNewMessage;

public void refresh() { refreshPredictions(); refreshDecisions(); }

public void refreshPredictions() {

service.getPredictions().thenAcceptAsync(p -> predictions.setAll(p), Platform::runLater)

.exceptionally(ex -> null);

}

public void refreshDecisions() {

service.getDecisions(20).thenAcceptAsync(list -> {

decisions.setAll(list);

totalDecisions.set(list.size());

pendingDecisions.set((int) list.stream().filter(AiDecisionDto::isPending).count());

}, Platform::runLater).exceptionally(ex -> null);

}

public void sendMessage() {

String text = chatInput.get();

if (text == null || text.isBlank()) return;

ChatMessage userMsg = new ChatMessage(text.trim(), true, LocalDateTime.now());

chatMessages.add(userMsg);

if (onNewMessage != null) onNewMessage.accept(userMsg);

chatInput.set(""); chatLoading.set(true);

service.sendMessage(text.trim()).thenAcceptAsync(response -> {

ChatMessage aiMsg = new ChatMessage(response.getMessage(), false,

response.getTimestamp() != null ? response.getTimestamp() : LocalDateTime.now());

chatMessages.add(aiMsg);

if (onNewMessage != null) onNewMessage.accept(aiMsg);

chatLoading.set(false);

}, Platform::runLater).exceptionally(ex -> { Platform.runLater(() -> chatLoading.set(false)); return null; });

}

public void acknowledgeDecision(long id) {

service.acknowledgeDecision(id).thenRunAsync(() -> decisions.stream()

.filter(d -> d.getId() == id).findFirst().ifPresent(d -> d.setAcknowledged(true)), Platform::runLater)

.exceptionally(ex -> null);

}

public ObservableList<PredictionDto> getPredictions() { return predictions; }

public ObservableList<AiDecisionDto> getDecisions() { return decisions; }

public ObservableList<ChatMessage> getChatMessages() { return chatMessages; }

public StringProperty chatInputProperty() { return chatInput; }

public BooleanProperty chatLoadingProperty() { return chatLoading; }

public IntegerProperty totalDecisionsProperty() { return totalDecisions; }

public IntegerProperty pendingDecisionsProperty() { return pendingDecisions; }

public void setOnNewMessage(Consumer<ChatMessage> callback) { this.onNewMessage = callback; }

}
