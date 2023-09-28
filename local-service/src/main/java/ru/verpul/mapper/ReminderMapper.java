package ru.verpul.mapper;

import org.mapstruct.Mapper;
import ru.verpul.DTO.ReminderDTO;
import ru.verpul.model.Reminder;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ReminderMapper {
    Set<ReminderDTO> remindersListToRemindersDTOSet(List<Reminder> reminders);

    ReminderDTO reminderToReminderDTO(Reminder reminder);

    Reminder reminderDTOToReminder(ReminderDTO reminderDTO);
}
