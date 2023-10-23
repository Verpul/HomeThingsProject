package ru.verpul.DTO.validator;

import ru.verpul.DTO.ReminderDTO;
import ru.verpul.util.CommonUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PeriodicReminderValidator implements ConstraintValidator<PeriodicReminder, ReminderDTO> {

    @Override
    public void initialize(PeriodicReminder constraintAnnotation) {}

    @Override
    public boolean isValid(ReminderDTO reminder, ConstraintValidatorContext context) {
        if (reminder.getPeriodic() == null || !reminder.getPeriodic()) return true;

        boolean valid = reminder.getPeriod() != null && reminder.getPeriodicity() != null && reminder.getExpireDate() != null;

        if (!valid) CommonUtil.attachValidationMessageToField(context, "periodic");

        return valid;
    }
}
