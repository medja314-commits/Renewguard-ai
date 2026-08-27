package fr.renewguard.util;

import java.util.Locale;

public final class NumberFormatter {

private static final Locale FR = Locale.FRANCE;

private NumberFormatter() {}

public static String formatKw(double kw) { return String.format(FR, "%.1f", kw); }

public static String formatKwh(double kwh) {

if (kwh >= 1000) return String.format(FR, "%.1f MWh", kwh / 1000.0);

return String.format(FR, "%.0f kWh", kwh);

}

public static String formatWatts(int watts) {

if (watts >= 1000) return String.format(FR, "%.1f kW", watts / 1000.0);

return watts + " W";

}

public static String formatVoltage(double volts) { return String.format(FR, "%.0f V", volts); }

public static String formatHz(double hz) { return String.format(FR, "%.2f Hz", hz); }

public static String formatPercent(int percent) { return percent + " %"; }

public static String formatPercentDouble(double percent) { return String.format(FR, "%.1f %%", percent); }

public static String formatCo2Kg(double kg) {

if (kg >= 1000) return String.format(FR, "%.2f t CO2", kg / 1000.0);

return String.format(FR, "%.1f kg CO2", kg);

}

public static String formatCurrency(double eur) { return String.format(FR, "%.2f EUR", eur); }

public static String formatCurrencyRounded(double eur) { return String.format(FR, "%.0f EUR", eur); }

public static String formatEta(long totalMinutes) {

if (totalMinutes <= 0) return "-";

long h = totalMinutes / 60;

long m = totalMinutes % 60;

if (h == 0) return m + "min";

return h + "h " + String.format("%02d", m) + "min";

}

public static String formatTimestamp(java.time.LocalDateTime dt) {

if (dt == null) return "-";

return dt.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm - dd/MM"));

}

public static String formatTimeOnly(java.time.LocalDateTime dt) {

if (dt == null) return "-";

return dt.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));

}

}
