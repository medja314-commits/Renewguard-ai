package fr.renewguard.util;

import java.util.prefs.Preferences;

public final class TokenStore {

private static final TokenStore INSTANCE = new TokenStore();

private static final String PREF_KEY = "rg_jwt";

private final Preferences prefs = Preferences.userNodeForPackage(TokenStore.class);

private String memToken;

private TokenStore() { memToken = prefs.get(PREF_KEY, null); }

public static TokenStore getInstance() { return INSTANCE; }

public void save(String token, boolean persist) {

memToken = token;

if (persist) prefs.put(PREF_KEY, token);

else prefs.remove(PREF_KEY);

}

public String get() { return memToken; }

public void clear() { memToken = null; prefs.remove(PREF_KEY); }

}
