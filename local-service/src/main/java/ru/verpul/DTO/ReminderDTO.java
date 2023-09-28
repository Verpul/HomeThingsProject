package ru.verpul.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReminderDTO {
    private Long id;

    private String title;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate expireDate;

//    private LocalTime reminderTime;

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
