package ru.verpul.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommonUtil {

    public static String getFormattedUpdateDate(String date) {
        return formatDateTime(date, "dd.MM.yyyy HH:mm");
    }

    public static String formatDateTime(String date, String pattern) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"))
                .format(DateTimeFormatter.ofPattern(pattern));
    }
}
