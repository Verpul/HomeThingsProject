package ru.verpul.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.verpul.DTO.validator.PeriodicReminder;
import ru.verpul.DTO.validator.RemindTimeFuture;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@PeriodicReminder(message = "Период, периодичность и дата события должны быть заполнены")
@RemindTimeFuture(message = "Время напоминания не может быть меньше текущего")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReminderDTO {
    private Long id;

    @NotBlank(message = "Название должно быть заполнено")
    @Size(max = 200, message = "Название не может превышать 200 символов")
    private String title;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @FutureOrPresent(message = "Дата события не может быть меньше текущей")
    private LocalDate expireDate;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @FutureOrPresent(message = "Дата напоминания не может быть меньше текущей")
    private LocalDate remindDate;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime remindTime;

    @Size(max = 500, message = "Комментарий не может превышать 500 символов")
    private String comment;

    private Long parentId;

    private Integer nestingDepth;

    private Long categoryId;

    private Boolean periodic;

    private String period;

    @Positive(message = "Периодичность не может быть меньше 1")
    private Integer periodicity;

    private String declensionPeriod;

    private Boolean completed;
}
