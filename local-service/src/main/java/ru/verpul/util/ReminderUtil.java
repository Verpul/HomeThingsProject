package ru.verpul.util;

import ru.verpul.enums.ReminderPeriod;

import java.util.List;
import java.util.Map;

public class ReminderUtil {
    private static final Map<ReminderPeriod, List<String>> declensions;

    static {
        declensions = Map.of(
                ReminderPeriod.MINUTE, List.of("минута", "минуты", "минут"),
                ReminderPeriod.HOUR, List.of("час", "часа", "часов"),
                ReminderPeriod.DAY, List.of("день", "дня", "дней"),
                ReminderPeriod.WEEK, List.of("неделя", "недели", "недель"),
                ReminderPeriod.MONTH, List.of("месяц", "месяца", "месяцев"),
                ReminderPeriod.YEAR, List.of("год", "года", "лет")
        );
    }

    public static String getDeclensionOfPeriod(ReminderPeriod period, int periodicity) {
        List<String> declensionsList = declensions.get(period);
        String result = "";

        int preLastDigit = periodicity % 100 / 10;

        if (preLastDigit == 1) {
            result = declensionsList.get(2);
        } else {
            switch (periodicity % 10) {
                case 1:
                    result = declensionsList.get(0);
                    break;
                case 2:
                case 3:
                case 4:
                    result = declensionsList.get(1);
                    break;
                default:
                    result = declensionsList.get(2);
            }
        }

        return periodicity +  " " + result;
    }
}
