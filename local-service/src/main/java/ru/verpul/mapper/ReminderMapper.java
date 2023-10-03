package ru.verpul.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.verpul.DTO.ReminderDTO;
import ru.verpul.model.Reminder;

@Mapper(componentModel = "spring")
public interface ReminderMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ReminderDTO reminderToReminderDTO(Reminder reminder);

    Reminder reminderDTOToReminder(ReminderDTO reminderDTO);
}
