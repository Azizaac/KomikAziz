package com.aziz.komikaziz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DateUtils {

    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));

    private static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("HH:mm", new Locale("id", "ID"));

    public static String formatDate(long timestamp) {
        return DATE_FORMAT.format(new Date(timestamp));
    }

    public static String formatTime(long timestamp) {
        return TIME_FORMAT.format(new Date(timestamp));
    }

    public static String getTimeAgo(long timestamp) {
        long now = System.currentTimeMillis();
        long diff = now - timestamp;

        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        long days = TimeUnit.MILLISECONDS.toDays(diff);

        if (minutes < 1) {
            return "Baru saja";
        } else if (minutes < 60) {
            return minutes + " menit yang lalu";
        } else if (hours < 24) {
            return hours + " jam yang lalu";
        } else if (days < 7) {
            return days + " hari yang lalu";
        } else {
            return formatDate(timestamp);
        }
    }
}