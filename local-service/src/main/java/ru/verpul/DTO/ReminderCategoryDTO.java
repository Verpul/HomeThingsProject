package ru.verpul.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.verpul.DTO.validator.UniqueReminderCategory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@UniqueReminderCategory(message = "Категория должна быть уникальной")
public class ReminderCategoryDTO {
    private Long id;

    @NotBlank(message = "Наименование категории должно быть заполнено")
    @Size(max = 100, message = "Название категории не должно превышать 100 символов")
    private String title;
}
