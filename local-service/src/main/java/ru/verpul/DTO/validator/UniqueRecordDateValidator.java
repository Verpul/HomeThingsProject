package ru.verpul.DTO.validator;

import lombok.RequiredArgsConstructor;
import ru.verpul.DTO.WeightRecordDTO;
import ru.verpul.service.WeightRecordService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.Objects;

@RequiredArgsConstructor
public class UniqueRecordDateValidator implements ConstraintValidator<UniqueRecordDate, Object> {
    private final WeightRecordService weightRecordService;

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        WeightRecordDTO weightRecordDTO = (WeightRecordDTO) o;
        Long id = weightRecordDTO.getId();
        LocalDate recordDate = weightRecordDTO.getWeightRecordDate();

        if (recordDate == null || recordDate.isAfter(LocalDate.now())) return true;

        Long foundId = weightRecordService.getWeightRecordByDate(recordDate);

        boolean valid = foundId == null || Objects.equals(id, foundId);

        if (!valid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("weightRecordDate")
                    .addConstraintViolation();
        }

        return valid;
    }
}
