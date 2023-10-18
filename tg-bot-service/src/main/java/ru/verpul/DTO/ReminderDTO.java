package ru.verpul.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReminderDTO {

    private String title;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate expireDate;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime remindTime;

    private String comment;
}
