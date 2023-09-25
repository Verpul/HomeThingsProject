package ru.verpul.util;

import java.time.*;

public class CurrenciesRateUtil {

    public static String convertMillisToDateTime(Long millis) {
        OffsetDateTime localDateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(millis),
                ZoneId.systemDefault()).withNano(0);
        return CommonUtil.getFormattedUpdateDate(localDateTime.toString());
    }

    public static boolean isDateTimeSuitableForUpdate() {
        LocalDateTime today = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        int hour = today.getHour();

        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY && hour <= 17;
    }
}
