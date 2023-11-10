package ru.verpul.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.verpul.enums.CurrencyType;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class CurrencyDTO {
    private Long id;

    @NotNull(message = "Валюта обязательна к заполнению")
    private CurrencyType currencyFrom;

    @Positive(message = "Количество не может быть меньше единицы")
    @NotNull(message = "Количество должно быть заполнено")
    private Double currencyFromAmount;

    @NotNull(message = "Валюта обязательна к заполнению")
    private CurrencyType currencyTo;

    @PositiveOrZero(message = "Количество не может быть меньше нуля")
    @NotNull(message = "Количество должно быть заполнено")
    private Double currencyToAmount;

    @NotNull(message = "Курс должен быть заполнен")
    @PositiveOrZero(message = "Курс не может быть меньше нуля")
    private Double rate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Дата обмена не может быть больше текущей")
    @NotNull(message = "Дата обмена должна быть заполнена")
    private LocalDate exchangeDate;
}
