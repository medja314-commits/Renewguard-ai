package fr.renewguard.component;

import fr.renewguard.model.dto.AlertDto;
import fr.renewguard.model.enums.AlertSeverity;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

public class NotificationPopoverController implements Initializable {

    @FXML private ListView<AlertDto> notificationList;
    @FXML private Button markAllReadBtn;
    @FXML private Button viewAllBtn;

    private fr.renewguard.viewmodel.shared.NotificationViewModel viewModel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewModel = fr.renewguard.viewmodel.shared.NotificationViewModel.getInstance();
        notificationList.setItems(viewModel.getNotifications());
        notificationList.setCellFactory(param -> new NotificationListCell());
    }

    @FXML
    private void onMarkAllRead() {
        viewModel.markAllRead();
    }

    @FXML
    private void onViewAll() {
        // TODO: Navigate to full notifications page
    }

    private static class NotificationListCell extends ListCell<AlertDto> {
        private final HBox root = new HBox();
        private final VBox textBox = new VBox();
        private final Label titleLabel = new Label();
        private final Label descLabel = new Label();
        private final Label timeLabel = new Label();
        private final Label severityLabel = new Label();

        public NotificationListCell() {
            root.setSpacing(10);
            root.setStyle("-fx-padding: 12 16 12 16; -fx-border-color: transparent transparent rgba(255,255,255,0.05) transparent; -fx-border-width: 0 0 1 0;");

            textBox.setSpacing(2);
            titleLabel.setStyle("-fx-font-family: 'Inter'; -fx-font-size: 12px; -fx-font-weight: 600; -fx-text-fill: #F5F7FA;");
            descLabel.setStyle("-fx-font-family: 'Inter'; -fx-font-size: 11px; -fx-text-fill: #8B93A7;");
            descLabel.setWrapText(true);
            timeLabel.setStyle("-fx-font-family: 'Inter'; -fx-font-size: 10px; -fx-text-fill: #4A5568;");

            severityLabel.setStyle("-fx-font-size: 16px;");

            textBox.getChildren().addAll(titleLabel, descLabel, timeLabel);
            root.setStyle("-fx-alignment: TOP_LEFT;");
            HBox.setHgrow(textBox, javafx.scene.layout.Priority.ALWAYS);
            root.getChildren().addAll(textBox, severityLabel);
        }

        @Override
        protected void updateItem(AlertDto item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                titleLabel.setText(item.getTitle());
                descLabel.setText(item.getDescription() != null ? item.getDescription() : "");
                timeLabel.setText(formatTime(item.getTimestamp()));
                severityLabel.setText(getSeverityIcon(item.getSeverity()));
                severityLabel.setStyle(getSeverityStyle(item.getSeverity()));
                setGraphic(root);
            }
        }

        private String formatTime(java.time.LocalDateTime timestamp) {
            if (timestamp == null) return "";
            java.time.Duration duration = java.time.Duration.between(timestamp, java.time.LocalDateTime.now());
            if (duration.toMinutes() < 1) return "À l'instant";
            if (duration.toMinutes() < 60) return duration.toMinutes() + " min";
            if (duration.toHours() < 24) return duration.toHours() + " h";
            return duration.toDays() + " j";
        }

        private String getSeverityIcon(AlertSeverity severity) {
            return switch (severity) {
                case CRITICAL -> "🔴";
                case WARNING -> "⚠";
                case INFO -> "ℹ";
                default -> "•";
            };
        }

        private String getSeverityStyle(AlertSeverity severity) {
            return switch (severity) {
                case CRITICAL -> "-fx-font-size: 16px; -fx-text-fill: #FF4D4D;";
                case WARNING -> "-fx-font-size: 16px; -fx-text-fill: #FFA53E;";
                case INFO -> "-fx-font-size: 16px; -fx-text-fill: #3B9DFF;";
                default -> "-fx-font-size: 16px; -fx-text-fill: #8B93A7;";
            };
        }
    }
}
