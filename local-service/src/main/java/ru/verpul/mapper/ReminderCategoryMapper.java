package ru.verpul.mapper;

import org.mapstruct.Mapper;
import ru.verpul.DTO.ReminderCategoryDTO;
import ru.verpul.model.ReminderCategory;

@Mapper(componentModel = "spring")
public interface ReminderCategoryMapper {
    ReminderCategory reminderCategoryDTOToReminderCategory(ReminderCategoryDTO reminderCategoryDTO);

    ReminderCategoryDTO reminderCategoryToReminderCategoryDTO(ReminderCategory reminderCategory);
}
