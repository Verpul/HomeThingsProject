package ru.verpul.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class ReminderCategoryDTO {
    private Long id;

    @NotBlank(message = "Наименование категории должно быть заполнено")
    @Length(max = 100, message = "Название категории не должно превышать 100 символов")
    private String title;
}
