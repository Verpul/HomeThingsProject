package ru.verpul.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.verpul.DTO.ReminderDTO;
import ru.verpul.schedule.ReminderSchedule;
import ru.verpul.service.TGBotService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/tg", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class TGBotController {

    private final ReminderSchedule reminderSchedule;
    private final TGBotService tgBotService;

    @PostMapping("/timed")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTimedRemindersData(@RequestBody List<ReminderDTO> timedReminders) {
        reminderSchedule.setTimedReminders(timedReminders);
    }

    @PostMapping("/twitch")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendTGNotification(@RequestBody String message) {
        tgBotService.sendNotification(message);
    }
}
