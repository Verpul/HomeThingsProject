package ru.verpul.reminder;

import ru.verpul.DTO.ReminderCategoryDTO;
import ru.verpul.model.ReminderCategory;

public class ReminderCategoryTestData {
    public static final String FIRST_CATEGORY_TITLE = "Категория 1";
    public static final Long FIRST_CATEGORY_ID = 1L;
    public static final Long CATEGORY_WITH_REMINDER_ID = 2L;
    public static final Long NOT_FOUND_ID = 100L;

    public static ReminderCategoryDTO getNewCategory() {
        return new ReminderCategoryDTO(null, "Категория 3");
    }

    public static ReminderCategoryDTO getUpdatedCategory() {
        return new ReminderCategoryDTO(1L, "Категория 1 updated");
    }
}
