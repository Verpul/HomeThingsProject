package ru.verpul.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class CurrenciesRateUtil {

    public static String convertMillisToDateTime(Long millis) {
        String localDateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(millis),
                ZoneId.systemDefault()).withNano(0).format(DateTimeFormatter.ISO_DATE_TIME);

        return CommonUtil.getFormattedUpdateDate(localDateTime);
    }

    public static boolean isDateTimeSuitableForUpdate() {
        LocalDateTime today = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        int hour = today.getHour();

        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY && hour <= 17;
    }
}
