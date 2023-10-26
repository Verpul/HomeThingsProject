package ru.verpul.message;

import org.springframework.stereotype.Component;
import ru.verpul.DTO.ReminderDTO;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ReminderMessage {
    private static final String REMINDERS_TODAY_MESSAGE = "Дела на сегодня:\n";
    private static final String NO_REMINDERS_MESSAGE = "Дел требующих завершения нет";

    public String getRemindersMessage(List<ReminderDTO> uncompletedReminders) {
        StringBuilder sb = new StringBuilder();

        if (uncompletedReminders.isEmpty()) {
            sb.append(NO_REMINDERS_MESSAGE);
        } else {
            uncompletedReminders.forEach(reminder -> {
                sb.append(REMINDERS_TODAY_MESSAGE);
                sb.append(reminder.getTitle());
                if(reminder.getExpireDate() != null) {
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    sb.append(" - ").append(reminder.getExpireDate().format(dateTimeFormatter));
                }
                if (reminder.getComment() != null) sb.append("\n").append(reminder.getComment());
                sb.append("\n\n");
            });
        }

        return sb.toString();
    }

    public String getTimedReminderMessage(ReminderDTO timedReminder) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        StringBuilder sb = new StringBuilder();
        sb.append(timedReminder.getRemindTime().format(dateTimeFormatter));
        sb.append("\n");
        sb.append(timedReminder.getTitle());
        sb.append("\n");
        if(timedReminder.getComment() != null) {
            sb.append(timedReminder.getComment());
        }

        return sb.toString();
    }
}
