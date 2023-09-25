package ru.verpul.util;

import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherUtil {
    public static String getFormattedWeatherDate(String date) {
        return StringUtils.capitalize(CommonUtil.formatDateTime(date, "EEEE dd MMM"));
    }

    public static String getWeatherTime(String date) {
        return CommonUtil.formatDateTime(date, "HH");
    }

    public static String roundAndFormatNumber(double number) {
        String round = String.format("%.0f", number);

        return round.equals("-0") ? "0" : round;
    }
}
