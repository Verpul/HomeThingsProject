package ru.verpul.enums;

import ru.verpul.exception.NotFoundException;

public enum ReminderPeriod {
    MINUTE("Минута"), HOUR("Час"), DAY("День"), WEEK("Неделя"), MONTH("Месяц"), YEAR("Год");

    private final String title;

    ReminderPeriod(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return this.title;
    }

    public String getTitle() {
        return this.title;
    }

    public static ReminderPeriod findByTitle(String title) {
        for (ReminderPeriod period : ReminderPeriod.values()) {
            if (period.getTitle().equals(title)) return period;
        }
        throw new NotFoundException("Период " + title + " не найден");
    }
}
