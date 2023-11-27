package ru.verpul.DTO.validator;

import lombok.RequiredArgsConstructor;
import ru.verpul.DTO.NoteCategoryDTO;
import ru.verpul.service.NoteCategoryService;
import ru.verpul.util.CommonUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

@RequiredArgsConstructor
public class UniqueNoteCategoryTitleValidator implements ConstraintValidator<UniqueNoteCategoryTitle, NoteCategoryDTO> {
    private final NoteCategoryService noteCategoryService;

    @Override
    public boolean isValid(NoteCategoryDTO noteCategory, ConstraintValidatorContext context) {
        if (noteCategory.getTitle() == null || noteCategory.getTitle().length() > 200) return true;

        Long foundId = noteCategoryService.findByTitle(noteCategory.getTitle());
        boolean valid = foundId == null || Objects.equals(foundId, noteCategory.getId());

        if (!valid) CommonUtil.attachValidationMessageToField(context, "title");

        return valid;
    }
}
