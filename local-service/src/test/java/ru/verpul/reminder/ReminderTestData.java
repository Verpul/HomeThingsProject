package ru.verpul.reminder;

import ru.verpul.DTO.ReminderDTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReminderTestData {
    public static final String PARAM_CATEGORY_ID = "categoryId";
    public static final String PARAM_SHOW_COMPLETED = "showCompleted";

    public static final Long SAVED_REMINDER_ID = 16L;
    public static final int SAVED_REMINDER_NESTING_DEPTH = 1;
    public static final Long PARENT_ID = 1L;
    public static final Long NOT_FOUND_ID = 100L;
    public static final Long CATEGORY_ID = 2L;
    public static final Long REMINDER_TO_UPDATE_COMPLETED_ID = 2L;
    public static final Long REMINDER_TO_UPDATE_UNCOMPLETED_ID = 3L;
    public static final Long REMINDER_WITH_CHILDREN_ID = 7L;
    public static final Long REMINDER1_CHILD_ID = 9L;
    public static final Long REMINDER2_CHILD_ID = 11L;
    public static final Long PERIODIC_REMINDER_ID = 14L;

    public static final String REMINDER_TO_SAVE_TITLE = "Напоминание";
    public static final String REMINDER_TO_SAVE_DATE = "2030-01-01";
    public static final String REMINDER_TO_SAVE_REMIND_TIME = "23:30";

    public static ReminderDTO getNewReminderDTO() {
        return ReminderDTO.builder()
                .title(REMINDER_TO_SAVE_TITLE)
                .remindDate(LocalDate.parse(REMINDER_TO_SAVE_DATE))
                .remindTime(LocalTime.parse(REMINDER_TO_SAVE_REMIND_TIME))
                .expireDate(LocalDate.parse(REMINDER_TO_SAVE_DATE))
                .comment("Комментарий")
                .categoryId(CATEGORY_ID)
                .periodic(true)
                .periodicity(6)
                .period("Месяц")
                .build();
    }
}
