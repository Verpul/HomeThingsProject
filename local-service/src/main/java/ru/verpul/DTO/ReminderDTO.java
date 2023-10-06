package ru.verpul.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReminderDTO {
    private Long id;

    @NotBlank(message = "Поле название должно быть заполнено")
    private String title;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @FutureOrPresent(message = "Дата события не может быть меньше текущей")
    private LocalDate expireDate;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @FutureOrPresent(message = "Дата напоминания не может быть меньше текущей")
    private LocalDate remindDate;

    @JsonFormat(pattern = "HH:mm")
    @Future(message = "Время напоминания не может быть меньше текущего")
    private LocalTime remindTime;

    @Length(max = 500, message = "Комментарий не может превышать 500 символов")
    private String comment;

    private Long parentId;

    private Integer nestingDepth;

    private Long categoryId;

    private Boolean periodic;

    private String period;

    private Integer periodicity;

    private String declensionedPeriod;
}
