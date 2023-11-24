package ru.verpul.DTO;

import lombok.Data;
import ru.verpul.DTO.validator.UniqueNoteCategoryTitle;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@UniqueNoteCategoryTitle(message = "Наименование категории должно быт уникальным")
public class NoteCategoryDTO {
    private Long id;

    @NotBlank(message = "Название должно быть заполнено")
    @Size(max = 200, message = "Название категории не может превышать 200 символов")
    private String title;
}
