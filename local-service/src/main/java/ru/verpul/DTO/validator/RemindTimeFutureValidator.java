package ru.verpul.DTO.validator;

import ru.verpul.DTO.ReminderDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalTime;

public class RemindTimeFutureValidator implements ConstraintValidator<RemindTimeFuture, ReminderDTO> {

    @Override
    public void initialize(RemindTimeFuture constraintAnnotation) {
    }

    @Override
    public boolean isValid(ReminderDTO reminder, ConstraintValidatorContext context) {
        LocalTime reminderTime = reminder.getRemindTime();
        if (reminderTime == null) return true;

        LocalDate reminderLocalDate = reminder.getRemindDate();

        boolean valid = (reminderLocalDate != null && reminderLocalDate.isBefore(LocalDate.now())) ||
                reminderTime.isAfter(LocalTime.now());

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("remindTime")
                    .addConstraintViolation();
        }
        return valid;
    }
}
