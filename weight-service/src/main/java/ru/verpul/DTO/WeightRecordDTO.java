package ru.verpul.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.verpul.DTO.validator.UniqueRecordDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@UniqueRecordDate(message = "Дата записи не уникальна")
@AllArgsConstructor
public class WeightRecordDTO {
    private Long id;

    @NotNull(message = "Поле должно быть заполнено")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate weightRecordDate;

    @NotEmpty(message = "Поле должно быть заполнено")
    @Pattern(regexp = "^[5-9]\\d(\\.\\d)?$", message = "Неверный формат данных")
    private String weightRecordValue;
}
