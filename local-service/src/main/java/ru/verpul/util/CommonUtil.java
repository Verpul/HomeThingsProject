package ru.verpul.util;

import javax.validation.ConstraintValidatorContext;

public class CommonUtil {

    public static void attachValidationMessageToField(ConstraintValidatorContext context, String fieldName) {
        context.disableDefaultConstraintViolation();
        context
                .buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                .addPropertyNode(fieldName)
                .addConstraintViolation();
    }
}
