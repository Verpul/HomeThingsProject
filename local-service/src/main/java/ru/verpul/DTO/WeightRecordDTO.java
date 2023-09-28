package ru.verpul.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.verpul.DTO.validator.UniqueRecordDate;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@UniqueRecordDate(message = "Дата записи не уникальна")
@AllArgsConstructor
@Builder
public class WeightRecordDTO {
    private Long id;

    @PastOrPresent(message = "Дата взвешивания не может быть больше текущей")
    @NotNull(message = "Поле должно быть заполнено")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate weightRecordDate;

    @NotBlank(message = "Поле должно быть заполнено")
    @Pattern(regexp = "^(([5-9]\\d(\\.\\d)?)|(^\\s*))$", message = "Вес должен быть от 50 до 99 кг ровно или с одой цифрой после точки")
    private String weightRecordValue;

    private Double differencePreviousValue;

    private String formattedWeightRecordDate;
}
