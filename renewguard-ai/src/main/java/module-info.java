module fr.renewguard {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires okhttp3;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires org.controlsfx.controls;
    requires eu.hansolo.fx.charts;
    requires org.slf4j;
    requires java.prefs;
 
    opens fr.renewguard to javafx.fxml;
    opens fr.renewguard.controller to javafx.fxml;
    opens fr.renewguard.component to javafx.fxml;
    opens fr.renewguard.model.dto to com.fasterxml.jackson.databind;
    opens fr.renewguard.model.enums to com.fasterxml.jackson.databind;
 
    exports fr.renewguard;
    exports fr.renewguard.viewmodel;
    exports fr.renewguard.viewmodel.shared;
}