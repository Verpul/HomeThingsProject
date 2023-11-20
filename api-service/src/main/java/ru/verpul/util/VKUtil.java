package ru.verpul.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class VKUtil {

    public static int getDateTimeAsEpoch(String beginDate, String beginTime) {
        LocalDate newsDate = LocalDate.parse(beginDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        LocalTime newsTime = LocalTime.parse(beginTime, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime newsDateTime = LocalDateTime.of(newsDate, newsTime);

        return (int) newsDateTime.atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    public static String getEpochAsDateTime(int epoch) {
        Instant instant = Instant.ofEpochSecond(epoch);

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return localDateTime.format(formatter);
    }

}
