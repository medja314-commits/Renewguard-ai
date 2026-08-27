package fr.renewguard.viewmodel.shared;

import fr.renewguard.util.TokenStore;

import javafx.beans.property.*;

public final class SessionViewModel {

private static final SessionViewModel INSTANCE = new SessionViewModel();

private final StringProperty username = new SimpleStringProperty("");

private final StringProperty siteName = new SimpleStringProperty("Site principal");

private final BooleanProperty connected = new SimpleBooleanProperty(false);

private final BooleanProperty emergencyMode = new SimpleBooleanProperty(false);

private final StringProperty activeScreen = new SimpleStringProperty("dashboard");

private SessionViewModel() {}

public static SessionViewModel getInstance() { return INSTANCE; }

public void authenticate(String token, String user, String site, boolean persist) {

TokenStore.getInstance().save(token, persist);

username.set(user);

siteName.set(site);

connected.set(true);

}

public void logout() {

TokenStore.getInstance().clear();

username.set(""); siteName.set(""); connected.set(false);

emergencyMode.set(false); activeScreen.set("dashboard");

}

public void toggleEmergency() { emergencyMode.set(!emergencyMode.get()); }

public String getUsername() { return username.get(); }

public StringProperty usernameProperty() { return username; }

public String getSiteName() { return siteName.get(); }

public StringProperty siteNameProperty() { return siteName; }

public boolean isConnected() { return connected.get(); }

public BooleanProperty connectedProperty() { return connected; }

public boolean isEmergencyMode() { return emergencyMode.get(); }

public void setEmergencyMode(boolean v) { emergencyMode.set(v); }

public BooleanProperty emergencyModeProperty() { return emergencyMode; }

public String getActiveScreen() { return activeScreen.get(); }

public void setActiveScreen(String v) { activeScreen.set(v); }

public StringProperty activeScreenProperty() { return activeScreen; }

}
