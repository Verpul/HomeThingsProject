package ru.verpul.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.verpul.DTO.ReminderDTO;

import java.util.List;

@FeignClient("tg-bot-service")
public interface TGBotFeign {

    @PostMapping("/api/tg/timed")
    void updateTimedRemindersData(@RequestBody List<ReminderDTO> timedReminder);

}
