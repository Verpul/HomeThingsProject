package ru.verpul.util;

import ru.verpul.DTO.ReminderDTO;
import ru.verpul.enums.ReminderPeriod;
import ru.verpul.model.Reminder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReminderUtil {
    private static final Map<ReminderPeriod, List<String>> declensions;

    static {
        declensions = Map.of(
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

    public static void setNextExpireAndRemindDate(Reminder reminder) {
        LocalDate dateOfRemind = reminder.getRemindDate();
        LocalDate dateOfExpire = reminder.getExpireDate();
        ReminderPeriod reminderPeriod = reminder.getPeriod();
        int periodicity = reminder.getPeriodicity();
        ChronoUnit chronoUnit;

        switch (reminderPeriod) {
            case DAY:
                chronoUnit = ChronoUnit.DAYS;
                break;
            case WEEK:
                chronoUnit = ChronoUnit.WEEKS;
                break;
            case MONTH:
                chronoUnit = ChronoUnit.MONTHS;
                break;
            default:
                chronoUnit = ChronoUnit.YEARS;
        }

        reminder.setExpireDate(dateOfExpire.plus(periodicity, chronoUnit));

        if (dateOfRemind != null) {
            long daysBetween = ChronoUnit.DAYS.between(dateOfRemind, dateOfExpire);
            reminder.setRemindDate(reminder.getExpireDate().minusDays(daysBetween));
        }
    }

    public static List<ReminderDTO> treeSort(List<ReminderDTO> reminderDTOList) {
        Map<Long, List<ReminderDTO>> remindersMap = new HashMap<>();

        for (ReminderDTO reminder : reminderDTOList) {
            if (!remindersMap.containsKey(reminder.getParentId())) {
                remindersMap.put(reminder.getParentId(), new ArrayList<>());
            }
            remindersMap.get(reminder.getParentId()).add(reminder);
        }

        List<ReminderDTO> sortedReminderDTOs = new ArrayList<>();
        sortChildren(null, remindersMap, sortedReminderDTOs);

        return sortedReminderDTOs;
    }

    private static void sortChildren(Long parentId, Map<Long, List<ReminderDTO>> remindersMap, List<ReminderDTO> sortedReminders) {
        if (remindersMap.containsKey(parentId)) {
            for (ReminderDTO reminder : remindersMap.get(parentId)) {
                sortedReminders.add(reminder);
                sortChildren(reminder.getId(), remindersMap, sortedReminders);
            }
        }
    }
}
