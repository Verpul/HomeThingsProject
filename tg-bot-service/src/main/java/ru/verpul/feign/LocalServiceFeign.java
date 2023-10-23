package ru.verpul.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.verpul.DTO.ReminderDTO;

import java.util.List;

@FeignClient("local-service")
public interface LocalServiceFeign {

    @GetMapping("/api/reminder/todo")
    List<ReminderDTO> getRemindersWithProperRemindDate();

    @GetMapping("/api/reminder/timed")
    List<ReminderDTO> getTimedRemindersForToday();
}
