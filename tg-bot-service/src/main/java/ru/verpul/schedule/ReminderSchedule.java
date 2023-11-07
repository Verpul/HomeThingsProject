package ru.verpul.schedule;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.verpul.DTO.ReminderDTO;
import ru.verpul.component.HomeThingsBot;
import ru.verpul.feign.LocalServiceFeign;
import ru.verpul.message.ReminderMessage;

import javax.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Setter
@Slf4j
public class ReminderSchedule {
    private final HomeThingsBot homeThingsBot;
    private final ReminderMessage reminderMessage;
    private final LocalServiceFeign localServiceFeign;
    private final DiscoveryClient discoveryClient;


    private List<ReminderDTO> timedReminders = new ArrayList<>();

    @Scheduled(cron = "0 1 0 * * *")
    private void everyDayRemindersCheck() {
        try {
            List<ReminderDTO> uncompletedReminders = localServiceFeign.getRemindersWithProperRemindDate();
            String message = reminderMessage.getRemindersMessage(uncompletedReminders);
            homeThingsBot.sendMessage(message);
        } catch (FeignException e) {
            log.error("Ошибка при загрузке данных о напоминаниях", e);
        }
    }

    @Scheduled(cron = "0 1 0 * * *")
    private void everyDayTimedRemindersCheck() {
        try {
            timedReminders = localServiceFeign.getTimedRemindersForToday();
        } catch (FeignException e) {
            log.error("Ошибка при загрузке данных о напоминаниях со временем", e);
        }
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
    private void loadTimedReminderOnStartup() {
        boolean localServiceDown = discoveryClient.getInstances("local-service").isEmpty();

        if (localServiceDown) {
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(this::loadTimedReminderOnStartup, 10, TimeUnit.SECONDS);
        } else {
            everyDayTimedRemindersCheck();
        }
    }
}
