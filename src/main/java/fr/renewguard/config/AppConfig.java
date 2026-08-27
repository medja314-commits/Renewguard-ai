package fr.renewguard.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AppConfig {

	private static final AppConfig INSTANCE = new AppConfig();
	private final Properties props;

	private AppConfig() {
		props = new Properties();
		try (InputStream in = getClass().getResourceAsStream("/application.properties")) {
			if (in == null) {
				throw new IOException("application.properties not found");
			}
			props.load(in);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load application.properties", e);
		}
	}

	public static AppConfig getInstance() {
		return INSTANCE;
	}

	public String getBaseUrl() {
		return props.getProperty("api.base-url", "http://localhost:8000");
	}

	public int getConnectTimeout() {
		return Integer.parseInt(props.getProperty("api.connect-timeout", "5"));
	}

	public int getReadTimeout() {
		return Integer.parseInt(props.getProperty("api.read-timeout", "10"));
	}

	public int getWriteTimeout() {
		return Integer.parseInt(props.getProperty("api.write-timeout", "10"));
	}

	public String getJwtStorageKey() {
		return props.getProperty("jwt.storage-key", "rg_jwt");
	}

	public String getProperty(String key) {
		return props.getProperty(key);
	}

	public String getProperty(String key, String defaultValue) {
		return props.getProperty(key, defaultValue);
	}

}
