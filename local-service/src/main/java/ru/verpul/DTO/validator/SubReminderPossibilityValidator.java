package ru.verpul.DTO.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import ru.verpul.DTO.ReminderDTO;
import ru.verpul.model.Reminder;
import ru.verpul.service.ReminderService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class SubReminderPossibilityValidator implements ConstraintValidator<SubReminderPossibility, ReminderDTO> {

    @Value("${reminder.max-nesting-depth}")
    private int maxNestingLevel;

    private final ReminderService reminderService;

    @Override
    public void initialize(SubReminderPossibility constraintAnnotation) {
    }

    @Override
    public boolean isValid(ReminderDTO reminder, ConstraintValidatorContext context) {
        if (reminder.getParentId() == null) return true;

        Reminder parentReminder = reminderService.getParentReminder(reminder.getParentId());

        return (parentReminder.getNestingDepth() == null || parentReminder.getNestingDepth() <= maxNestingLevel - 1) && !parentReminder.getCompleted();
    }
}
