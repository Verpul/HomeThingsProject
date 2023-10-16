package ru.verpul.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.verpul.DTO.ReminderDTO;
import ru.verpul.enums.ReminderPeriod;
import ru.verpul.model.Reminder;
import ru.verpul.util.ReminderUtil;

@Mapper(componentModel = "spring", imports = {ReminderPeriod.class, ReminderUtil.class})
public interface ReminderMapper {
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(target = "period", expression = "java(reminder.getPeriod() != null ? reminder.getPeriod().toString() : null)")
    @Mapping(target = "declensionPeriod", expression = "java(reminder.getPeriodic() ? " +
            "ReminderUtil.getDeclensionOfPeriod(reminder.getPeriod(), reminder.getPeriodicity()) : null)")
    ReminderDTO reminderToReminderDTO(Reminder reminder);

    @Mapping(target = "period", expression = "java(reminderDTO.getPeriod() != null ? ReminderPeriod.findByTitle(reminderDTO.getPeriod()) : null)")
    @Mapping(target = "completed", constant = "false")
    @Mapping(target = "periodic", defaultValue = "false")
    @Mapping(target = "nestingDepth", ignore = true)
    Reminder reminderDTOToReminder(ReminderDTO reminderDTO);
}
