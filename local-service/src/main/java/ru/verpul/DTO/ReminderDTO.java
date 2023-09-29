package ru.verpul.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

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


//    private Boolean repeatable;
//
//    private Boolean saveHistory;
//
//    private Period period;
//
//    private Integer periodLength;
//
//    private String price;
//
//    private Set<ReminderDateDTO> reminderDates = new HashSet<>();
}
