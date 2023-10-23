package ru.verpul.schedule;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.verpul.DTO.ReminderDTO;
import ru.verpul.component.HomeThingsBot;
import ru.verpul.feign.LocalServiceFeign;
import ru.verpul.message.ReminderMessage;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Setter
public class ReminderSchedule {
    private final HomeThingsBot homeThingsBot;
    private final ReminderMessage reminderMessage;
    private final LocalServiceFeign localServiceFeign;

    private List<ReminderDTO> timedReminders;

    @Scheduled(cron = "0 0 0 * * *")
    private void everyDayRemindersCheck() {
        List<ReminderDTO> uncompletedReminders = localServiceFeign.getRemindersWithProperRemindDate();
        String message = reminderMessage.getRemindersMessage(uncompletedReminders);
        homeThingsBot.sendMessage(message);
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void everyDayTimedRemindersCheck() {
        timedReminders = localServiceFeign.getTimedRemindersForToday();
    }

    @Scheduled(cron = "0 * * * * *")
    private void everyMinuteTimedRemindersCheck() {
        if (timedReminders.size() != 0) {
            for (ReminderDTO reminder: timedReminders) {
                if (reminder.getRemindTime().getHour() == LocalTime.now().getHour()
                        && reminder.getRemindTime().getMinute() == LocalTime.now().getMinute()) {
                    String message = reminderMessage.getTimedReminderMessage(reminder);
                    homeThingsBot.sendMessage(message);
                }
            }
        }
    }

    @PostConstruct
    private void loadTimedRemindersOnStartup() {
        everyDayTimedRemindersCheck();
    }
}
