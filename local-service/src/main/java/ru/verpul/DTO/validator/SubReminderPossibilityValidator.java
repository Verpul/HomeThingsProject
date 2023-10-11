package ru.verpul.DTO.validator;

import lombok.RequiredArgsConstructor;
import ru.verpul.DTO.ReminderDTO;
import ru.verpul.model.Reminder;
import ru.verpul.service.ReminderService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class SubReminderPossibilityValidator implements ConstraintValidator<SubReminderPossibility, ReminderDTO> {

    private final ReminderService reminderService;

    @Override
    public void initialize(SubReminderPossibility constraintAnnotation) {}

    @Override
    public boolean isValid(ReminderDTO reminder, ConstraintValidatorContext context) {
        if (reminder.getParentId() == null) return true;

        Reminder parentReminder = reminderService.getParentReminder(reminder.getParentId());

        return (parentReminder.getNestingDepth() == null || parentReminder.getNestingDepth() <= 3) && !parentReminder.getCompleted();
    }
}
