package ru.verpul.DTO.validator;

import lombok.RequiredArgsConstructor;
import ru.verpul.DTO.ReminderCategoryDTO;
import ru.verpul.model.ReminderCategory;
import ru.verpul.service.ReminderCategoryService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UniqueReminderCategoryValidator implements ConstraintValidator<UniqueReminderCategory, ReminderCategoryDTO> {

    private final ReminderCategoryService reminderCategoryService;

    @Override
    public void initialize(UniqueReminderCategory constraintAnnotation) {}

    @Override
    public boolean isValid(ReminderCategoryDTO category, ConstraintValidatorContext context) {
        ReminderCategory foundReminderCategory = reminderCategoryService.findCategoryByTitle(category.getTitle());

        boolean valid = foundReminderCategory == null || foundReminderCategory.getId().equals(category.getId());

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("title")
                    .addConstraintViolation();
        }

        return valid;
    }
}
