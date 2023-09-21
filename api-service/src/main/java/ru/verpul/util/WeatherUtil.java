package ru.verpul.util;

import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherUtil {
    public static String getFormattedWeatherDate(String date) {
        return StringUtils.capitalize(formatDateTime(date, "EEEE dd MMM"));
    }

    public static String getWeatherTime(String date) {
        return formatDateTime(date, "HH");
    }

    public static String getFormattedWeatherUpdateDate(String date) {
        return formatDateTime(date, "dd.MM.yyyy HH:mm");
    }


    public static String roundAndFormatNumber(double number) {
        String round = String.format("%.0f", number);

        return round.equals("-0") ? "0" : round;
    }

    private static String formatDateTime(String date, String pattern) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"))
                .format(DateTimeFormatter.ofPattern(pattern));
    }
}
